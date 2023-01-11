package local.authorization.resource.server.security;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

//@Configuration
//@Order(1)
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig /*extends WebSecurityConfigurerAdapter*/ {

    @Value( "${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}" )
    private String jwkSetUri;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //@Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.eraseCredentials(false).inMemoryAuthentication().passwordEncoder(passwordEncoder)
                .withUser("user1").password(passwordEncoder.encode("user1Pass")).roles("USER")
                .and()
                .withUser("user2").password(passwordEncoder.encode("user2Pass")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("adminPass")).roles("ADMIN");
    }

    /*@Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/view");
    }*/

    //@Override
    public void configure(final HttpSecurity http) throws Exception {

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).restOperations(restTemplate(new RestTemplateBuilder())).build();

        http//.addFilterAt(new SecurityContextPersistenceFilter(new HttpSessionSecurityContextRepository()), SecurityContextPersistenceFilter.class)
                //.requestMatcher(new AntPathRequestMatcher("/rest/**"))

        //.csrf().disable()
                .requestMatcher(new AntPathRequestMatcher("/rest/**"))
                .authorizeRequests()
                //.mvcMatchers("/oauth/jwks.json").permitAll()
                .mvcMatchers("/products/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                //.antMatchers( "/products/list").hasAnyRole("ADMIN", "USER").and()
                //.antMatchers("/login-successful").hasAnyRole("USER")
                //.anyRequest().authenticated().and()
                //.oauth2ResourceServer(oauth2 -> oauth2.jwt());
                .oauth2ResourceServer().jwt().decoder(jwtDecoder);

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

    /*@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }*/


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


}