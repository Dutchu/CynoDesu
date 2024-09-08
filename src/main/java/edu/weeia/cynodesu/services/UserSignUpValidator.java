package edu.weeia.cynodesu.services;

import edu.weeia.cynodesu.api.v1.model.UserSignUpForm;
import edu.weeia.cynodesu.repositories.AppUserRepository;

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
        return UserSignUpForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSignUpForm toCreate = (UserSignUpForm) target;

        if (toCreate.getPwdPlainText().toLowerCase().contains(toCreate.getUsername().toLowerCase()) || toCreate.getUsername().toLowerCase().contains(toCreate.getPwdPlainText().toLowerCase())) {
            errors.rejectValue("pwdPlainText", "user.weakpwd", "Weak password, choose another");
        }

        if (userRepository.existsByUsername(toCreate.getUsername())) {
            errors.rejectValue("username", "user.alreadyexists", "Username " + toCreate.getUsername() + " already exists");
        }

        if (userRepository.existsByEmail(toCreate.getEmail())) {
            errors.rejectValue("email", "email.alreadyexists", "Email " + toCreate.getEmail() + " already exists");
        }
    }

}