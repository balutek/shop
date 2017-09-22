package pl.balutek.auth;

import static pl.balutek.auth.AuthUtils.generateSHA512;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pl.balutek.common.contract.auth.AuthData;

@Component
public class AuthService {

    @Value("${password.sha512.salt}")
    private String hashSalt;

    private final UserRepository userRepository;

    private final JwtTokenGenerator jwtTokenGenerator;

    private final JwtTokenParser jwtTokenParser;

    @Autowired
    public AuthService(UserRepository userRepository,
        JwtTokenGenerator jwtTokenGenerator,
        JwtTokenParser jwtTokenParser) {
        this.userRepository = userRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtTokenParser = jwtTokenParser;
    }

    // TODO: create separate user microservice to provide users data or sth similar
    @PostConstruct
    public void setUp() {
        Optional<String> hashOpt = generateSHA512("test", hashSalt);

        Assert.isTrue(hashOpt.isPresent(), "Hash could not be created");

        hashOpt.ifPresent(passwordHash ->
            userRepository.save(
                User.builder()
                    .login("user")
                    .passwordHash(passwordHash)
                    .build()));

    }

    public Optional<String> authenticateAndGenerateToken(String login, String password) {
        return generateSHA512(password, hashSalt)
            .map(passwordHash ->
                userRepository.getUserByLoginAndPasswordHash(login, passwordHash)
                    .map(jwtTokenGenerator::generate)
                    .orElse(StringUtils.EMPTY));
    }


    public AuthData parseToken(String token) {
        return jwtTokenParser.parse(token)
            .map(this::convertToAuthData)
            .orElseGet(this::anonymousAuthData);
    }

    private AuthData convertToAuthData(Jws<Claims> claimsJws) {
        Claims body = claimsJws.getBody();
        return AuthData.builder()
            .jti(body.getId())
            .login(body.getSubject())
            .roles(body.get("roles", List.class))
            .build();
    }

    private AuthData anonymousAuthData() {
        return AuthData.builder()
            .login("anonymous")
            .roles(Collections.singletonList("ANONYMOUS"))
            .build();
    }

}
