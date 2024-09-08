package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.domain.BreedingFacility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BreedingFacilityRepository extends JpaRepository<BreedingFacility, Long> {

    @Query("SELECT bf FROM BreedingFacility bf JOIN bf.users u WHERE u.id = :userId")
    List<BreedingFacility> findBreedingFacilitiesByUserId(@Param("userId") Long userId);

    @Query("SELECT f FROM BreedingFacility f JOIN f.users u WHERE u.id = :userId")
    Page<BreedingFacilityPreview> findByUserId(@Param("userId") Long userId, Pageable pageable);

}
