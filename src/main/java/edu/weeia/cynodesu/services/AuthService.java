package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.LoginDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDto;
import edu.weeia.cynodesu.api.v1.model.UserSingUpResponseDTO;
import edu.weeia.cynodesu.security.AppUserDetails;

public interface AuthService {
    public UserSingUpResponseDTO signUp(UserSignUpDto user);

    public AppUserDetails signIn(LoginDTO loginRequest);
}
