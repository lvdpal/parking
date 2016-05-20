package nl.finalist.parking.backend.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    // LP TODO IKWRS-333: do we need these to be Strings? Or can we just use an enum?
    public static final String ADMIN = "ROLE_ADMIN";

    public static final String EMPLOYEE = "ROLE_EMPLOYEE";

    public static final String BUDGETKEEPER = "ROLE_BUDGETKEEPER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
