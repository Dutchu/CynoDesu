package edu.weeia.cynodesu.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Dog extends BaseAuditingEntity implements Serializable {

    @NotEmpty
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @NotEmpty
    @Lob //postgres saves as 'text', must use select lo_get(content::oid) from article; to retrieve
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DogStatus status = DogStatus.AWAIT;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<ReceivedFile> attachedFiles = new ArrayList<>();
}
