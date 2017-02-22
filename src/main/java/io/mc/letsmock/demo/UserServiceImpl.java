package io.mc.letsmock.demo;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final DateTimeProvider dateTimeProvider;
    private final CryptoService cryptoService;

    public UserServiceImpl(UserRepository userRepository, DateTimeProvider dateTimeProvider, CryptoService cryptoService, NotificationService notificationService) {
        this.userRepository = requireNonNull(userRepository);
        this.notificationService = requireNonNull(notificationService);
        this.dateTimeProvider = requireNonNull(dateTimeProvider);
        this.cryptoService = requireNonNull(cryptoService);
    }

    @Override
    public void startResetPasswordProcess(String userEmailAddress) {
        User user = userRepository.findByEmailAddress(userEmailAddress);
        if (user == null)
            return;

        UUID token = UUID.randomUUID();
        LocalDateTime tokenValidityEndDate =
                dateTimeProvider.now().plusDays(1);

        user.setResetPasswordToken(token);
        user.setResetPasswordTokenValidityEndDate(tokenValidityEndDate);

        ResetPasswordNotificationData notificationData = new ResetPasswordNotificationData(
                user.getEmail(),
                token,
                tokenValidityEndDate);

        notificationService.sendResetPasswordNotification(notificationData);
    }

    @Override
    public void finishResetPasswordProcess(
            String userEmailAddress,
            String newPassword,
            UUID resetPasswordToken)
    {
        User user = userRepository.findByEmailAddress(userEmailAddress);
        if (user == null)
            return;

        if (user.getResetPasswordToken() == null)
            return;

        if (!user.getResetPasswordToken().equals(resetPasswordToken))
            return;

        if (user.getResetPasswordTokenValidityEndDate()
                .isBefore(dateTimeProvider.now()))
            return;

        user.setResetPasswordToken(null);
        user.setResetPasswordTokenValidityEndDate(null);

        String newPasswordHash = cryptoService.sha1(newPassword);
        user.setPasswordHash(newPasswordHash);

        notificationService.sendPasswordChangedConfirmation(user.getEmail());
    }
}
