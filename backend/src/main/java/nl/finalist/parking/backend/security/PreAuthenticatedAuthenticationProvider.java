package nl.finalist.parking.backend.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class PreAuthenticatedAuthenticationProvider implements AuthenticationProvider {

    public PreAuthenticatedAuthenticationProvider() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return (PreAuthenticatedAuthenticationToken) authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.equals(authentication);
    }
}
