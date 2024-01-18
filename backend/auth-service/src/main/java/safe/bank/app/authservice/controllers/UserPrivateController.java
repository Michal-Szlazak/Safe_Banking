package safe.bank.app.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import safe.bank.app.authservice.services.KeycloakService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/user/private")
public class UserPrivateController {

    private final KeycloakService keycloakService;

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public void logout(JwtAuthenticationToken token) {
        keycloakService.logout(token.getName());
    }
}
