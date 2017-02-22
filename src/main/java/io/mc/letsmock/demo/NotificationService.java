package io.mc.letsmock.demo;

public interface NotificationService {
    void sendResetPasswordNotification(ResetPasswordNotificationData data);

    void sendPasswordChangedConfirmation(String email);
}
