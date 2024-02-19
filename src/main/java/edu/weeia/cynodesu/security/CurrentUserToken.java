package edu.weeia.cynodesu.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.weeia.cynodesu.configuration.Constants;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.io.Serial;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class CurrentUserToken extends DefaultOidcUser {
    @Serial
    private static final long serialVersionUID = 332175240400944267L;
    private final Collection<? extends GrantedAuthority> authorities;
    private final UUID userId;
    private final String username;
    private final String email;

    /***
     * Production constructor
     */
    public CurrentUserToken(
            Collection<? extends GrantedAuthority> authorities,
            OidcIdToken idToken,
            OidcUserInfo userInfo) {
        super(authorities, idToken, userInfo);
        this.authorities = authorities;
        this.userId = UUID.fromString(getName());
        this.username = userInfo.getSubject();
        this.email = userInfo.getEmail();
    }

    /***
     * Testing constructor
     */
    public CurrentUserToken(
            Collection<? extends GrantedAuthority> authorities,
            OidcIdToken userId,
            OidcUserInfo userInfo,
            String username) {
        //TODO: Testing values
        //Corrected the super call to include the correct parameters
//        super(authorities, userId, userInfo);
        super(authorities, OidcIdToken.withTokenValue("DUMMY-TEST").claim("DUMMY", "DUMMY").claim("sub", username).build());
        this.authorities = SecurityUtils.extractAuthorityFromClaims(userInfo.getClaims());
        this.userId = UUID.fromString(getName());
        this.username = getAttribute("preferred_username");
        this.email = getAttribute("email");
    }

    /***
     * Simple testing constructor
     */
    //TODO: Testing values
    public CurrentUserToken(Collection<GrantedAuthority> authorities, String username) {
        super(authorities, OidcIdToken.withTokenValue("DUMMY-TEST").claim("DUMMY", "DUMMY").claim("sub", username).build());
        this.authorities = authorities;
        this.userId = null;
        this.username = username;
        this.email = null;
    }

    @JsonIgnore
    public boolean isUser() {
        return getGrantedAuthorities().contains(Constants.ROLE_USER);
    }

    @JsonIgnore
    public boolean isAdmin() {
        return getGrantedAuthorities().contains(Constants.ROLE_ADMIN);
    }

    @JsonIgnore
    public Collection<String> getGrantedAuthorities() {
        Collection<? extends GrantedAuthority> authorities = getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UserToken getUserToken() {
        return new UserToken(getUsername(), getIdToken().getTokenValue(), getGrantedAuthorities());
    }

    public record UserToken(String username, String token, Collection<String> roles) {
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
        CurrentUserToken that = (CurrentUserToken) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId);
    }
}
