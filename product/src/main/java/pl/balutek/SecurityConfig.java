package pl.balutek;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.balutek.jwt.JWTAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/product/nonAuth").permitAll()
                .antMatchers("/product/auth").hasAuthority("USER")
            .and()
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public JWTAuthFilter jwtAuthFilter() {
        return new JWTAuthFilter();
    }

}
