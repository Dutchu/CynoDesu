package edu.weeia.cynodesu.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.weeia.cynodesu.configuration.Constants;
import edu.weeia.cynodesu.domain.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class AppUserDetails extends User {
    private final Long id;
    private final String firstName;
    private final String email;
    public AppUserDetails(
            Long id, String userName, String password, String firstName, String email,
            Collection<Authority> authorities,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked) {
        super(userName, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.firstName = firstName;
        this.email = email;
    }
    @JsonIgnore
    public boolean isUser() {
        return getGrantedAuthorities().contains(Constants.ROLE_USER);
    }
    @JsonIgnore
    public boolean isSystemAdmin() {
        return getGrantedAuthorities().contains(Constants.ROLE_ADMIN);
    }
    public Collection<String> getGrantedAuthorities() {
        Collection<GrantedAuthority> authorities = getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AppUserDetails that = (AppUserDetails) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
