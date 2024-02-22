package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.domain.Authority;
import edu.weeia.cynodesu.repositories.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public void save(Authority auth) {
        authorityRepository.save(auth);
    }

    public Set<Authority> findByNameIn(String... roles) {
        return authorityRepository.findByNameIn(List.of(roles));
    }
}
