package nl.finalist.parking.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * Filters incoming requests and installs a Spring Security principal
 * if a header corresponding to a valid user is found.
 */
@Component
public class XAuthTokenFilter extends GenericFilterBean {
    private static final Logger LOG = LoggerFactory.getLogger(XAuthTokenFilter.class);

    public static final String XAUTH_TOKEN_HEADER_NAME = "x-auth-token";

    @Inject
    private TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String authToken = httpServletRequest.getHeader(XAUTH_TOKEN_HEADER_NAME);

        Enumeration<String> headerNamesEnumeration = httpServletRequest.getHeaderNames();
        List<String> headerNames = new ArrayList<>();

        while (headerNamesEnumeration.hasMoreElements()) {
            headerNames.add(headerNamesEnumeration.nextElement());
        }

        LOG.debug("headerNames: " + String.join(", ", headerNames));

        if (StringUtils.hasText(authToken)) {
            Optional<String> tokenErrorOptional = tokenProvider.validateXAuthToken(authToken);

            if (tokenErrorOptional.isPresent()) {
                LOG.debug("authToken error: " + authToken);
            } else {
                PreAuthenticatedAuthenticationToken token = tokenProvider.createTokenFromXAuthToken(authToken);
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        } else {
            LOG.debug("authToken empty");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
