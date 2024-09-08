package edu.weeia.cynodesu.configuration;

import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.services.AuditingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {

    @Bean
    public AuditorAware<AppUser> auditorAware() {
        return new AuditingService();
    }
}
