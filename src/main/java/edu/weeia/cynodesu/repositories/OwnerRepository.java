package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Owner getByLastName(String lastName);
}
