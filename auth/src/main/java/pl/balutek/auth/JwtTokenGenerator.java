package pl.balutek.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import javax.annotation.PostConstruct;
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
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(new Date())
            .setSubject(user.getLogin())
            .claim("roles", Collections.singletonList("USER"))
            .signWith(SignatureAlgorithm.RS512, privateRsaKey)
            .compact();
    }

}
