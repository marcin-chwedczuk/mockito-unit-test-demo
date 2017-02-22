package io.mc.letsmock;

import java.util.List;

public interface DummyService {
    void methodReturningVoid();
    void methodReturningVoidWithParameters(int anInteger, String aString);
    void methodReturningVoidWithCollectionParameter(List<String> aList);

    int methodReturningPrimitiveInt();
    Integer methodReturningPrimitiveWrapper();
    String methodReturningString();
    List<String> methodReturningCollection();

    User methodReturningCustomTypeAndTakingParameters(String userName);
    User complexMethod(String aString, int anInteger);

    void processUser(User user);
}
