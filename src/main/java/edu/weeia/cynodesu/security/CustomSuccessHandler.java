package edu.weeia.cynodesu.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    //TODO: Figure out if both methods are necessary
    //TODO: Class not used anymore - previous setup. Could be useful as I struggled to figure this out but I came to conclusion that integrating user's "/home" view into "/" view is stupid:

    /***
     * public class DevSecurityConfig {
     *     CustomSuccessHandler successHandler;
     *     ...
     *                 .formLogin(formLogin -> formLogin
     *                         .loginPage("/auth/login")
     *                         .successHandler(successHandler)
     *                         .permitAll())
     */
    //    @Override
    //    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
    //        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    //
    //        request.getRequestDispatcher("/auth/success").forward(request, response);
    //    }
    //    @Override
    //    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    //        request.getRequestDispatcher("/auth/success").forward(request, response);
    //    }

//    @Bean
//    public AuthenticationSuccessHandler loginSuccessHandler() {
//        return new SimpleUrlAuthenticationSuccessHandler("/") {
//            @Override
//            protected void handle(HttpServletRequest request, HttpServletResponse response,
//                                  Authentication authentication) throws IOException, ServletException {
//                response.sendRedirect("/");
//            }
//        };
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.sendRedirect("/home");
    }
}
