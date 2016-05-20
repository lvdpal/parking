package nl.finalist.parking.backend.database;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Base database configuration to be inherited by any application that has a relational database.
 */
public abstract class AbstractDatabaseConfiguration {
    /**
     * Get the JPA vendor adapter.
     *
     * @param database The type of database.
     * @return
     */
    public static JpaVendorAdapter getJpaVendorAdapter(Database database) {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(database);
        adapter.setGenerateDdl(false);
        adapter.setShowSql(false);
        return adapter;
    }

    protected abstract String[] getPackagesToScan();

    /**
     * Set all properties on the entity manager factory and return it.
     *
     * @param jpaVendorAdapter
     * @param jpaProperties
     * @param dataSource
     * @return
     * @throws NamingException
     */
    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(JpaVendorAdapter jpaVendorAdapter,
            @Qualifier("jpaProperties") Properties jpaProperties, DataSource dataSource) throws NamingException {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPackagesToScan(getPackagesToScan());
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factory.setJpaVendorAdapter(jpaVendorAdapter);
        factory.setJpaProperties(jpaProperties);
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    /**
     * Return the transaction manager
     *
     * @param entityManagerFactory
     * @return
     * @throws NamingException
     */
    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws NamingException {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
