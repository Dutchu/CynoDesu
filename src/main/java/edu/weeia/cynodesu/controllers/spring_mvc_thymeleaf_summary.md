
# Spring MVC Application with Thymeleaf and Spring Security

## Overview

This guide covers the structure and flow of a Spring MVC application using Thymeleaf for views and Spring Security for handling authentication and authorization. The application includes user registration, login, and functionality for managing a collection of dogs. Administrators have additional privileges to approve dogs and manage competitions.

## Table of Contents
- [File Structure](#file-structure)
- [Landing Page and Registration Flow](#landing-page-and-registration-flow)
- [Login Flow](#login-flow)
- [Adding a Dog](#adding-a-dog)
- [Admin Functionality](#admin-functionality)
- [Summary](#summary)

## File Structure

```plaintext
src/main/resources/templates/
├── layout/
│   ├── _header.html
│   ├── _footer.html
│   ├── base.html
├── auth/
│   ├── login.html
│   ├── register.html
├── user/
│   ├── home.html
│   ├── add-dog-modal.html
├── admin/
│   ├── approve-dogs.html
│   ├── manage-competitions.html
│   ├── competition-details.html
```

## Landing Page and Registration Flow

### File Naming and SEO

- **Landing Page Naming**: Use `index.html` or `home.html` for better SEO.
- **Thymeleaf Fragment Naming**: Use underscores (`_header.html`) to indicate that these are reusable fragments.

### Registration Controller

```java
@Controller
public class AuthController {

    @GetMapping("/")
    public String showLandingPage() {
        return "landing";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("appUser") AppUser appUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        model.addAttribute("success", true);
        return "auth/register";
    }
}
```

### Landing Page (landing.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Landing Page</title>
    <link rel="stylesheet" th:href="@{/css/styles.css}" />
</head>
<body>
    <div th:replace="layout/_header :: header"></div>
    <div>
        <h1>Welcome to DogApp!</h1>
        <button th:onclick="'showRegisterModal()'">Register</button>
        <button th:onclick="'showLoginModal()'">Log In</button>
    </div>
    <div th:replace="auth/register :: registerModal"></div>
    <div th:replace="auth/login :: loginModal"></div>
    <div th:replace="layout/_footer :: footer"></div>
    <script th:src="@{/js/modal.js}"></script>
</body>
</html>
```

### Registration Form (auth/register.html)

```html
<div th:fragment="registerModal">
    <div id="registerModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeRegisterModal()">&times;</span>
            <h2>Register</h2>
            <form th:action="@{/register}" th:object="${appUser}" method="post">
                <div th:if="${success}" class="success">Registration successful!</div>
                <div>
                    <label for="username">Username:</label>
                    <input type="text" th:field="*{username}" />
                    <div th:if="${#fields.hasErrors('username')}" th:errors="*{username}"></div>
                </div>
                <div>
                    <label for="password">Password:</label>
                    <input type="password" th:field="*{password}" />
                    <div th:if="${#fields.hasErrors('password')}" th:errors="*{password}"></div>
                </div>
                <button type="submit">Sign Up</button>
            </form>
        </div>
    </div>
</div>
```

## Login Flow

### Login Controller

```java
@GetMapping("/login")
public String showLoginForm() {
    return "auth/login";
}
```

### Login Form (auth/login.html)

```html
<div th:fragment="loginModal">
    <div id="loginModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeLoginModal()">&times;</span>
            <h2>Log In</h2>
            <form th:action="@{/login}" method="post">
                <div>
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username"/>
                </div>
                <div>
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password"/>
                </div>
                <button type="submit">Sign In</button>
            </form>
        </div>
    </div>
</div>
```

### Redirection to Home

- **`/home` vs `/home/$username`**: Generally, `/home` is sufficient. Use `/home/$username` for SEO or personalized URLs if needed.

## Adding a Dog

### User Home Controller

```java
@GetMapping("/user/home")
public String showUserHomePage(Model model, Pageable pageable) {
    AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Page<Dog> dogs = dogService.findAllByUser(currentUser, pageable);
    model.addAttribute("dogs", dogs);
    return "user/home";
}

@PostMapping("/user/add-dog")
public String addDogToUserCollection(@Valid @ModelAttribute("dog") Dog dog, BindingResult result, Model model, Pageable pageable) {
    if (result.hasErrors()) {
        return "user/add-dog-modal";
    }
    dogService.save(dog);
    return "redirect:/user/home";
}
```

### User Home View (user/home.html)

```html
<div th:fragment="userHome">
    <h2>Your Dogs</h2>
    <table>
        <tr>
            <th>Name</th>
            <th>Breed</th>
        </tr>
        <tr th:each="dog : ${dogs.content}">
            <td th:text="${dog.name}">Dog Name</td>
            <td th:text="${dog.breed}">Breed</td>
        </tr>
    </table>
    <button th:onclick="'showAddDogModal()'">Add Dog</button>
    <div th:replace="user/add-dog-modal :: addDogModal"></div>
</div>
```

### Add Dog Modal (user/add-dog-modal.html)

```html
<div th:fragment="addDogModal">
    <div id="addDogModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeAddDogModal()">&times;</span>
            <h2>Add a New Dog</h2>
            <form th:action="@{/user/add-dog}" th:object="${dog}" method="post">
                <div>
                    <label for="dogName">Dog Name:</label>
                    <input type="text" th:field="*{name}" />
                    <div th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></div>
                </div>
                <div>
                    <label for="breed">Breed:</label>
                    <input type="text" th:field="*{breed}" />
                    <div th:if="${#fields.hasErrors('breed')}" th:errors="*{breed}"></div>
                </div>
                <button type="submit">Add Dog</button>
            </form>
        </div>
    </div>
</div>
```

## Admin Functionality

- **Shared Views for User and Admin**: Use `sec:authorize` to show/hide admin-specific features in shared views.

### Example with `sec:authorize`

```html
<table>
    <tr>
        <th>Name</th>
        <th>Breed</th>
        <th sec:authorize="hasRole('ADMIN')">Actions</th>
    </tr>
    <tr th:each="dog : ${dogs.content}">
        <td th:text="${dog.name}">Dog Name</td>
        <td th:text="${dog.breed}">Breed</td>
        <td sec:authorize="hasRole('ADMIN')">
            <button th:onclick="'approveDog('+${dog.id}+');'">Approve</button>
        </td>
    </tr>
</table>
```

## Summary

- **SEO**: Consider using traditional names like `index.html` for landing pages.
- **Thymeleaf Fragments**: Use underscores as a convention to differentiate reusable components.
- **URLs**: `/home` is generally preferred, though `/home/$username` has specific use cases like SEO or personalized routing.
- **Shared Views**: Use `sec:authorize` for role-based view customization.

This setup helps manage users, dogs, and admin functionalities within a clean, maintainable structure.
