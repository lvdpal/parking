package nl.finalist.parking.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import nl.finalist.parking.backend.service.GoogleUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * A helper class for Google's OAuth2 authentication API.
 *
 * @author Matyas Danter with iPROFS additions
 */
@Component
public class GoogleAuthHelper {
    private static final List<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email");
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Please provide a value for the clientId constant before proceeding, set this up at
     * https://code.google.com/apis/console/
     */
    @Value("${googleAuthClientId}")
    private String clientId;

    /**
     * Please provide a value for the clientSecret constant before proceeding, set this up at
     * https://code.google.com/apis/console/
     */
    @Value("${googleAuthClientSecret}")
    private String clientSecret;

    /**
     * Callback URI that google will redirect to after successful authentication
     */
    @Value("${googleAuthCallbackUrl}")
    private String callbackUri;

    @Value("${googleUserInfoUrl}")
    private String userInfoUrl;

    @Inject
    private ObjectMapper objectMapper;

    private GoogleAuthorizationCodeFlow flow = null;
    private String stateToken = null;

    /**
     * Initializes properties that are dependent on injected values.
     */
    @PostConstruct
    public void initFlowAndStateToken() {
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientId, clientSecret, SCOPE)
                .build();
        stateToken = generateStateToken();
    }

    /**
     * Builds a login URL based on client ID, secret, callback URI, and scope
     */
    public String buildLoginUrl() {
        final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();

        return url.setRedirectUri(callbackUri).setState(stateToken).build();
    }

    /**
     * Expects an Authentication Code, and makes an authenticated request for the user's profile information
     *
     * @param authCode authentication code provided by google
     * @return JSON formatted user profile information
     */
    public String getUserEmail(final String authCode) {
        try {
            final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(callbackUri).execute();

            final Credential credential = flow.createAndStoreCredential(response, "117578298996192841492");
            final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);

            // Make an authenticated httpRequest
            final GenericUrl url = new GenericUrl(userInfoUrl);
            final HttpRequest httpRequest = requestFactory.buildGetRequest(url);
            httpRequest.getHeaders().setContentType("application/json");

            String jsonResponse = httpRequest.execute().parseAsString();
            GoogleUserInfo googleUserInfo = objectMapper.readValue(jsonResponse, GoogleUserInfo.class);

            return googleUserInfo.getEmail();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateStateToken() {
        SecureRandom sr1 = new SecureRandom();

        return "google;" + sr1.nextInt();
    }
}
