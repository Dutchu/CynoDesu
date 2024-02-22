package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.LoginDTO;
import edu.weeia.cynodesu.api.v1.model.LoginResponseDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDTO;
import edu.weeia.cynodesu.api.v1.model.UserSingUpResponseDTO;

public interface AuthService {
    public UserSingUpResponseDTO signUp(UserSignUpDTO user);

    public LoginResponseDTO signIn(LoginDTO loginRequest);
}
