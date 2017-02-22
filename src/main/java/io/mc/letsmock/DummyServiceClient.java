package io.mc.letsmock;

import java.util.Objects;

public class DummyServiceClient {
    private final DummyService dummyService;

    public DummyServiceClient(DummyService dummyService) {
        this.dummyService = Objects.requireNonNull(dummyService);
    }

    public void test() {
        dummyService.methodReturningVoid();
    }
}
