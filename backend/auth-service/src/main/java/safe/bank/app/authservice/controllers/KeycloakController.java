package safe.bank.app.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import safe.bank.app.authservice.controller_advice.exceptions.UserCreationException;
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
public class KeycloakController {

    private final Logger logger = LoggerFactory.getLogger(KeycloakController.class);

    private final KeycloakService restService;

    @GetMapping("/roles")
    public List<String> getRoles(@RequestHeader("UserId") String userId) {
        return restService.getRoles(userId);
    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody UserLoginDTO userLoginDTO) {
        return restService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public void logout(@RequestParam(value = "refresh_token", name = "refresh_token") String userId) {
        restService.logout(userId);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserPostDTO userPostDTO) throws UserCreationException {
        restService.register(userPostDTO);
    }
}
