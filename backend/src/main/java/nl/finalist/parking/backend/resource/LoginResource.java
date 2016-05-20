package nl.finalist.parking.backend.resource;

import nl.finalist.parking.backend.entity.Authority;
import nl.finalist.parking.backend.entity.Employee;
import nl.finalist.parking.backend.repository.EmployeeRepository;
import nl.finalist.parking.backend.security.GoogleAuthHelper;
import nl.finalist.parking.backend.security.TokenProvider;
import nl.finalist.parking.backend.security.XAuthTokenFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class LoginResource {
    @Inject
    GoogleAuthHelper googleAuthHelper;

    @Inject
    private EmployeeRepository employeeRepository;

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/googleloginlink", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGoogleLink() {
        String url = googleAuthHelper.buildLoginUrl();

        return new ResponseEntity<>(url, HttpStatus.OK);
    }

    @RequestMapping(value = "/googlelogin", method = RequestMethod.GET)
    @Transactional
    public void authenticateWithGoogle(@RequestParam(value = "code") String authCode, HttpServletResponse response) {
        String googleUserEmail = googleAuthHelper.getUserEmail(authCode);
        Optional<Employee> employeeOptional = employeeRepository.findOneByEmail(googleUserEmail);

        if (!employeeOptional.isPresent()) {
            Cookie ckError = new Cookie("x-auth-error", "Je account staat niet in de database.");
            ckError.setPath("/");
            response.addCookie(ckError);

            try {
                response.sendRedirect("/");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Employee employee = employeeOptional.get();
            List<? extends GrantedAuthority> authorities =
                    employee.getAuthorities().stream().map(Authority::getName).map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
            PreAuthenticatedAuthenticationToken token =
                    tokenProvider.createTokenFromDetails(employee.getId(), employee.getEmail(), authorities);
            Authentication authentication = authenticationManager.authenticate(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Cookie tokenCookie =
                    new Cookie(XAuthTokenFilter.XAUTH_TOKEN_HEADER_NAME, tokenProvider.getXAuthToken(token));
            tokenCookie.setPath("/");
            response.addCookie(tokenCookie);

            Cookie expiryCookie = new Cookie("x-auth-token-expires", tokenProvider.getExpiresFromAuthentication(token));
            expiryCookie.setPath("/");
            response.addCookie(expiryCookie);

            // TODO: Redirecting to /#/ from the backend is NOT loosely coupled. Redirecting to "/" however breaks
            // the deep linking support in the front-end
            try {
                response.sendRedirect("/#/");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
