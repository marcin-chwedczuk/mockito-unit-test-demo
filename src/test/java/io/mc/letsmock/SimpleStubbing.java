package io.mc.letsmock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Null;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleStubbing {

    private DummyService dummyService;

    @Before
    public void before() {
        dummyService = mock(DummyService.class);
    }

    @Test
    public void returnGivenPrimitiveValue() {
        when(dummyService.methodReturningPrimitiveInt())
                .thenReturn(13);

        assertThat(dummyService.methodReturningPrimitiveInt())
               .isEqualTo(13);

        // second time:
        assertThat(dummyService.methodReturningPrimitiveInt())
               .isEqualTo(13);
    }

    @Test
    public void returnGivenCollection() {
        List<String> myStrings = Arrays.asList(
                "foo",
                "bar",
                "nyu"
        );

        when(dummyService.methodReturningCollection())
                .thenReturn(myStrings);

        assertThat(dummyService.methodReturningCollection())
                .contains("foo", "bar", "nyu")
                .hasSize(3);
    }

    @Test
    public void returnBasedOnParameters() {
        User joe = new User("joe");
        User mary = new User("mary");

        when(dummyService.complexMethod("joe", 1))
                .thenReturn(joe);

        when(dummyService.complexMethod("mary", 1))
                .thenReturn(mary);

        assertThat(dummyService.complexMethod("joe", 1))
                .isSameAs(joe);

        assertThat(dummyService.complexMethod("mary", 1))
                .isSameAs(mary);

        assertThat(dummyService.complexMethod("joe", 13))
                .isNull();
    }

    @Test
    public void eachCallsReturnsDifferentValue() {
        when(dummyService.methodReturningString())
                .thenReturn("foo")
                .thenReturn("bar")
                .thenReturn("nyu");

        assertThat(dummyService.methodReturningString())
                .isEqualTo("foo");

        assertThat(dummyService.methodReturningString())
                .isEqualTo("bar");

        assertThat(dummyService.methodReturningString())
                .isEqualTo("nyu");
    }

    @Test
    public void throwException() {
        when(dummyService.methodReturningString())
                .thenThrow(new NullPointerException());

        assertThatThrownBy(() -> {
            dummyService.methodReturningString();
        })
        .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void throwExceptionFromVoidMethod() {
        doThrow(new NullPointerException())
            .when(dummyService).methodReturningVoid();

        assertThatThrownBy(() -> {
            dummyService.methodReturningVoid();
        })
        .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void thrownBasedOnParameters() {
        when(dummyService.complexMethod("invalid-user", 1))
                .thenThrow(new IllegalArgumentException());

        assertThat(dummyService.complexMethod("valid-user", 1))
                .isNull();

        assertThatThrownBy(() -> {
            dummyService.complexMethod("invalid-user", 1);
        })
        .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void customActionOnMethodCall() {
        when(dummyService.methodReturningString())
            .thenAnswer(invocation -> {
                Object[] args = invocation.getArguments();
                return "args: " + Arrays.asList(args).toString();
            });

        assertThat(dummyService.methodReturningString())
            .isEqualTo("args: []");
    }
}
