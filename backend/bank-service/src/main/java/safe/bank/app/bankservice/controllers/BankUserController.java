package safe.bank.app.bankservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import safe.bank.app.bankservice.dtos.PostBankUserDTO;
import safe.bank.app.bankservice.services.BankUserService;

@RequestMapping("/api/bank/user")
@RestController
@RequiredArgsConstructor
public class BankUserController {

    private final BankUserService bankUserService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBankUser(@Valid @RequestBody PostBankUserDTO postBankUserDTO) {
        bankUserService.createBankUser(postBankUserDTO);
    }
}
