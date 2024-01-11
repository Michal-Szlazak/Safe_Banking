package safe.bank.app.bankservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import safe.bank.app.bankservice.dtos.BankAccountCreateDto;
import safe.bank.app.bankservice.dtos.BankAccountGetDTO;
import safe.bank.app.bankservice.services.BankAccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bank/account")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBankAccount(@RequestBody BankAccountCreateDto bankAccountCreateDto, JwtAuthenticationToken token) {
        String userId = token.getName();
        bankAccountService.createBankAccount(bankAccountCreateDto, UUID.fromString(userId));
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<BankAccountGetDTO> getBankAccounts(JwtAuthenticationToken token) {
        String userId = token.getName();
        return bankAccountService.getBankAccountGetDTOs(UUID.fromString(userId));
    }
}