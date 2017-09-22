package pl.balutek.common.contract.auth;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthData {
    private String jti;
    private String login;
    private List<String> roles;
}
