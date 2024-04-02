package edu.weeia.cynodesu.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ProfileConfig {
    public ProfileConfig(Environment environment) {
        String activeProfile = environment.getProperty("SPRING_PROFILES_ACTIVE");
        if (activeProfile != null) {
            System.setProperty("spring.profiles.active", activeProfile);
        }
    }
}
