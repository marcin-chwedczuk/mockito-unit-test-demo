package io.mc.letsmock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

public class VerifyMockBehaviour {
    private DummyService dummyService;

    @Before
    public void before() {
        dummyService = mock(DummyService.class);
    }

    @Test
    public void verifyMethodCalledWithSpecifiedArguments() {
        dummyService.complexMethod("joe", 3);

        verify(dummyService).complexMethod("joe", 3);
    }

    @Test
    public void verifyWithArgumentMatchers() {
        dummyService.complexMethod("joe", 11);

        verify(dummyService).complexMethod(startsWith("j"), anyInt());
    }

    @Test
    public void orderDoesntMatter() {
        dummyService.complexMethod("foo", 1);
        dummyService.complexMethod("bar", 1);

        verify(dummyService).complexMethod("bar", 1);
        verify(dummyService).complexMethod("foo", 1);
    }

    @Test
    public void orderMatters() {
        dummyService.complexMethod("foo", 1);
        dummyService.complexMethod("bar", 1);

        InOrder inOrder = Mockito.inOrder(dummyService);

        inOrder.verify(dummyService).complexMethod("foo", 1);
        inOrder.verify(dummyService).complexMethod("bar", 1);
    }

    @Test
    public void methodNotCalled() {
        verify(dummyService, never()).methodReturningVoid();
    }

    @Test
    public void methodCalledSpecificNumberOfTimes() {
        dummyService.methodReturningVoid();
        dummyService.methodReturningVoid();
        dummyService.methodReturningVoid();

        verify(dummyService, times(3))
                .methodReturningVoid();
    }

    @Test
    public void verifyWithArgumentCaptor() {
        User joe = new User("joe");
        dummyService.processUser(joe);

        ArgumentCaptor<User> userUsedInCallCaptor =
                        ArgumentCaptor.forClass(User.class);
        verify(dummyService).processUser(userUsedInCallCaptor.capture());

        User userUsedInCall = userUsedInCallCaptor.getValue();
        assertThat(userUsedInCall.getName())
                .isEqualTo("joe");
    }

    @Test
    public void customVerifyMessage() {
        dummyService.methodReturningVoid();

        verify(dummyService,
                times(1)
                    .description("method must be called exactly once"))
            .methodReturningVoid();
    }
}
