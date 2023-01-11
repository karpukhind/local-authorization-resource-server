package local.authorization.resource.server.security;

//import local.authorization.resource.server.filters.SCRCustomUsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*@Configuration
@Order(1)
@EnableResourceServer*/
public class ResourceServerConfigRest /*extends ResourceServerConfigurerAdapter*/ {

    @Autowired
    private SecurityContextRepository securityContextRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    //@Override
    public void configure(final HttpSecurity http) throws Exception {

        /*SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/login-successful");
        successHandler.setAlwaysUseDefaultTargetUrl(true);

        SCRCustomUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new SCRCustomUsernamePasswordAuthenticationFilter();
        usernamePasswordAuthenticationFilter.setSecurityContextRepository(securityContextRepository);
        usernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager);
        usernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);*/


        http//.addFilterAt(new SecurityContextPersistenceFilter(new HttpSessionSecurityContextRepository()), SecurityContextPersistenceFilter.class)
                .requestMatcher(new AntPathRequestMatcher("/rest/**"))
                .authorizeRequests()
                .antMatchers("/rest/products/add").hasRole("ADMIN")
                .antMatchers("/rest/store/add").hasRole("ADMIN")

                .anyRequest().authenticated();
                //.antMatchers("/rest/products/add").hasAnyRole("ADMIN");
                //.antMatchers("/login-successful").hasAnyRole("USER")


                /*.and().formLogin()
                .loginPage("/login.jsf").permitAll()
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/login-successful", true)
                //.successForwardUrl("/login-successful")
                //.successHandler(successHandler)
                .and()//.addFilterAt(usernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().permitAll();*/
                //.antMatchers("/rest/products/list").denyAll();
    }

}


/*
* Related links:
* https://stackoverflow.com/questions/36411947/spring-security-getauthentication-returns-null
*
* AuthenticationManagerBuilder.eraseCredentials(false) :
* https://question-it.com/questions/1056484/securitycontextholdergetcontext-getauthentication-getcredentials-vozvraschaet-null-posle-autentifikatsii
* */


/*
* Save Authentication authResult  to SecurityContextHolder.getContext().getAuthentication()
* org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter.successfulAuthentication
* */