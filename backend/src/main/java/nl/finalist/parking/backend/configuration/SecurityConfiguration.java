package nl.finalist.parking.backend.configuration;

import nl.finalist.parking.backend.security.Http401UnauthorizedEntryPoint;
import nl.finalist.parking.backend.security.PreAuthenticatedAuthenticationProvider;
import nl.finalist.parking.backend.security.XAuthTokenConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import javax.inject.Inject;

/**
 * Contains spring configuration for spring-security.
 */
@Configuration
@ComponentScan({ "nl.finalist.parking.backend.security" })
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private XAuthTokenConfigurer xAuthTokenConfigurer;

    @Bean
    public PreAuthenticatedAuthenticationProvider authenticationProvider() {
        return new PreAuthenticatedAuthenticationProvider();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    // TODO: fix security for rest calls
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and().csrf().disable().headers()
                .frameOptions().disable().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/api/googlelogin").permitAll()
                .antMatchers("/api/googleloginlink").permitAll()
                .antMatchers("/api/spots/currentStatus").permitAll()
                .antMatchers("/api/spots/free").permitAll()
                .antMatchers("/api/reservation/*").permitAll()
                .antMatchers("/api/**").authenticated().and().apply(xAuthTokenConfigurer);
        // @formatter:on
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
