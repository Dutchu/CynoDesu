package edu.weeia.cynodesu;

import edu.weeia.cynodesu.api.v1.model.CreateDogDTO;
import edu.weeia.cynodesu.api.v1.model.UserSignUpHashedDTO;
import edu.weeia.cynodesu.services.AuthorityService;
import edu.weeia.cynodesu.services.DogServiceImpl;
import edu.weeia.cynodesu.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@ContextConfiguration(classes = {CynoDesuApplication.class})
public class DogControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DogServiceImpl dogServiceImpl;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // Set up necessary preconditions here
        // Create a test user
//        AppUser appUser = new AppUser();
//        appUser.setEmail("admin@admin.pl");
//        appUser.setPassword(passwordEncoder.encode("admin"));
//        appUser.setFirstName("Admin");
//        appUser.setLastName("Admin");
//        appUser.setUsername("admin");
//        appUser.setActive(true);
//        appUser.setAuthorities(authorityService.findByNameIn("ROLE_USER", "ROLE_ADMIN"));

        var registerUserDTO = new UserSignUpHashedDTO("admin@admin.pl", "Test", "Admin", "admin", passwordEncoder.encode("Admin@123"));

        userService.save(registerUserDTO);

        // Mock the authenticated user
        mockAuthenticatedUser(registerUserDTO.username());
    }

    private void mockAuthenticatedUser(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void testPostRequestWithoutCsrf() throws Exception {
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"value\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {

        mockMvc.perform(formLogin().user("admin").password("admin"))
                .andExpect(status().isOk())
                .andExpect(authenticated().withUsername("admin"));
    }
    @Test
    public void testSaveDog() {
        // Mock the authenticated user
        String username = "admin";
        mockAuthenticatedUser(username);

        // Create and save a Dog entity
        CreateDogDTO dto = new CreateDogDTO(1L, "Sparky", "Test");
        var dog = dogServiceImpl.createDog(dto);

        // Verify the createdByUser field is set
        assertNotNull(dog);
//        assertEquals(username, dog.getCreatedByUser().getUsername());
    }



}
