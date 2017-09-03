package pl.balutek.authorization;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(
        AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authenticate(
        @RequestParam String login,
        @RequestParam String password) {
        return authService.authenticateAndGenerateToken(login, password)
            .map(jwtToken -> ResponseEntity.status(HttpStatus.OK).body(jwtToken))
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(StringUtils.EMPTY));
    }
}
