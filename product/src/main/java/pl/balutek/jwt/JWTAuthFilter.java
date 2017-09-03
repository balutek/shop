package pl.balutek.jwt;

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
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

public class JWTAuthFilter extends GenericFilterBean {

    private static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";

    private PublicKey publicRsaKey;

    @PostConstruct
    public void loadPublicKey() {
        try {
            byte[] publicKeyBytes =
                Files.readAllBytes(new ClassPathResource("shop_public.der").getFile().toPath());
            X509EncodedKeySpec pkcs8EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");
            publicRsaKey = rsaKeyFactory.generatePublic(pkcs8EncodedKeySpec);
        }
        catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException exception) {
            Assert.state(false, "Could not setup public key");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Optional.ofNullable(request.getHeader(X_AUTH_TOKEN))
            .flatMap(this::extractJwsClaims)
            .map(this::createAuthTokenFromClaims)
            .ifPresent(jwtAuthToken ->
                SecurityContextHolder.getContext().setAuthentication(jwtAuthToken));

        chain.doFilter(servletRequest, servletResponse);
    }

    private Optional<Jws<Claims>> extractJwsClaims(String token) {
        try {
            return Optional.of(
                Jwts.parser()
                    .setSigningKey(publicRsaKey)
                    .parseClaimsJws(token));
        } catch (JwtException | IllegalArgumentException notParsedException) {
            return Optional.empty();
        }

    }

    private JWTAuthToken createAuthTokenFromClaims(Jws<Claims> jwsClaims) {
        return JWTAuthToken.builder()
            .login(jwsClaims.getBody().getSubject())
            .authenticatedFlag(true)
            .build();
    }

}
