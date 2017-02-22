package io.mc.letsmock.demo;

import java.util.UUID;

public interface UserService {
    void startResetPasswordProcess(
            String userEmailAddress);

    void finishResetPasswordProcess(
            String userEmailAddress,
            String newPassword,
            UUID resetPasswordToken);
}
