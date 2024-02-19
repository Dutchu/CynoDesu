package edu.weeia.cynodesu.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class SecurityUtils {
    private SecurityUtils() {
    }

    public static Optional<UUID> getCurrentUserId() {
        Optional<CurrentUserToken> user = getCurrentUserDetails();

        return user.map(CurrentUserToken::getUserId);
    }

    public static Optional<CurrentUserToken> getCurrentUserDetails() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();

        return authentication == null || !(authentication.getPrincipal() instanceof DefaultOidcUser) ?
                Optional.empty() :
                Optional.of(mapAuthenticationPrincipalToCurrentUser((DefaultOidcUser) authentication.getPrincipal()));

    }

    public static Optional<OidcIdToken> getCurrentUserJWT() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        return Optional
                .ofNullable(ctx.getAuthentication())
                .filter(auth -> auth.getPrincipal() instanceof DefaultOidcUser)
                .map(auth -> (DefaultOidcUser) auth.getPrincipal())
                .map(DefaultOidcUser::getIdToken);
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static List<GrantedAuthority> mapRolesToGrantedAuthorities(List<String> roles) {
        return roles == null ? Collections.emptyList() : roles.stream()
                .filter(role -> role.startsWith("ROLE_"))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Extracts the authorities from the provided claims map.
     *
     * <p>This method attempts to retrieve the "groups" claim from the provided map, which is expected to be a list of user roles.
     * Each role is then converted into a {@link SimpleGrantedAuthority} object, and all such objects are collected into a list.
     * If the "groups" claim is not present or is not a list, an empty list is returned.</p>
     *
     * @param claims a map of claims, typically obtained from a security token such as a JWT
     * @return a list of {@link GrantedAuthority} objects representing the user's roles, or an empty list if the "groups" claim is not present or is not a list
     */
    public static List<? extends GrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
        return Optional.ofNullable(claims.get("groups"))
                .filter(List.class::isInstance)
                .map(obj -> (List<?>) obj)
                .stream()
                .flatMap(Collection::stream)
                .map(Object::toString)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static CurrentUserToken mapAuthenticationPrincipalToCurrentUser(DefaultOidcUser principal) {
        return new CurrentUserToken(
                principal.getAuthorities(),
                principal.getIdToken(),
                principal.getUserInfo());
    }

}
