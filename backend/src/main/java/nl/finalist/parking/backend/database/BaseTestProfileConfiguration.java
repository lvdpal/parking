package nl.finalist.parking.backend.database;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * The abstract code for database connectivity which covers the boilerplate configuration for the application in test.
 */
public class BaseTestProfileConfiguration {
    protected BaseTestProfileConfiguration() {
    }

    /**
     * @return
     */
    @Bean
    public JpaVendorAdapter getJpaVendorAdapter() {
        return AbstractDatabaseConfiguration.getJpaVendorAdapter(Database.HSQL);
    }

    /**
     * @return
     * @throws NamingException
     */
    @Bean
    public DataSource getDataSource() throws NamingException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
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
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.globally_quoted_identifiers", "false");
        return properties;
    }
}
