package safe.bank.app.bankservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import safe.bank.app.bankservice.dtos.PostBankUserDTO;
import safe.bank.app.bankservice.entities.BankUser;
import safe.bank.app.bankservice.mappers.BankUserMapper;
import safe.bank.app.bankservice.repositories.BankUserRepository;

@Service
@RequiredArgsConstructor
public class BankUserService {

    private final BankUserRepository bankUserRepository;
    private final BankUserMapper bankUserMapper;

    public void createBankUser(PostBankUserDTO userDTO) {
        BankUser bankUser = bankUserMapper.toEntity(userDTO);
        bankUserRepository.save(bankUser);
    }
}
