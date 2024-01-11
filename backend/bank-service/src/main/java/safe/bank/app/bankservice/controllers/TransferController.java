package safe.bank.app.bankservice.controllers;

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
@RequestMapping("/bank/transfer")
public class TransferController {

    private final TransferService transferService;
    @PostMapping
    public void sendTransaction(@RequestBody TransferCreateDTO transactionCreateDTO,
                                JwtAuthenticationToken token) {
        String userId = token.getName();
        transferService.sendTransaction(transactionCreateDTO, UUID.fromString(userId));
    }

    @GetMapping
    public List<TransferGetDTO> getTransfers(JwtAuthenticationToken token) {
        String userId = token.getName();
        return transferService.getTransfers(UUID.fromString(userId));
    }
}
