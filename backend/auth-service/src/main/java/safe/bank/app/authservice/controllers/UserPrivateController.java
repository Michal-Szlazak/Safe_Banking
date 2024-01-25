package safe.bank.app.authservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import safe.bank.app.authservice.dtos.PasswordChangeDTO;
import safe.bank.app.authservice.services.KeycloakService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/user/private")
public class UserPrivateController {

    private final KeycloakService keycloakService;

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public void logout(JwtAuthenticationToken token) {
        keycloakService.logout(token.getName());
    }

    @PostMapping("/changePassword")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(JwtAuthenticationToken token,
                               @RequestBody @Valid PasswordChangeDTO passwordChangeDTO) {
        String userId = token.getName();
        keycloakService.setNewPassword(passwordChangeDTO, UUID.fromString(userId));
    }
}
