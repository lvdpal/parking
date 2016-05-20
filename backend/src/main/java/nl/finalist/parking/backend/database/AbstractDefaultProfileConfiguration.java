package nl.finalist.parking.backend.database;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * The abstract code for database connectivity which covers the boilerplate configuration for the application in use.
 */
public abstract class AbstractDefaultProfileConfiguration {
    /**
     * @return
     */
    @Bean
    public JpaVendorAdapter getJpaVendorAdapter() {
        return AbstractDatabaseConfiguration.getJpaVendorAdapter(Database.MYSQL);
    }

    /**
     * @return
     */
    @Bean(name = "jpaProperties")
    public Properties getJpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("charSet", "UTF-8");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.dbcp.validationQuery", "SELECT 1");
        properties.setProperty("hibernate.dbcp.testOnBorrow", "true");
        properties.setProperty("hibernate.globally_quoted_identifiers", "true");
        return properties;
    }

    /**
     * @return
     * @throws NamingException
     */
    @Bean
    public DataSource getDataSource() throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        return (DataSource) envContext.lookup(getResourceName());
    }

    protected abstract String getResourceName();
}
