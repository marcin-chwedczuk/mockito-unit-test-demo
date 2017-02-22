package io.mc.letsmock.demo;

import java.time.LocalDateTime;
import java.util.UUID;

public class ResetPasswordNotificationData {
    private final String emailAddress;
    private final UUID resetPasswordToken;
    private final LocalDateTime resetPasswordTokenValidityEndDate;

    public ResetPasswordNotificationData(String emailAddress, UUID resetPasswordToken, LocalDateTime resetPasswordTokenValidityEndDate) {
        this.emailAddress = emailAddress;
        this.resetPasswordToken = resetPasswordToken;
        this.resetPasswordTokenValidityEndDate = resetPasswordTokenValidityEndDate;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public UUID getResetPasswordToken() {
        return resetPasswordToken;
    }

    public LocalDateTime getResetPasswordTokenValidityEndDate() {
        return resetPasswordTokenValidityEndDate;
    }
}
