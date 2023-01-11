package local.authorization.resource.server.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import local.authorization.resource.server.converter.JwtCustomHeadersAccessTokenConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value( "${spring.security.oauth2.authenticationserver.jwt.jwk-set-key-alias}" )
    private String rsaAlias;
    @Value( "${spring.security.oauth2.authenticationserver.jwt.jwk-set-key-storepass}" )
    private String keyStorePassword;
    @Value( "${spring.security.oauth2.authenticationserver.jwt.jwk-set-key-id}" )
    private String keyId;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

   /* @Autowired
    private UserDetailsService userDetailsService;*/

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter());//.userDetailsService(userDetailsService);
    }


    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("first-client")
                .secret(passwordEncoder.encode("noonewilleverguess"))
                //.secret("noonewilleverguess")
                //.scopes("read", "write")
                .authorizedGrantTypes( "authorization_code", "refresh_token", "client_credentials", "password");
                //.redirectUris("http://localhost:8081/oauth/login/client-app");
    }

    // https://www.programmersought.com/article/49825807868/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                //.tokenKeyAccess("permitAll()") //oauth/token_key is public
                .checkTokenAccess("permitAll()") //oauth/check
                // _token public
                .allowFormAuthenticationForClients() //Form authentication (application token)
        ;
    }



   /* @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(getKeyPair());

        //converter.setSigningKey("bael");
        return converter;
    }    */

    @Bean
    /**
     * Definition of these two methods are related to the deprecated Spring security 4 authorization server and turns on
     * this authorization server in the JWT mode.
     * Along with these two methods   spring.security.oauth2.resourceserver.jwt.jwk-set-uri   property has to be defined
     * so that to indicate authorization server metadata to the resource server and therefore JwtDecoder bean will be added
     * to the application context with the definition of that property in order to decode RSA public key from authorization server metadata.
     * Otherwise application fails to start ( https://github.com/okta/samples-java-spring/issues/48 )
     */
    public JwtAccessTokenConverter accessTokenConverter() {
        return new JwtCustomHeadersAccessTokenConverter(
                Collections.singletonMap("kid", keyId),
                getKeyPair());
    }
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }


    @Bean
    /**
     * Authorization server metadata bean to be called from authorization server metadata endpoint that delivers public
     * RSA keys to other resources ( like Spring security 5 resource server ) in order to check the signature of JWT/JWS token
     * that is supplied with rest request.
     * This authorization server metadata is indicated to the resource server by a predefined property
     * spring.security.oauth2.resourceserver.jwt.jwk-set-uri
     * ( https://www.baeldung.com/spring-security-oauth2-jws-jwk  |||  3.2. Configuring the Resource Server )
     *
     */
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) getKeyPair().getPublic())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(keyId);
        return new JWKSet(builder.build());
    }

    private KeyPair getKeyPair() {
        ClassPathResource ksFile = new ClassPathResource("authentication-server-rsa.jks");
        KeyStoreKeyFactory ksFactory = new KeyStoreKeyFactory(ksFile, keyStorePassword.toCharArray());
        KeyPair keyPair = ksFactory.getKeyPair(rsaAlias);
        return keyPair;
    }

}
