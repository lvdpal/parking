package nl.finalist.parking.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfiguration {
    @Bean
    public TokenProvider tokenProvider() {
        // String secret = propertyResolver.getProperty("secret", String.class, "mySecretXAuthSecret");
        // int validityInSeconds = propertyResolver.getProperty("tokenValidityInSeconds", Integer.class, 3600);
        return new TokenProvider("myXAuthSecret", 3600);
    }
}
