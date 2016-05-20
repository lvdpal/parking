package nl.finalist.parking.backend.configuration;

import nl.finalist.parking.backend.database.AbstractDatabaseConfiguration;
import nl.finalist.parking.backend.database.AbstractDefaultProfileConfiguration;
import nl.finalist.parking.backend.database.BaseTestProfileConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * Configuration for the database layer
 */
@Configuration
@ComponentScan("nl.finalist.parking.backend.repository")
@EnableJpaRepositories("nl.finalist.parking.backend.repository")
@EnableTransactionManagement
public class DatabaseConfiguration extends AbstractDatabaseConfiguration {
    protected String[] getPackagesToScan() {
        return new String[] { "nl.finalist.parking.backend.entity" };
    }

    /**
     * Default configuration
     */
    @Configuration
    @Profile("default")
    public static class DefaultConfiguration extends AbstractDefaultProfileConfiguration {
        protected String getResourceName() {
            return "jdbc/services";
        }
    }

    /**
     * Test configuration
     */
    @Configuration
    @Profile("test")
    public static class TestConfiguration extends BaseTestProfileConfiguration {
    }

    /**
     * AAT configuration
     */
    @Configuration
    @Profile("aat")
    public static class AATestConfiguration extends AbstractDefaultProfileConfiguration {
        protected String getResourceName() {
            return "jdbc/parking";
        }

        /**
         * @return
         */
        @Bean(name = "jpaProperties")
        public Properties getJpaProperties() {
            Properties properties = new Properties();
            properties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
            properties.setProperty("hibernate.connection.pool_size", "10");
            properties.setProperty("hibernate.current_session_context_class", "thread");
            properties.setProperty("hibernate.globally_quoted_identifiers", "false");
            properties.setProperty("hibernate.dbcp.validationQuery", "select 1 from INFORMATION_SCHEMA.SYSTEM_USERS");
            properties.setProperty("hibernate.dbcp.testOnBorrow", "true");
            return properties;
        }
    }
}
