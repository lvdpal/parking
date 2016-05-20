package nl.finalist.parking.backend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

public class Bootstrap implements WebApplicationInitializer {
    public static final String API_SERVLET_NAME = "api";
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        final AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setServletContext(servletContext);

        applicationContext.register(SpringConfiguration.class);
        applicationContext.register(DatabaseConfiguration.class);
        applicationContext.register(SecurityConfiguration.class);
        applicationContext.register(JsonConfiguration.class);

        servletContext.addListener(new ContextLoaderListener(applicationContext));
        servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class)
                .addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, API_SERVLET_NAME);

        ServletRegistration.Dynamic registration =
                servletContext.addServlet(API_SERVLET_NAME, new DispatcherServlet(applicationContext));
        registration.setLoadOnStartup(1);
        registration.addMapping("/*");

        LOGGER.debug("Finished onStartup");
    }
}
