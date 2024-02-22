package edu.weeia.cynodesu.repositories;

import edu.weeia.cynodesu.domain.AppUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    @EntityGraph(attributePaths = {"authorities"})
    Optional<AppUser> findOneWithAuthoritiesByUsername(String username);

    Optional<AppUser> findOneByUsername(String username);

    Optional<AppUser> findByIdAndActiveIsTrue(Long id);

    Page<AppUser> findAllByActiveIsTrue(Pageable pageable);

    Iterable<AppUser> findByFirstName(String personName);

    Iterable<AppUser> findByFirstNameStartingWith(String emailSuffix);

    Iterable<AppUser> findByEmailEndingWith(String emailSuffix);

    Iterable<AppUser> findByFirstNameOrEmail(String first, String email);

    Iterable<AppUser> findByFirstNameLikeOrEmail(String firstLike, String email);

    Iterable<AppUser> findByFirstNameNotLike(String firstName);

    Optional<AppUser> findOneWithAuthoritiesByEmail(String email);

    @Transactional(readOnly = true)
    boolean existsByUsername(String username);
}
