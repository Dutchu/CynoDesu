package edu.weeia.cynodesu.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Dog extends BaseAuditingEntity implements Serializable {

    @NotEmpty
    private String name;

    @Lob
    @JsonIgnore
    private byte[] icon;

    @ManyToOne
    @JoinColumn(name = "breed_id")
    private Breed breed;

    private LocalDate dob;

    private Sex sex;

    private String registrationNo;

//    @NotEmpty
//    /***
//     * @Type(type="org.hibernate.type.BinaryType") isn't working in Hibernate 6>
//     * but if you are using for converting in byte[] type, you can just delete annotation @Lob and use @JdbcTypeCode(Types.BINARY).
//     */
//    //@Lob
//    // postgres saves as 'text', must use select lo_get(content::oid) from article; to retrieve
    @Lob
    @Column(columnDefinition = "text")
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DogStatus status = DogStatus.AWAIT;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<ReceivedFile> attachedFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breeding_facility_id")
    private BreedingFacility breedingFacility;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(
            mappedBy = "dog",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<DogCompetitionScore> scores = new HashSet<>();

}
