package edu.weeia.cynodesu.security;

import edu.weeia.cynodesu.configuration.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;

import java.util.stream.Collectors;

import java.util.List;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/static/**",
            "/error/**",
            "/swagger-ui/**",
            "/swagger-ui.html/**",
            "/signup/**",
            "/" //landing page is allowed for all
    };

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(ah -> ah
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers("/admin/**").hasAuthority(Constants.ROLE_ADMIN)
                        .requestMatchers("/user/**").hasAuthority(Constants.ROLE_USER)
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/api/**").authenticated()//individual api will be secured differently
                        .anyRequest().authenticated()) //this one will catch the rest patterns
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(withDefaults())
                .oauth2ResourceServer(o2 -> o2.jwt(withDefaults()))
                .oauth2Client(withDefaults());

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}") String issuerUri) {
        return JwtDecoders.fromOidcIssuerLocation(issuerUri);
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> authorities.stream().filter(a -> a instanceof OidcUserAuthority)
                .map(a -> (OidcUserAuthority) a)
                .map(a -> SecurityUtils.extractAuthorityFromClaims(a.getUserInfo().getClaims()))
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }
}
