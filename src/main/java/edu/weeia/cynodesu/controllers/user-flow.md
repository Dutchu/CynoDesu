# App Flows in Spring MVC with Spring Security
## • User flow 1: Registration
### - Enter "/" - landing page.
### - Click Register button from _fragments/header.html displayed on main page (landing.html).
(question no.1 to ChatGPT - is landing good name for a file and if that's going to change how Google's SEO)\
(question no.2 to ChatGPT - why fragments in thymeleaf are with underscore)
### - Registration form pops up with CSS modal functionality and basic validation/error message display coming back from Bean Validation in Java.
### - Filled form with user data (everything necessary to register an AppUser implementing UserDetails from Spring Security basic credentials functionality) gets send to a server.
### - In response comes information about successful operation. There has been no redirection in this flow.

## • User flow 2: Log in
### - User is on landing page or main page or index page.
### - Clicks "Log In" button from _fragments/header.html displayed on main page (landing.html).
### - "Log In" form pops up with CSS modal functionality and basic validation/error message display coming back from Bean Validation in Java.
### - After Successful user validation is redirected to a "/home".
(question no.3 to ChatGPT - Should there be "/home/$"user_name"", or just "/home". What would be use case for a username in url? expression $"user_name" means that here is supposed to be username of validated user)

## • User flow 3: Adding dog to User's collection of dogs.
### - User's "/home" page is accessed after successful user validation and displays User's collection of dogs.
### - Users clicks button placed under a table with it's collection of dogs, "Add Dog".
### - "Create Dog" form pops up with modal functionality with same capabilities as with "Sign Up" to display Bean Validation messages coming from server.
(question no.4 to ChatGPT - should there be a folder with thymeleaf template reflecting dog that would be shared as a view for both user and admin or Admin would be just a User with additional functionality available to Admin hidden with Spring Security and Thymeleaf sec:isRoleAdmin or something along those lines)
### - Successful creation of dog should refresh table of dogs with new entry. 

## Note that Dog is a Pageable resource, meaning "Page<Dog> findWithAllByStatus(Pageable pageable, DogStatus status);"

# Admin is a superset of user. Admin folder with views should only expand on user's views with admin capabilities.
## • Admin flow 1: Dog's Status change "Accepted/Rejected".
## • Admin flow 2: Creation of Competitions.
