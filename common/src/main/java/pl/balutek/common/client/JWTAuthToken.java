package pl.balutek.common.client;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Builder;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pl.balutek.common.contract.auth.AuthData;

@Builder
public class JWTAuthToken implements Authentication {

    private AuthData authData;

    public JWTAuthToken(AuthData authData) {
        this.authData = authData;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authData.getRoles()
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return authData.getJti();
    }

    @Override
    public Object getPrincipal() {
        return authData.getLogin();
    }

    @Override
    public boolean isAuthenticated() {
        return StringUtils.isNotBlank(authData.getJti());
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    @Override
    public String getName() {
        return authData.getLogin();
    }
}
