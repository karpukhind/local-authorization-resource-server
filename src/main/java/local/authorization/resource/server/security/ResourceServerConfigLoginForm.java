package local.authorization.resource.server.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;


//@Configuration
//@Order(2)
public class ResourceServerConfigLoginForm /*extends ResourceServerConfigurerAdapter*/ {


    //@Override
    public void configure(final HttpSecurity http) throws Exception {

        /*SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/login-successful");
        successHandler.setAlwaysUseDefaultTargetUrl(true);

        SCRCustomUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new SCRCustomUsernamePasswordAuthenticationFilter();
        usernamePasswordAuthenticationFilter.setSecurityContextRepository(securityContextRepository);
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager);
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);*/


        http.addFilterAt(new SecurityContextPersistenceFilter(new HttpSessionSecurityContextRepository()), SecurityContextPersistenceFilter.class)

                .authorizeRequests()
                //.antMatchers("/rest/products/add").hasAnyRole("ADMIN")
                //.antMatchers("/login-successful").hasAnyRole("USER")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login.jsf").permitAll()
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/login-successful", true)
                //.successForwardUrl("/login-successful")
                //.successHandler(successHandler)
                .and()//.addFilterAt(usernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().permitAll();
        //.antMatchers("/rest/products/list").denyAll();
    }
}
