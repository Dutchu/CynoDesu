package edu.weeia.cynodesu.configuration;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class LoggingFilter extends CommonsRequestLoggingFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void beforeRequest(@NonNull HttpServletRequest request, @NonNull String message) {
        logger.info("Before request [{}]", message);
    }

    @Override
    protected void afterRequest(@NonNull HttpServletRequest request, @NonNull String message) {
        logger.info("After request [{}]", message);
    }

    @Override
    protected boolean shouldLog(@NonNull HttpServletRequest request) {
        return logger.isInfoEnabled();
    }
}