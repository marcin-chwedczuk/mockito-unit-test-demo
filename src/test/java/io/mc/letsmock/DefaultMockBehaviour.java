package io.mc.letsmock;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class DefaultMockBehaviour {

    private DummyService dummyService;

    @Before
    public void before() {
        dummyService = mock(DummyService.class);
    }

    @Test
    public void methodReturningVoid() {
        dummyService.methodReturningVoid();
    }

    @Test
    public void methodReturningVoidWithParameters() {
        dummyService.methodReturningVoidWithParameters(0, null);
        dummyService.methodReturningVoidWithParameters(101, "foo");
    }

    @Test
    public void methodReturningVoidWithCollectionParameter() {
        dummyService.methodReturningVoidWithCollectionParameter(null);
        dummyService.methodReturningVoidWithCollectionParameter(Arrays.asList("foo"));
        dummyService.methodReturningVoidWithCollectionParameter(new ArrayList<>());
    }

    @Test
    public void methodReturningPrimitiveInt() {
        int result =
            dummyService.methodReturningPrimitiveInt();

        assertThat(result)
                .isEqualTo(0);
    }

    @Test
    public void methodReturningPrimitiveWrapper() {
        Integer result =
            dummyService.methodReturningPrimitiveWrapper();

        assertThat(result)
                .isEqualTo(0);
    }

    @Test
    public void methodReturningString() {
        String result =
            dummyService.methodReturningString();

        assertThat(result)
                .isNull();
    }

    @Test
    public void methodReturningCollection() {
        List<String> result =
            dummyService.methodReturningCollection();

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void methodReturningCustomTypeAndTakingParameters() {
        User user = dummyService
                .methodReturningCustomTypeAndTakingParameters(null);

        assertThat(user)
                .isNull();
    }
}
