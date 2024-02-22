package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.api.v1.model.DogPreviewDTO;
import edu.weeia.cynodesu.domain.Dog;
import edu.weeia.cynodesu.domain.DogStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
    Dog findByName(String name);

    @EntityGraph(attributePaths = {"createdByUser"})
//    , "comments", "attachedFiles"})
    Page<Dog> findWithAllByStatus(Pageable pageable, DogStatus status);

    @EntityGraph(attributePaths = {"createdByUser"})
//    , "attachedFiles"})
    Page<Dog> findWithUserAndAttachedFilesByStatus(DogStatus status, Pageable pageable);
}
