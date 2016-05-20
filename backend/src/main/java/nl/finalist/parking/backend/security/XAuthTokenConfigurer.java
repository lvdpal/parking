package nl.finalist.parking.backend.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.inject.Inject;

@Configuration
public class XAuthTokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Inject
    private XAuthTokenFilter customFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(customFilter, RequestHeaderAuthenticationFilter.class);
    }
}
