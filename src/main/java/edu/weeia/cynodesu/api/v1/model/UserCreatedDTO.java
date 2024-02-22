package edu.weeia.cynodesu.api.v1.model;

import edu.weeia.cynodesu.domain.Authority;

import java.util.Set;

public record UserCreatedDTO(Long Id, String username, String email, Set<Authority> role) {
}
