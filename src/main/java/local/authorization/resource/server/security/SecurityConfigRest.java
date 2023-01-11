package local.authorization.resource.server.security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.env.Environment;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@Order(2)
public class SecurityConfigRest extends WebSecurityConfigurerAdapter {

   /* @Value( "${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}" )
    private String jwkSetUri;*/

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment env;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(false).inMemoryAuthentication().passwordEncoder(passwordEncoder)
                .withUser("user1").password(passwordEncoder.encode("user1Pass")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder.encode("user2Pass")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("adminPass")).roles("ADMIN");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    /**
     * En example how to define RequestMatcher as a bean and than create a request matcher that negates the first that
     * is as a bean + RequestHeaderRequestMatcher
     * https://stackoverflow.com/questions/37059128/spring-boot-1-3-3-enableresourceserver-and-enableoauth2sso-at-the-same-time
     */
    protected void configure(HttpSecurity http) throws Exception {
        //JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).restOperations(restTemplate(new RestTemplateBuilder())).build();
        JwtDecoder jwtDecoder = defaultPublicRsaFromJwtKeyValue();

        http.requestMatcher(new AntPathRequestMatcher("/rest/**")).authorizeRequests()
                .regexMatchers(HttpMethod.POST,"/rest/products/add/?").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/rest/products/store/**").hasRole("ADMIN")
                .regexMatchers(HttpMethod.POST,"/rest/store/add/?").hasRole("ADMIN")
                .regexMatchers(HttpMethod.POST,"/rest/store/\\d/brands/?").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .oauth2ResourceServer().jwt()
                .decoder(jwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
    }
    private static JwtAuthenticationConverter jwtAuthenticationConverter()
    {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    private RestTemplate restTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException, KeyManagementException {

        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSslcontext(sslContext)
                .setHostnameVerifier(new X509HostnameVerifier() {
                    @Override
                    public void verify(String hostname, SSLSocket sslSocket) throws IOException {}

                    @Override
                    public void verify(String hostname, X509Certificate x509Certificate) throws SSLException {}

                    @Override
                    public void verify(String hostname, String[] strings, String[] strings1) throws SSLException {}

                    @Override
                    public boolean verify(String hostname, SSLSession sslSession) {
                        return "localhost".equals(hostname);
                    }
                })
                .build();
        HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
        customRequestFactory.setHttpClient(httpClient);
        return builder.requestFactory(() -> customRequestFactory).build();
    }

    /**
     * Parse Public RSA from string:
     * https://gist.github.com/destan/b708d11bd4f403506d6d5bb5fe6a82c5
     *
     * This method didn't work as it requests security.oauth2.resource.jwt.key-uri endpoint to get default public RSA key
     * from the authorization server but it needs to do it at the server startup when that endpoint is still not available
     * Therefore NimbusJwtDecoder can request spring.security.oauth2.resourceserver.jwt.jwk-set-uri  only as it is mentioned in
     * https://medium.com/swlh/stateless-jwt-authentication-with-spring-boot-a-better-approach-1f5dbae6c30f
     *
     * Therefore definition of  security.oauth2.resource.jwt.key-uri endpoint doesn't make sense.
     * security.oauth2.resource.jwt.key-value  should be defined instead. Default public RSA key value should be copied into there
     * from security.oauth2.resource.jwt.key-uri
     *
     * @return
     */
    private JwtDecoder defaultPublicRsaFromTokenKey() throws Exception {

        String tokenKeyUrl = env.getProperty("security.oauth2.resource.jwt.key-uri");
        ResponseEntity<String> response = restTemplate(new RestTemplateBuilder()).exchange(RequestEntity.get(tokenKeyUrl).build(), String.class);
        JsonObject jsonObject = new JsonParser().parse(response.getBody()).getAsJsonObject();
        String publicKeyContent = jsonObject.get("value").getAsString().replaceAll("\\n", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpecX509);

        return NimbusJwtDecoder.withPublicKey(pubKey).build();

    }
    private JwtDecoder defaultPublicRsaFromJwtKeyValue() throws Exception {

        String publicKeyContent = env.getProperty("security.oauth2.resource.jwt.key-value")
                .replaceAll("\\n", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "");

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpecX509);

        return NimbusJwtDecoder.withPublicKey(pubKey).build();

    }

}
