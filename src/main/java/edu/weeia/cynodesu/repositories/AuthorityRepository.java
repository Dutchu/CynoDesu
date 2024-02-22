package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    @Transactional(readOnly = true)
    Set<Authority> findByNameIn(Collection<String> name);
}
