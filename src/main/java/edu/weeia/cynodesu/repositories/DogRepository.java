package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.domain.Dog;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DogRepository extends JpaRepository<Dog, Long> {
    Dog findByName(String name);

}
