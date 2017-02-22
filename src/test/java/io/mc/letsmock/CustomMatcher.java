package io.mc.letsmock;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

public final class CustomMatcher {
    private CustomMatcher() { }

    public static String anyStringStartingWith(String phrase) {
        return Mockito.argThat(s -> s != null && s.startsWith(phrase));
    }
}
