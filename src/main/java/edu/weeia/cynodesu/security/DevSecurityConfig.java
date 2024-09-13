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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@Profile({"dev", "default"})
public class DevSecurityConfig {

    CustomSuccessHandler successHandler;


    DevSecurityConfig(CustomSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/v2/api-docs",
            "/h2-console/**",
            "/webjars/**",
            "/static/**",
            "/images/**",          // <-- Add this line to whitelist images
            "/css/**",             // <-- Add this line to whitelist CSS files if needed
            "/js/**",              // <-- Add this line to whitelist JS files if needed
            "/", //landing page is allowed for all
            "/landing",
            "/signup",
            "/favicon.ico",
            "/dog/**",
            "/error/**",
            "/index"
    };

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
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/home").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER)
                        .requestMatchers("/competition/**").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER)
                        .requestMatchers("/sse/**").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER)
                        .requestMatchers("/dog/**").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER)
                        .requestMatchers("/facilities/**").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER)
                        .requestMatchers("/facility/**").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER)
                        .requestMatchers("/admin/**").hasAuthority(Constants.ROLE_ADMIN)
                        .requestMatchers("/user/**").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_USER))
                .formLogin(formLogin -> formLogin
                        .loginPage("/")
                        .loginProcessingUrl("/auth/login")
                        .successHandler(successHandler)
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler("/?error=true"))
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

    //TODO: IMPORTANT TO UNDERSTAND HOW THIS WORK
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/error/403");
        return accessDeniedHandler;
    }
}