package utils;

import org.example.Main;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Hash {
    public static String salt;
    public static String hash;

    public static String generateSalt() {
        SecureRandom randomValue = new SecureRandom();
        byte[] salt = new byte[16];
        randomValue.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    public static String hash(String text, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedString = salt + text;
            byte[] hashString = digest.digest(saltedString.getBytes());
            StringBuilder hexString = new StringBuilder();
            for(byte b: hashString) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
