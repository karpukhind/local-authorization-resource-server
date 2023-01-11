package local.authorization.resource.server.security;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;

import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

import javax.servlet.ServletContext;

@RewriteConfiguration
public class HttpConfiguration extends HttpConfigurationProvider {

    @Override
    public Configuration getConfiguration(final ServletContext context)
    {
        return ConfigurationBuilder.begin()

                // A basic join
                .addRule(Join.path("/view/login").to("/login.jsf"))
                .addRule(Join.path("/view/login-successful").to("/login-successful.jsf"));
                //.addRule(Join.path("/layout").to("/layout.jsf"));
                //.addRule(Join.path("/product-list").to("/product-list.jsf"));
    }

    @Override
    public int priority() {
        return 10;
    }
}
