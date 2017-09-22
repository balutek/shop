package pl.balutek.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class JwtTokenParser {

    private PublicKey publicRsaKey;

    @PostConstruct
    public void loadPublicKey() {
        try {
            byte[] publicKeyBytes =
                Files.readAllBytes(new ClassPathResource("shop_public.der").getFile().toPath());
            X509EncodedKeySpec pkcs8EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");
            publicRsaKey = rsaKeyFactory.generatePublic(pkcs8EncodedKeySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException exception) {
            Assert.state(false, "Could not setup public key");
        }
    }

    public Optional<Jws<Claims>> parse(String token) {
        try {
            return Optional.of(
                Jwts.parser()
                    .setSigningKey(publicRsaKey)
                    .parseClaimsJws(token));
        } catch (JwtException | IllegalArgumentException notParsedException) {
            return Optional.empty();
        }
    }

}
