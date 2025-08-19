package com.pahanaedu.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    @DisplayName("Hashing a password should return a non-empty, non-null string")
    void hashPassword_shouldReturnNonEmptyString() {
        // Arrange
        String plainPassword = "mySecurePassword123";

        // Act
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        // Assert
        assertNotNull(hashedPassword, "The hashed password should not be null.");
        assertFalse(hashedPassword.isEmpty(), "The hashed password should not be empty.");
        assertNotEquals(plainPassword, hashedPassword, "The hashed password should not be the same as the plain password.");
    }

    @Test
    @DisplayName("Verifying a correct password against its hash should return true")
    void verifyPassword_withCorrectPassword_shouldReturnTrue() {
        // Arrange
        String plainPassword = "correct_password";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        // Act
        boolean isPasswordCorrect = PasswordUtil.verifyPassword(plainPassword, hashedPassword);

        // Assert
        assertTrue(isPasswordCorrect, "Verification should succeed for the correct password.");
    }

    @Test
    @DisplayName("Verifying an incorrect password against a hash should return false")
    void verifyPassword_withIncorrectPassword_shouldReturnFalse() {
        // Arrange
        String plainPassword = "correct_password";
        String incorrectPassword = "wrong_password";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        // Act
        boolean isPasswordCorrect = PasswordUtil.verifyPassword(incorrectPassword, hashedPassword);

        // Assert
        assertFalse(isPasswordCorrect, "Verification should fail for an incorrect password.");
    }

    @Test
    @DisplayName("Hashing the same password twice should produce two different hashes due to salting")
    void hashPassword_calledTwice_shouldProduceDifferentHashes() {
        // Arrange
        String plainPassword = "passwordWithSalt";

        // Act
        String hash1 = PasswordUtil.hashPassword(plainPassword);
        String hash2 = PasswordUtil.hashPassword(plainPassword);

        // Assert
        assertNotNull(hash1);
        assertNotNull(hash2);
        assertNotEquals(hash1, hash2, "Two hashes of the same password should be different because of the random salt.");
    }
}