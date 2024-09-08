package edu.weeia.cynodesu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.weeia.cynodesu.configuration.Constants;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "APP_USER")
public class AppUser extends BaseAuditingEntity implements UserDetails {

//    @Basic(fetch = jakarta.persistence.FetchType.LAZY)
    @Lob
    @JsonIgnore
    byte[] avatar;

    @Size(max = 30)
    private String firstName;

    @Size(max = 30)
    private String lastName;

    @Column(length = 254, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String username; //doesn't need to be unique (multi-tenacy)

    @Column(name = "password_hash", length = 60)
    private String password;

    @JoinTable(
            name = "APP_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = Constants.BATCH_SIZE)
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<BreedingFacility> breedingFacilities = new HashSet<>();

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
