package nl.finalist.parking.backend.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Common JSON encoding information for use between different applications.
 */
@Configuration
public class JsonConfiguration {
    /**
     * Create and return an ObjectMapper for converting JSON to POJOs and vice versa.
     *
     * @return
     */
    @Bean
    public ObjectMapper getObjectMapper() throws ClassNotFoundException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new ISO8601DateFormat());
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return mapper;
    }
}
