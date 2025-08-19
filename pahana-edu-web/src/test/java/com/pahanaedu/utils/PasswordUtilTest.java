//package com.pahanaedu.utils;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DisplayName("Password Utility Tests")
//class PasswordUtilTest {
//
//    @Test
//    @DisplayName("Hashing a password should return a non-null, non-empty string")
//    void hashPassword_ShouldReturnValidHashString() {
//        String password = "mySecretPassword!@#";
//        String hash = PasswordUtil.hashPassword(password);
//        assertNotNull(hash, "Hash should not be null");
//        assertFalse(hash.isEmpty(), "Hash should not be empty");
//    }
//
//    @Test
//    @DisplayName("Hashing the same password twice should produce different hashes")
//    void hashPassword_ShouldProduceDifferentHashes_ForSamePassword_DueToSalt() {
//        String password = "mySecretPassword!@#";
//        String hash1 = PasswordUtil.hashPassword(password);
//        String hash2 = PasswordUtil.hashPassword(password);
//        assertNotEquals(hash1, hash2, "Hashes should be different due to random salting");
//    }
//
//    @Test
//    @DisplayName("Verification should return true for the correct password")
//    void verifyPassword_ShouldReturnTrue_ForCorrectPassword() {
//        String password = "mySecretPassword!@#";
//        String hash = PasswordUtil.hashPassword(password);
//        assertTrue(PasswordUtil.verifyPassword(password, hash), "Verification should succeed for the correct password");
//    }
//
//    @Test
//    @DisplayName("Verification should return false for an incorrect password")
//    void verifyPassword_ShouldReturnFalse_ForIncorrectPassword() {
//        String password = "mySecretPassword!@#";
//        String hash = PasswordUtil.hashPassword(password);
//        assertFalse(PasswordUtil.verifyPassword("thisIsTheWrongPassword", hash), "Verification should fail for an incorrect password");
//    }
//
//    @Test
//    @DisplayName("Verification should return false for a malformed or invalid hash string")
//    void verifyPassword_ShouldReturnFalse_ForInvalidHash() {
//        String password = "mySecretPassword!@#";
//        String invalidHash = "thisIsNotAValidBase64Hash";
//        assertFalse(PasswordUtil.verifyPassword(password, invalidHash), "Verification should fail for a malformed hash");
//    }
//}