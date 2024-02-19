package edu.weeia.cynodesu.controllers;

import edu.weeia.cynodesu.configuration.AppProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AppProperties appProperties;
    private final ClientRegistration clientRegistration;

    public AuthController(ClientRegistrationRepository registrations, AppProperties appProperties) {
        this.appProperties = appProperties;
        this.clientRegistration = registrations.findByRegistrationId("oidc");
    }

    //FIXME: use keycloak rest api to handle these
//    @GetMapping("/change-password")
//    public String changePassword() {
//        return "redirect:" + appProperties.getKeycloak().getAccountUrl();
//    }
//
//    @GetMapping("/settings")
//    public String settings(RedirectAttributes redirectAttributes, HttpServletRequest req, HttpServletResponse resp) {
//        return getKeyCloakAccountUrl(redirectAttributes, req, resp);
//    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, @AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken) {
        var logoutUrlSb = new StringBuilder();
        logoutUrlSb.append(this.clientRegistration.getProviderDetails().getConfigurationMetadata().get("end_session_endpoint").toString());
        String originUrl = request.getHeader(HttpHeaders.ORIGIN);
        if (!StringUtils.hasText(originUrl)) {
            originUrl = appProperties.getWeb().getBaseUrl() + "?logout=true";
        }
        logoutUrlSb.append("?id_token_hint=").append(idToken.getTokenValue()).append("&post_logout_redirect_uri=").append(originUrl);

        request.getSession().invalidate();
        return "redirect:" + logoutUrlSb;
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/oauth2/authorization/oidc"; //this will redirect to login page
    }


}
