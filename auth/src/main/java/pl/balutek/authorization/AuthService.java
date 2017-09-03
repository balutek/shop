package pl.balutek.authorization;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AuthService {

    @Value("${password.sha512.salt}")
    private String hashSalt;

    private final UserRepository userRepository;

    private final JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    public AuthService(UserRepository userRepository,
        JwtTokenGenerator jwtTokenGenerator) {
        this.userRepository = userRepository;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    // TODO: create separate user microservice to provide users data or sth similar
    @PostConstruct
    public void setUp() {
        Optional<String> hashOpt = generateSHA512("test");

        Assert.isTrue(hashOpt.isPresent(), "Hash could not be created");

        hashOpt.ifPresent(passwordHash ->
            userRepository.save(
                User.builder()
                    .login("user")
                    .passwordHash(passwordHash)
                    .build()));

    }

    public Optional<String> authenticateAndGenerateToken(String login, String password) {
        return generateSHA512(password)
            .map(passwordHash ->
                userRepository.getUserByLoginAndPasswordHash(login, passwordHash)
                    .map(jwtTokenGenerator::generate)
                    .orElse(StringUtils.EMPTY));
    }


    public Optional<String> generateSHA512(String valueToEncrypt) {
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
