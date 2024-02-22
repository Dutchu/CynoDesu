package edu.weeia.cynodesu.domain;

import edu.weeia.cynodesu.configuration.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
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

@Entity
@Data
public class AppUser extends BaseEntity implements UserDetails {

    @Basic(fetch = jakarta.persistence.FetchType.LAZY)
    @Lob
    byte[] avatar;

    @Size(max = 30)
    private String firstName;

    @Size(max = 30)
    private String lastName;

    @Column(length = 254, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String username; //doesn't need to be unique (multi-tenacy)

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 20)
    private String uniqueId;

    @Column(name = "password_hash", length = 60)
    private String password;

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

    private String activationKey;

    private String resetKey;

    @Override
    public boolean isEnabled() {
        return active;
    }
}
