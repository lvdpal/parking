package nl.finalist.parking.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */
@Component
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(Http401UnauthorizedEntryPoint.class);

    /**
     * Always returns a 401 error code to the client.
     */
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authenticationException) throws IOException, ServletException {

        LOG.debug("request URL: " + request.getMethod() + " " + request.getRequestURL().toString());
        LOG.debug("authenticationException: " + authenticationException.getMessage());

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }
}
