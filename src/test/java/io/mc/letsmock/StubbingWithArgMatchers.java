package io.mc.letsmock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import static io.mc.letsmock.CustomMatcher.anyStringStartingWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StubbingWithArgMatchers {

    private DummyService dummyService;

    @Before
    public void before() {
        dummyService = mock(DummyService.class);
    }

    @Test
    public void anyMatchers() {
        User joe = new User("joe");

        when(dummyService.complexMethod(anyString(), anyInt()))
                .thenReturn(joe);

        assertThat(dummyService.complexMethod("foo", 3))
                .isSameAs(joe);

        assertThat(dummyService.complexMethod("bar", 101))
                .isSameAs(joe);
    }

    @Test
    public void argThatMatcher() {
        User joe = new User("joe");

        when(dummyService.complexMethod(
                argThat(s -> s.startsWith("joe")),
                anyInt()))
            .thenReturn(joe);

        assertThat(dummyService.complexMethod("joester", 1))
                .isSameAs(joe);

        assertThat(dummyService.complexMethod("joeie", 11))
                .isSameAs(joe);

        assertThat(dummyService.complexMethod("foo", 1))
                .isNull();
    }

    @Test
    public void customMatcher() {
        User joe = new User("joe");
        User mary = new User("mary");

        when(dummyService.complexMethod(
                anyStringStartingWith("joe"),
                anyInt()))
            .thenReturn(joe);

        when(dummyService.complexMethod(
                anyStringStartingWith("mary"),
                anyInt()))
            .thenReturn(mary);

        assertThat(dummyService.complexMethod("joester", 1))
                .isSameAs(joe);

        assertThat(dummyService.complexMethod("mary",3))
                .isSameAs(mary);
    }

    @Test
    public void partialMatch() {
        User joe = new User("joe");
        User mary = new User("mary");

        when(dummyService.complexMethod(anyString(), eq(2)))
                .thenReturn(joe);

        when(dummyService.complexMethod(anyString(), eq(1)))
                .thenReturn(mary);

        // Invalid:
        // when(dummyService.complexMethod(anyString(), 3))
        //        .thenReturn(null);

        assertThat(dummyService.complexMethod("foo", 2))
                .isSameAs(joe);

        assertThat(dummyService.complexMethod("bar", 1))
                .isSameAs(mary);
    }
}
