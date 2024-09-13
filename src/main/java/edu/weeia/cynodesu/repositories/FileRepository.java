package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.domain.ReceivedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<ReceivedFile, UUID> {
}
