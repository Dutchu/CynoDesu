package edu.weeia.cynodesu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class BreedingFacility extends BaseAuditingEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long breedingFacilityId;
    @Size(min = 3, max = 30)
    private String name;
    private String phoneNumber;
    @Email(message = "Invalid email format")
    private String email;
    private String address;
    private String registrationNumber;
    private String licenseInfo;
    private String notes;
    @Lob
    @JsonIgnore
    private byte[] icon;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "user_facility",
            joinColumns = @JoinColumn(name = "breeding_facility_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<AppUser> users = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "breedingFacility")
    private Set<Dog> dogs = new HashSet<>();

}
