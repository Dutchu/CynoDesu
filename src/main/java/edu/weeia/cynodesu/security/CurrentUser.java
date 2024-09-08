package edu.weeia.cynodesu.security;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal(errorOnInvalidType = true, expression = "T(edu.weeia.cynodesu.security.SecurityUtils).mapAuthenticationPrincipalToCurrentUser(#this)")
public @interface CurrentUser {
}