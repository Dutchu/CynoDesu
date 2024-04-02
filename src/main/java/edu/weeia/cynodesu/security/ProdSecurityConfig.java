package edu.weeia.cynodesu.security;

import edu.weeia.cynodesu.configuration.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Profile("prod")
public class ProdSecurityConfig {
    private final ProdCustomSuccessHandler prodSuccessHandler;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/v2/api-docs",
            "/h2-console/**",
            "/webjars/**",
            "/static/**",
            "/", //landing page is allowed for all
            "/landing",
            "/favicon.ico",
            "/dog/**",
            "/index"
    };

    public ProdSecurityConfig(ProdCustomSuccessHandler prodSuccessHandler) {
        this.prodSuccessHandler = prodSuccessHandler;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
//                .addFilterBefore(new LoggingFilter(), ChannelProcessingFilter.class)
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(ah -> ah
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers("/landing").permitAll()
                        .requestMatchers("/admin/**").hasAuthority(Constants.ROLE_ADMIN)
                        .requestMatchers("/user/**").hasAuthority(Constants.ROLE_USER)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/**").authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login")
                        .successHandler(prodSuccessHandler)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // the URL on which a POST will trigger logout
                        .logoutSuccessUrl("/") // the URL to redirect to after logout
                        .invalidateHttpSession(true) // specifies whether to invalidate the HttpSession
                        .deleteCookies("JSESSIONID")) // deletes the specified cookies
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
