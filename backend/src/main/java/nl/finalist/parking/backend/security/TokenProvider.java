package nl.finalist.parking.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Encodes security information into an Authority for use in the Spring application as well as external use in the
 * x-auth and x-auth-expires cookies.
 */
public class TokenProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(Http401UnauthorizedEntryPoint.class);

    private final String secretKey;
    private final int tokenLifetimeSeconds;

    public TokenProvider(String secretKey, int tokenLifetimeSeconds) {
        this.secretKey = secretKey;
        this.tokenLifetimeSeconds = tokenLifetimeSeconds;
    }

    private static Long getIdFromXAuthToken(String xAuthToken) {
        return getLongFromXAuthToken(xAuthToken, 0);
    }

    /**
     * Get the email from the x-auth cookie content.
     */
    public static String getEmailFromXAuthToken(String xAuthToken) {
        return getFieldFromString(xAuthToken, 1);
    }

    private static String getAuthoritiesStringFromXAuthToken(String xAuthToken) {
        return getFieldFromString(xAuthToken, 2);
    }

    private static Long getExpiresFromXAuthToken(String xAuthToken) {
        return getLongFromXAuthToken(xAuthToken, 3);
    }

    private static String getSignatureFromXAuthToken(String xAuthToken) {
        return getFieldFromString(xAuthToken, 4);
    }

    private static List<SimpleGrantedAuthority> getAuthoritiesFromXAuthToken(String xAuthToken) {
        String authoritiesString = getAuthoritiesStringFromXAuthToken(xAuthToken);

        if (StringUtils.hasText(authoritiesString)) {
            String[] authorityNames = authoritiesString.split(",");
            return Arrays.stream(authorityNames).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private static Long getLongFromXAuthToken(String xAuthToken, int index) {
        String longValue = getFieldFromString(xAuthToken, index);

        if (longValue == null) {
            return null;
        }

        try {
            return Long.parseLong(longValue);
        } catch (NumberFormatException e) {
            LOGGER.info("xAuthToken part #" + index + " is not a long");
            return null;
        }
    }

    private static String getFieldFromString(String xAuthToken, int index) {
        if (!StringUtils.hasText(xAuthToken)) {
            LOGGER.info("xAuthToken does not have any text");
            return null;
        }

        String[] parts = xAuthToken.split(":");

        if (index >= parts.length) {
            LOGGER.info("xAuthToken contains only " + parts.length + " parts, no #" + index);
            return null;
        }

        return parts[index];
    }

    /**
     * Create a new token based on employee details.
     */
    public PreAuthenticatedAuthenticationToken createTokenFromDetails(Long id, String email,
            List<? extends GrantedAuthority> authorities) {
        long expires = System.currentTimeMillis() + 1000L * tokenLifetimeSeconds;
        List<String> authorityNames =
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String authoritiesString = String.join(",", authorityNames);

        String visiblePart = generateTokenContent(id, email, authoritiesString, expires);
        String principal = visiblePart + ":" + generateSignature(visiblePart);

        return new PreAuthenticatedAuthenticationToken(principal, null, authorities);
    }

    private static String generateTokenContent(Long id, String email, String authoritiesString, long expires) {
        return Long.toString(id) + ":" + email + ":" + authoritiesString + ":" + expires;
    }

    /**
     * Create an Authory token from existing cookie content.
     */
    public PreAuthenticatedAuthenticationToken createTokenFromXAuthToken(String xAuthToken) {
        return new PreAuthenticatedAuthenticationToken(xAuthToken, null, getAuthoritiesFromXAuthToken(xAuthToken));
    }

    /**
     * Get the string used to fill the x-auth cookie.
     */
    public String getXAuthToken(Authentication token) {
        return (String) token.getPrincipal();
    }

    /**
     * Check if the token is still valid and correct.
     */
    public Optional<String> validateXAuthToken(String xAuthToken) {
        Long id = getIdFromXAuthToken(xAuthToken);
        String email = getEmailFromXAuthToken(xAuthToken);
        String authoritiesString = getAuthoritiesStringFromXAuthToken(xAuthToken);
        Long expires = getExpiresFromXAuthToken(xAuthToken);
        String signature = getSignatureFromXAuthToken(xAuthToken);

        if (id == null || email == null || authoritiesString == null || expires == null || signature == null) {
            return Optional.of(id + ", " + email + ", " + authoritiesString + ", " + expires + " or " + signature + " is null");
        } else {
            String signatureBasedOnData =
                    generateSignature(generateTokenContent(id, email, authoritiesString, expires));
            long currentTime = System.currentTimeMillis();

            if (expires < currentTime) {
                return Optional.of("Expires " + expires + " is before " + currentTime);
            } else if (!signature.equals(signatureBasedOnData)) {
                return Optional.of("Expected signature " + signatureBasedOnData + " got " + signature);
            } else {
                return Optional.empty();
            }
        }
    }

    public Long getIdFromAuthentication(Authentication authentication) {
        return getIdFromXAuthToken(getXAuthToken(authentication));
    }

    /**
     * Get email from Spring authentication.
     */
    public String getEmailFromAuthentication(Authentication authentication) {
        return getEmailFromXAuthToken(getXAuthToken(authentication));
    }

    /**
     * Get expires from Spring authentication.
     */
    public String getExpiresFromAuthentication(Authentication authentication) {
        String xAuthToken = getXAuthToken(authentication);
        Long expires = getExpiresFromXAuthToken(xAuthToken);

        return expires == null ? null : Long.toString(expires);
    }

    private String generateSignature(String visiblePart) {
        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No SHA-256 algorithm");
        }

        return new String(Hex.encode(digest.digest((visiblePart + ":" + secretKey).getBytes())));
    }
}
