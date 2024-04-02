package edu.weeia.cynodesu.api.v1.model;

import java.security.Principal;

public record LoginResponseDTO (String accessToken, Principal principal) {}
