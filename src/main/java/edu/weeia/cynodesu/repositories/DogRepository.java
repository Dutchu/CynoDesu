package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.api.v1.model.DogDetailDto;
import edu.weeia.cynodesu.domain.Dog;
import edu.weeia.cynodesu.domain.DogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
    Dog findByName(String name);

    @EntityGraph(attributePaths = {"createdByUser"})
//    , "comments", "attachedFiles"})
    Page<Dog> findWithAllByStatus(Pageable pageable, DogStatus status);

    @EntityGraph(attributePaths = {"createdByUser"})
//    , "attachedFiles"})
    Page<Dog> findWithUserAndAttachedFilesByStatus(DogStatus status, Pageable pageable);

    @Query("SELECT d FROM Dog d WHERE d.status = :status AND d.breedingFacility.id IN " +
            "(SELECT bf.id FROM BreedingFacility bf WHERE bf.id = :breedingFacilityId)")
    Page<Dog> findAcceptedDogsByFacilityId(@Param("breedingFacilityId") Long breedingFacilityId, Pageable pageable, @Param("status") DogStatus status);

    @Query("SELECT d FROM Dog d WHERE d.status = :status")
    Page<Dog> findAllByDogStatus(Pageable pageable, @Param("status") DogStatus status);

}
