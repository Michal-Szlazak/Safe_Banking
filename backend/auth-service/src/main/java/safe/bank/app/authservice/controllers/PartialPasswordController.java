package safe.bank.app.authservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import safe.bank.app.authservice.dtos.PartialPasswordCreateDTO;
import safe.bank.app.authservice.dtos.PartialPasswordGetDTO;
import safe.bank.app.authservice.dtos.PartialPasswordPostDTO;
import safe.bank.app.authservice.services.PartialPasswordService;

import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class PartialPasswordController {

    private final PartialPasswordService partialPasswordService;

    @GetMapping("/auth/private/partial-password")
    @ResponseStatus(HttpStatus.OK)
    public PartialPasswordGetDTO getPartialPassword(JwtAuthenticationToken token) {
        String userId = token.getName();
        return partialPasswordService.getPartialPassword(UUID.fromString(userId));
    }

    @PostMapping("/auth/public/partial-password")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPartialPasswordSet(PartialPasswordCreateDTO partialPasswordCreateDTO) {

        partialPasswordService.createPartialPasswordSet(partialPasswordCreateDTO.password(),
                partialPasswordCreateDTO.userId());
    }

    @PostMapping("/auth/private/partial-password/check")
    @ResponseStatus(HttpStatus.OK)
    public void checkPartialPassword(@RequestBody PartialPasswordPostDTO partialPasswordPostDTO,
                                        JwtAuthenticationToken token) {
        String userId = token.getName();
        partialPasswordService.checkPartialPassword(partialPasswordPostDTO,
                UUID.fromString(userId));
    }
}
