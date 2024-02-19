package edu.weeia.cynodesu.domain;

import edu.weeia.cynodesu.configuration.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "APP_USER")
public class AppUser extends BaseEntity implements UserDetails {
    @Getter
    @Setter
    @Basic(fetch = jakarta.persistence.FetchType.LAZY)
    @Lob
    byte[] avatar;
    @Getter
    @Setter
    @Size(max = 30)
    private String firstName;
    @Getter
    @Setter
    @Size(max = 30)
    private String lastName;
    @Getter
    @Setter
    @Column(length = 254, unique = true, nullable = false)
    private String email;
    @Getter
    @Setter
    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 20)
    private String uniqueId;
    @Setter
    @Column(name = "password_hash", length = 60)
    private String password;
    @Setter
    @JoinTable(
            name = "APP_USER_AUTHORITY",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = Constants.BATCH_SIZE)
    private Set<Authority> authorities = new HashSet<>();
    @Column(nullable = false)
    private boolean active = false;
    @Column(nullable = false)
    private boolean accountNonExpired = true;
    @Column(nullable = false)
    private boolean accountNonLocked = true;
    @Column(nullable = false)
    private boolean credentialsNonExpired = true;
    @Getter
    @Setter
    private String activationKey;
    @Getter
    @Setter
    private String resetKey;
    @Override
    public Collection<Authority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return uniqueId;
    }
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    @Override
    public boolean isEnabled() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }
    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    public AppUser() {

    }
    public AppUser(String uniqueId, String firstName, String lastName, String email) {
        this.uniqueId = uniqueId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.accountNonExpired = Boolean.TRUE;
        this.accountNonLocked = Boolean.TRUE;
        this.credentialsNonExpired = Boolean.TRUE;
        this.active = Boolean.TRUE;
    }
    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", authorities=" + authorities +
                ", active=" + active +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                '}';
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
        AppUser appUser = (AppUser) o;
        return Objects.equals(uniqueId, appUser.uniqueId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uniqueId);
    }
}
