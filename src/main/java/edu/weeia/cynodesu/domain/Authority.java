package edu.weeia.cynodesu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Setter
public class Authority implements GrantedAuthority {
    @Id
    @Column(length = 16)
    private String name;
    @Override
    public String getAuthority() {
        return name;
    }
    public Authority() {
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Authority authority1 = (Authority) o;

        return name.equals(authority1.name);
    }
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    @Override
    public String toString() {
        return """
            Authority{\
            authority='\
            """ + name + '\'' +
                '}';
    }
}
