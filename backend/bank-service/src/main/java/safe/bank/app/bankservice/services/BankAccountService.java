package safe.bank.app.bankservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import safe.bank.app.bankservice.CvvGenerator;
import safe.bank.app.bankservice.dtos.BankAccountCreateDto;
import safe.bank.app.bankservice.dtos.BankAccountGetDTO;
import safe.bank.app.bankservice.dtos.GeneratedAccountDTO;
import safe.bank.app.bankservice.entities.BankAccount;
import safe.bank.app.bankservice.entities.BankUser;
import safe.bank.app.bankservice.mappers.BankAccountMapper;
import safe.bank.app.bankservice.repositories.BankAccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final FakerService fakerService;
    private final BankUserService bankUserService;

    private final BankAccountMapper bankAccountMapper;
    private final CvvGenerator cvvGenerator;

    @Transactional
    public void createBankAccount(BankAccountCreateDto bankAccountCreateDto, UUID userId) {

        BankUser bankUser = bankUserService.getUser(userId);
        GeneratedAccountDTO generatedAccountDTO = fakerService.generatedAccountDTO();

        BankAccount plainBankAccount = bankAccountMapper.toEntity(
                bankAccountCreateDto,
                bankUser,
                generatedAccountDTO);

        bankAccountRepository.save(plainBankAccount);
    }

    public List<BankAccountGetDTO> getBankAccountGetDTOs(UUID userId) {

        BankUser bankUser = bankUserService.getUser(userId);
        List<BankAccount> bankAccounts = bankAccountRepository.findAllByBankUser(bankUser);

        return bankAccounts.stream().map(
                bankAccount -> bankAccountMapper.toBankAccountGetDTO(
                        bankAccount,
                        cvvGenerator.generateCVV(
                                bankAccount.getAccountNumber(),
                                bankAccount.getExpiresAt()
                        )
                )
        ).toList();
    }
}
