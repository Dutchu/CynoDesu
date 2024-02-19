package edu.weeia.cynodesu.services;


import edu.weeia.cynodesu.repositories.UserRepository;
import edu.weeia.cynodesu.api.v1.model.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class UserSignUpValidator implements Validator {

    final UserRepository userRepository;

    @Override
    public boolean supports(Class clazz) {
        return UserSignUpDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSignUpDto toCreate = (UserSignUpDto) target;


        if (StringUtils.containsIgnoreCase(
                toCreate.getPwdPlainText(), toCreate.getUsername(), Locale.ENGLISH) ||
                StringUtils.containsIgnoreCase(
                        toCreate.getPwdPlainText(), toCreate.getUsername(), Locale.ENGLISH)) {
            errors.rejectValue("pwdPlainText", "user.weakPwd", "Weak password, choose a stronger one!");
        }

        if (userRepository.existsByUsername(toCreate.getUsername())) {
            errors.rejectValue("username", "user.alreadyExists", toCreate.getUsername() + ": username already exists!");
        }

        if(userRepository.existsByEmail(toCreate.getEmail())) {
            errors.rejectValue("email", "user.alreadyExists", toCreate.getEmail() + ": email already exists!");
        }
    }
}
