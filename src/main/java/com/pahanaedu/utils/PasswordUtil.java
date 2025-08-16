package com.pahanaedu.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;


public final class PasswordUtil {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_SIZE = 16; // 16 bytes = 128 bits

    private PasswordUtil() {}

    public static String hashPassword(String plainPassword) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_SIZE];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);

            md.update(salt);

            byte[] hashedPassword = md.digest(plainPassword.getBytes());

            byte[] combined = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(hashedPassword, 0, combined, salt.length, hashedPassword.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean verifyPassword(String plainPassword, String storedHashedPassword) {
        try {
            byte[] combined = Base64.getDecoder().decode(storedHashedPassword);

            byte[] salt = Arrays.copyOfRange(combined, 0, SALT_SIZE);

            byte[] originalHash = Arrays.copyOfRange(combined, SALT_SIZE, combined.length);

            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] newHash = md.digest(plainPassword.getBytes());

            return Arrays.equals(originalHash, newHash);

        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}