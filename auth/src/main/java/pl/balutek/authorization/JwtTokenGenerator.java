package pl.balutek.authorization;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenGenerator {

    private PrivateKey privateRsaKey;

    @PostConstruct
    public void loadPrivateKey() throws Exception {
        byte[] privateKeyBytes =
            Files.readAllBytes(new ClassPathResource("shop_private.der").getFile().toPath());
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");
        privateRsaKey = rsaKeyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    public String generate(User user) {
        return Jwts.builder()
            .setIssuedAt(new Date())
            .setSubject(user.getLogin())
            .signWith(SignatureAlgorithm.RS512, privateRsaKey)
            .compact();
    }

}
