package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.repositories.AppUserRepository;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class UserSignUpValidator implements Validator {

    final AppUserRepository userRepository;

    @Override
    public boolean supports(Class clazz) {
        return UserSignUpDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSignUpDTO toCreate = (UserSignUpDTO) target;

        if (toCreate.pwdPlainText().toLowerCase().contains(toCreate.username().toLowerCase()) || toCreate.username().toLowerCase().contains(toCreate.pwdPlainText().toLowerCase())) {
            errors.rejectValue("pwdPlaintext", "user.weakpwd", "Weak password, choose another");
        }

        if (userRepository.existsByUsername(toCreate.username())) {
            errors.rejectValue("username", "user.alreadyexists", "Username " + toCreate.username() + " already exists");
        }
    }

}