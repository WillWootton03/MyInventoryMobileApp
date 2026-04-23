package com.zybooks.cs360finalproject;

import android.util.Base64;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

// Utility for hashing and verifying hashed passwords
public class PasswordUtil {

    // Salting adds a unique key to the end of hashing securing hashed passwords even more
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 10000; // Amount of times to repeat hashing in spec
    private static final int KEY_LENGTH = 256; // len in bits of final hash

    public static byte[] generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH]; // Generate a new byte array with then len of SALT_LEN
        random.nextBytes(salt); // Fills the salt array with random bytes in each iteration
        return salt;
    }

    // Hashes a plaintext password and adds salt key to it
    public static String hashPassword(String password, byte[] salt) {
        try {
            // create a hash specification based on params
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            // selects algorithm to use with hash
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            // generates the unique hash for the password
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.encodeToString(hash, Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Verifies a plaintext input using SALT and the hashed password stored in Users table
    public static boolean verifyPassword(String plaintextPassword, String storedPassHash, byte[] salt) {
        String newHash = hashPassword(plaintextPassword, salt);
        return newHash.equals(storedPassHash); // Returns true or false depending if password match
    }

}