package edu.weeia.cynodesu.configuration;

import jakarta.servlet.Servlet;
import org.h2.server.web.JakartaWebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setPatternParser(new PathPatternParser()); // Use modern path pattern matching
    }

    @Bean
    ServletRegistrationBean<Servlet> h2servletRegistration() {
        ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>(new JakartaWebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }
}
