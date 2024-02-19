package edu.weeia.cynodesu.repositories;


import edu.weeia.cynodesu.domain.AppUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    @Transactional(readOnly = true)
    @Cacheable("userExistsByUsername")
    boolean existsByUsername(String username);

    @Cacheable("userExistsByEmail")
    boolean existsByEmail(String email);
}
