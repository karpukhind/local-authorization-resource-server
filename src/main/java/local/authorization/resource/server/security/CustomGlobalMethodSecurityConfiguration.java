package local.authorization.resource.server.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/*@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)*/
/**
 * @EnableGlobalMethodSecurity description : https://www.baeldung.com/spring-security-method-security
 */
public class CustomGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {




}
