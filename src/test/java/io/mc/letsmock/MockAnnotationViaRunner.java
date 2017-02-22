package io.mc.letsmock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MockAnnotationViaRunner {
    @Mock
    private DummyService dummyService;

    @InjectMocks
    private DummyServiceClient dummyServiceClient;

    @Test
    public void mockWasCreated() {
        dummyService.methodReturningVoid();

        verify(dummyService).methodReturningVoid();
    }

    @Test
    public void mockWasInjectedIntoClient() {
        dummyServiceClient.test();
    }
}
