package safe.bank.app.bankservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import safe.bank.app.bankservice.dtos.PostBankUserDTO;
import safe.bank.app.bankservice.entities.BankUser;
import safe.bank.app.bankservice.mappers.BankUserMapper;
import safe.bank.app.bankservice.repositories.BankUserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankUserService {

    private final BankUserRepository bankUserRepository;
    private final BankUserMapper bankUserMapper;

    public void createBankUser(PostBankUserDTO userDTO) {
        BankUser bankUser = bankUserMapper.toEntity(userDTO);
        bankUserRepository.save(bankUser);
    }

    public BankUser getUser(UUID userId) {
        return bankUserRepository.findById(userId).orElseThrow(
                () ->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User for id " + userId + " not found."));
    }
}
