package pl.balutek.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class AuthUtils {

    public static Optional<String> generateSHA512(String valueToEncrypt, String hashSalt) {
        try {
            MessageDigest sha512Algorithm = MessageDigest.getInstance("SHA-512");
            sha512Algorithm.update(hashSalt.getBytes());
            byte[] hashBytes = sha512Algorithm.digest(valueToEncrypt.getBytes());
            return Optional.of(new String(hashBytes));
        } catch (NoSuchAlgorithmException e) {
            return Optional.empty();
        }
    }
}
