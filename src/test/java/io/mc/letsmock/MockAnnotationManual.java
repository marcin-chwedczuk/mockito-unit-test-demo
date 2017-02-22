package io.mc.letsmock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class MockAnnotationManual {
    @Mock
    private DummyService dummyService;

    @InjectMocks
    private DummyServiceClient dummyServiceClient;

    @Before
    public void setUp() {
        // preferably into base class
        MockitoAnnotations.initMocks(this);
    }

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
