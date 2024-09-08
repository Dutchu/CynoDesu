package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.domain.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
}
