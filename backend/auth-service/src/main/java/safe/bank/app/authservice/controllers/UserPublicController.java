package safe.bank.app.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import safe.bank.app.authservice.controller_advice.exceptions.UserCreationException;
import safe.bank.app.authservice.dtos.PasswordChangeDTO;
import safe.bank.app.authservice.dtos.ResetPasswordDTO;
import safe.bank.app.authservice.dtos.UserLoginDTO;
import safe.bank.app.authservice.dtos.UserPostDTO;
import safe.bank.app.authservice.services.KeycloakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/user/public")
public class UserPublicController {

    private final KeycloakService keycloakService;

    @GetMapping("/roles")
    public List<String> getRoles(@RequestHeader("UserId") String userId) {
        return keycloakService.getRoles(userId);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return keycloakService.login(userLoginDTO.getEmail(), userLoginDTO.getPassword());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserPostDTO userPostDTO) throws UserCreationException {
        keycloakService.register(userPostDTO);
    }

    @PostMapping(value = "/refreshToken")
    public String refreshJwtToken(@RequestHeader(name = "refresh_token") String refreshToken) {
        return keycloakService.refreshJwtToken(refreshToken);
    }

    @PostMapping("/forgotPassword")
    @ResponseStatus(HttpStatus.OK)
    public void forgotPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        keycloakService.forgotPasswordEmail(resetPasswordDTO.getEmail());
    }
}
