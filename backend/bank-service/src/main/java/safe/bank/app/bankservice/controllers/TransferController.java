package safe.bank.app.bankservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import safe.bank.app.bankservice.dtos.TransferGetDTO;
import safe.bank.app.bankservice.dtos.TransferCreateDTO;
import safe.bank.app.bankservice.services.TransferService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bank/transfer")
public class TransferController {

    private final TransferService transferService;
    @PostMapping
    public void sendTransaction(@RequestBody @Valid TransferCreateDTO transactionCreateDTO,
                                JwtAuthenticationToken token) {
        String userId = token.getName();
        transferService.sendTransfer(transactionCreateDTO, UUID.fromString(userId));
    }

    @GetMapping
    public List<TransferGetDTO> getTransfers(JwtAuthenticationToken token) {
        String userId = token.getName();
        return transferService.getTransfers(UUID.fromString(userId));
    }
}
