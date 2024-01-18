package safe.bank.app.bankservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import safe.bank.app.bankservice.dtos.BankAccountCreateDto;
import safe.bank.app.bankservice.dtos.BankAccountGetDTO;
import safe.bank.app.bankservice.dtos.GeneratedAccountDTO;
import safe.bank.app.bankservice.entities.BankAccount;
import safe.bank.app.bankservice.entities.BankUser;
import safe.bank.app.bankservice.mappers.BankAccountMapper;
import safe.bank.app.bankservice.repositories.BankAccountRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final FakerService fakerService;
    private final BankUserService bankUserService;
    private final EncryptionService encryptionService;

    private final BankAccountMapper bankAccountMapper;

    @Transactional
    public void createBankAccount(BankAccountCreateDto bankAccountCreateDto, UUID userId) {

        BankUser bankUser = bankUserService.getUser(userId);
        GeneratedAccountDTO generatedAccountDTO = fakerService.generatedAccountDTO();

        BankAccount plainBankAccount = bankAccountMapper.toEntity(
                bankAccountCreateDto,
                bankUser,
                generatedAccountDTO);
        BankAccount encryptedBankAccount = encryptionService.encryptBankAccount(plainBankAccount);
        bankAccountRepository.save(encryptedBankAccount);
    }

    public List<BankAccountGetDTO> getBankAccountGetDTOs(UUID userId) {

        BankUser bankUser = bankUserService.getUser(userId);
        List<BankAccount> encryptedBankAccounts = bankAccountRepository.findAllByBankUser(bankUser);
        List<BankAccount> decryptedBankAccounts = encryptedBankAccounts.stream()
                .map(encryptionService::decryptBankAccount).toList();
        return decryptedBankAccounts.stream().map(bankAccountMapper::toBankAccountGetDTO).toList();
    }

    public List<BankAccount> getBankAccounts(UUID userId) {
        BankUser encryptedBankUser = bankUserService.getUser(userId);
        BankUser decryptedBankUser = encryptionService.decryptBankUser(encryptedBankUser);
        List<BankAccount> bankAccounts = bankAccountRepository.findAllByBankUser(decryptedBankUser);
        encryptionService.encryptBankUser(decryptedBankUser);
        return bankAccounts;
    }

    public BankAccount getBankAccountByAccountNumber(String accountNumber) {

        return bankAccountRepository.findBankAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    public void saveBankAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }
}
