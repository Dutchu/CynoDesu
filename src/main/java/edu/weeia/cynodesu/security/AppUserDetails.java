package edu.weeia.cynodesu.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.weeia.cynodesu.domain.AppUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import edu.weeia.cynodesu.configuration.Constants;
import edu.weeia.cynodesu.domain.Authority;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class AppUserDetails extends User {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private final AppUser appUser;

//    public AppUserDetails(Long id, String userName, String email, String password, String firstName, String lastName, Collection<Authority> authorities,
//                          boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, AppUser appUser) {
public AppUserDetails(AppUser appUser) {
        super(appUser.getUsername(), appUser.getPassword(), appUser.isEnabled(), appUser.isAccountNonExpired(), appUser.isCredentialsNonExpired(), appUser.isAccountNonLocked(), appUser.getAuthorities());

    this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.appUser = appUser;
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