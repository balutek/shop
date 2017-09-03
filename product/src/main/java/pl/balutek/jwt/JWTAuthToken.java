package pl.balutek.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import java.util.Collection;
import java.util.Collections;
import lombok.Builder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Builder
public class JWTAuthToken implements Authentication {
    private String login;
    private boolean authenticatedFlag;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return login;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticatedFlag;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticatedFlag = isAuthenticated;
    }

    @Override
    public String getName() {
        return login;
    }
}
