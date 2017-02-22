package io.mc.letsmock.demo;

public class Fixtures {

    public static User userJoe() {
        User joe = new User();

        joe.setEmail("joe@example.com");
        joe.setPasswordHash("old-password-hash");

        joe.setResetPasswordToken(null);
        joe.setResetPasswordTokenValidityEndDate(null);

        return joe;
    }
}
