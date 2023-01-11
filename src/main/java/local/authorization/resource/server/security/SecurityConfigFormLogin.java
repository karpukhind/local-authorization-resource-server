package local.authorization.resource.server.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Order(1)
@EnableWebSecurity
public class SecurityConfigFormLogin extends WebSecurityConfigurerAdapter {

    /**
     * Relevant {@link HttpSecurity} api info:
     * https://stackoverflow.com/questions/68082468/spring-security-change-login-default-path
     * http.getSharedObject(SharedObject.class)
     * http.apply(Configurer)
     *
     * Order of ant matchers :
     * https://stackoverflow.com/questions/30819337/multiple-antmatchers-in-spring-security/30819556
     *
     * @param http
     * @throws Exception
     */
    protected void configure(HttpSecurity http) throws Exception {
        http .requestMatcher(new AntPathRequestMatcher("/view/**"))
                .authorizeRequests()//.antMatchers("/login1").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/view/login").permitAll()
                .defaultSuccessUrl("/view/login-successful", true)
        .and().logout()
                .logoutUrl("/view/perform_logout")
        .logoutSuccessUrl("/view/login");

    }
}
