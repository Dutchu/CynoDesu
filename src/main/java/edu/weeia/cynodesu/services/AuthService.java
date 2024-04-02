package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.LoginDTO;
import edu.weeia.cynodesu.api.v1.model.LoginResponseDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDTO;
import edu.weeia.cynodesu.api.v1.model.UserSingUpResponseDTO;
import edu.weeia.cynodesu.domain.AppUser;
import edu.weeia.cynodesu.security.AppUserDetails;
import org.springframework.security.core.Authentication;

public interface AuthService {
    public UserSingUpResponseDTO signUp(UserSignUpDTO user);

    public AppUserDetails signIn(LoginDTO loginRequest);
}
