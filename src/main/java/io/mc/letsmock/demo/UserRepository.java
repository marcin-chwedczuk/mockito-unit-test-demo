package io.mc.letsmock.demo;

public interface UserRepository {
    User findByEmailAddress(String emailAddress);
}
