package nl.finalist.parking.backend.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Spring configuration.
 */
@Configuration
@EnableScheduling
@EnableWebMvc
@ComponentScan({ "nl.finalist.parking.backend.resource",
        "nl.finalist.parking.backend.service" })
@PropertySource("classpath:/backend.properties")
public class SpringConfiguration extends WebMvcConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringConfiguration.class);

    @Inject
    private ObjectMapper objectMapper;

    /**
     * Required to make @Value work in combination with a property-file.
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * This adds the custom ObjectMapper bean to the list of converters.
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        converters.add(0, converter);
    }

    /**
     * Return the application version, which is retrieved from the services pom file in the given context.
     *
     * @param context
     * @return
     */
    @Bean(name = "applicationVersion")
    @Autowired
    public String applicationVersion(ServletContext context) {
        final Properties properties = new Properties();

        try {
            final InputStream stream =
                    context.getResourceAsStream("META-INF/maven/nl.finalist.parking/backend/pom.properties");

            if (stream != null) {
                properties.load(stream);
            }
        } catch (IOException e) {
            LOGGER.error("Unable to read version.", e);
        }

        return properties.getProperty("version", ".");
    }
}
