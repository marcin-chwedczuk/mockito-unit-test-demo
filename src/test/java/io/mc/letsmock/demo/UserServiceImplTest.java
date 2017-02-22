package io.mc.letsmock.demo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    private UserRepository userRepository;
    private DateTimeProvider dateTimeProvider;
    private CryptoService cryptoService;
    private NotificationService notificationService;

    private UserServiceImpl userService;

    private User joe;
    private UUID dummyToken;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        dateTimeProvider = mock(DateTimeProvider.class);
        cryptoService = mock(CryptoService.class);
        notificationService = mock(NotificationService.class);

        userService = new UserServiceImpl(
                userRepository,
                dateTimeProvider,
                cryptoService,
                notificationService);

        joe = Fixtures.userJoe();

        joe.setResetPasswordToken(null);
        joe.setResetPasswordTokenValidityEndDate(null);

        dummyToken = UUID.fromString("2554ccbc-f5fb-11e6-bc64-92361f002671");

        when(userRepository.findByEmailAddress("joe@example.com"))
                .thenReturn(joe);

        when(dateTimeProvider.now())
                .thenReturn(LocalDateTime.of(2017,3,10, 0,0));
    }

    @Test
    public void startResetPasswordProcess_givenEmailNotBelongingToAnyUser_doesNothing() {
        // arrange
        when(userRepository.findByEmailAddress("unknown@example.com"))
                .thenReturn(null);

        // act
        userService.startResetPasswordProcess("unknown@example.com");

        // assert
        verify(notificationService, never())
                .sendResetPasswordNotification(any());

        verify(notificationService, never())
                .sendPasswordChangedConfirmation(anyString());
    }

    @Test
    public void startResetPasswordProcess_givenEmailOfExistingUser_generatesToken() {
        // arrange
        User joe = Fixtures.userJoe();

        when(userRepository.findByEmailAddress("joe@example.com"))
                .thenReturn(joe);

        when(dateTimeProvider.now())
                .thenReturn(LocalDateTime.of(2017,3,10, 0,0));

        // act
        userService.startResetPasswordProcess("joe@example.com");

        // assert
        assertThat(joe.getResetPasswordToken())
                .isNotNull();

        assertThat(joe.getResetPasswordTokenValidityEndDate())
                .isEqualTo(LocalDateTime.of(2017,3,11, 0,0));

        assertThat(joe.getPasswordHash())
                .withFailMessage("Password should not be changed")
                .isEqualTo("old-password-hash");

    }

    @Test
    public void startResetPasswordProcess_givenEmailOfExistingUser_sendsNotificationToUser() {
        // arrange

        // act
        userService.startResetPasswordProcess("joe@example.com");

        // assert
        ArgumentCaptor<ResetPasswordNotificationData> captor =
                ArgumentCaptor.forClass(ResetPasswordNotificationData.class);

        verify(notificationService, description("Notification with password reset token was not send to user"))
                .sendResetPasswordNotification(captor.capture());

        ResetPasswordNotificationData notificationData = captor.getValue();

        assertThat(notificationData)
                .isNotNull();

        assertThat(notificationData.getEmailAddress())
                .isEqualTo("joe@example.com");

        assertThat(notificationData.getResetPasswordToken())
                .isEqualTo(joe.getResetPasswordToken());

        assertThat(notificationData.getResetPasswordTokenValidityEndDate())
                .isEqualTo(joe.getResetPasswordTokenValidityEndDate());
    }

    @Test
    public void finishResetPasswordProcess_noUserHasSpecifiedEmail_doesNothing() {
        // act
        userService.finishResetPasswordProcess("unknown@example.com",
                "newPass", dummyToken);

        // assert
        assertThat(joe.getPasswordHash())
                .isEqualTo("old-password-hash");
    }

    @Test
    public void finishResetPasswordProcess_userHasNoTokenSet_doesNothing() {
        // arrange
        joe.setResetPasswordToken(null);
        joe.setResetPasswordTokenValidityEndDate(LocalDateTime.of(2020,1,1, 0,0));

        // act
        userService.finishResetPasswordProcess("joe@example.com", "newPass", dummyToken);

        // assert
        assertThat(joe.getPasswordHash())
                .isEqualTo("old-password-hash");
     }

    @Test
    public void finishResetPasswordProcess_tokenExpired_doesNothing() {
         // arrange
         joe.setResetPasswordToken(dummyToken);
         joe.setResetPasswordTokenValidityEndDate(
                 LocalDateTime.of(2010,1,1, 0,0));

         // act
         userService.finishResetPasswordProcess("joe@example.com", "newPass", dummyToken);

         // assert
         assertThat(joe.getPasswordHash())
                 .isEqualTo("old-password-hash");
    }

    @Test
    public void finishResetPasswordProcess_tokenNotMatch_doesNothing() {
         // arrange
         joe.setResetPasswordToken(dummyToken);
         joe.setResetPasswordTokenValidityEndDate(
                 LocalDateTime.of(2010,1,1, 0,0));

         // act
         userService.finishResetPasswordProcess("joe@example.com", "newPass", UUID.randomUUID());

         // assert
         assertThat(joe.getPasswordHash())
                 .isEqualTo("old-password-hash");
    }

    @Test
    public void finishResetPasswordProcess_validToken_changesPassword() {
        // arrange
        joe.setResetPasswordToken(
                UUID.fromString("2554ccbc-f5fb-11e6-bc64-92361f002671"));
        joe.setResetPasswordTokenValidityEndDate(
                LocalDateTime.of(2020,1,1, 0,0));

        when(cryptoService.sha1("newPass"))
                .thenReturn("newHash");

        // act
        userService.finishResetPasswordProcess(
                "joe@example.com",
                "newPass",
                UUID.fromString("2554ccbc-f5fb-11e6-bc64-92361f002671"));

        // assert
        assertThat(joe.getPasswordHash())
                .isEqualTo("newHash");

        assertThat(joe.getResetPasswordToken())
                .isNull();
    }

    @Test
    public void finishResetPasswordProcess_validToken_sendsConfirmationToUser() {
         // arrange
        joe.setResetPasswordToken(
                UUID.fromString("2554ccbc-f5fb-11e6-bc64-92361f002671"));
        joe.setResetPasswordTokenValidityEndDate(
                LocalDateTime.of(2020,1,1, 0,0));

        // act
        userService.finishResetPasswordProcess(
                "joe@example.com",
                "newPass",
                UUID.fromString("2554ccbc-f5fb-11e6-bc64-92361f002671"));

        // assert
        verify(notificationService, description("should send password reset confirmation to user"))
                .sendPasswordChangedConfirmation("joe@example.com");
    }
}
