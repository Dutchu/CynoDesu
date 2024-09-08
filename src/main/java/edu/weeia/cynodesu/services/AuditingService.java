package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.security.AppUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContext;

import java.util.Optional;

@Component
public class AuditingService implements AuditorAware<AppUser> {
    @Override
    public Optional<AppUser> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(principal -> principal instanceof AppUserDetails ? ((AppUserDetails) principal).getAppUser() : null);
    }
}
