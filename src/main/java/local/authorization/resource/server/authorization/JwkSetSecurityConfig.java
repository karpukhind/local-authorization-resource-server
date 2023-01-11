package local.authorization.resource.server.authorization;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

//@Configuration
public class JwkSetSecurityConfig /*extends WebSecurityConfigurerAdapter*/ {
    //@Override
    public void configure(final HttpSecurity http) throws Exception {

        http.requestMatcher(new AntPathRequestMatcher("/oauth/**"))
                .authorizeRequests()
                .antMatchers("/jwks.json").permitAll();
                //.anyRequest().authenticated();

    }
}
