package io.mc.letsmock.demo;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private Long id;

    private String email;
    private String passwordHash;

    private UUID resetPasswordToken;
    private LocalDateTime resetPasswordTokenValidityEndDate;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public UUID getResetPasswordToken() {
        return resetPasswordToken;
    }
    public void setResetPasswordToken(UUID resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public LocalDateTime getResetPasswordTokenValidityEndDate() {
        return resetPasswordTokenValidityEndDate;
    }
    public void setResetPasswordTokenValidityEndDate(LocalDateTime resetPasswordTokenValidityEndDate) {
        this.resetPasswordTokenValidityEndDate = resetPasswordTokenValidityEndDate;
    }
}
