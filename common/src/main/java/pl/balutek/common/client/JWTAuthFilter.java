package pl.balutek.common.client;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import pl.balutek.common.contract.auth.AuthClient;

public class JWTAuthFilter extends GenericFilterBean {

    private static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";

    private final AuthClient authClient;

    @Autowired
    public JWTAuthFilter(AuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Optional.ofNullable(request.getHeader(X_AUTH_TOKEN))
            .map(authClient::parseToken)
            .map(JWTAuthToken::new)
            .ifPresent(jwtAuthToken ->
                SecurityContextHolder.getContext().setAuthentication(jwtAuthToken));

        chain.doFilter(servletRequest, servletResponse);
    }

}
