package safe.bank.app.bankservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import safe.bank.app.bankservice.dtos.TransferCreateDTO;
import safe.bank.app.bankservice.dtos.TransferGetDTO;
import safe.bank.app.bankservice.entities.BankAccount;
import safe.bank.app.bankservice.entities.BankUser;
import safe.bank.app.bankservice.entities.Transfer;
import safe.bank.app.bankservice.mappers.TransferMapper;
import safe.bank.app.bankservice.repositories.TransferRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final BankAccountService bankAccountService;
    private final TransferMapper transferMapper;
    private final EncryptionService encryptionService;

    @Transactional
    public void sendTransfer(TransferCreateDTO transferCreateDTO, UUID userId) {

        verifyUserAccessToAccount(userId, transferCreateDTO);
        verifyGivenReceiverNameWithReceiverAccount(transferCreateDTO);

        BankAccount encryptedReceiverAccount = bankAccountService.getBankAccountByAccountNumber(
                transferCreateDTO.getReceiverAccount());
        BankAccount decryptedReceiverAccount = encryptionService.decryptBankAccount(
                encryptedReceiverAccount);

        BankAccount encryptedSenderAccount = bankAccountService.getBankAccountByAccountNumber(
                transferCreateDTO.getSenderAccount());
        BankAccount decryptedSenderAccount = encryptionService.decryptBankAccount(encryptedSenderAccount);

        BigDecimal senderAccountBalance = new BigDecimal(decryptedSenderAccount.getBalance());
        BigDecimal receiverAccountBalance = new BigDecimal(decryptedReceiverAccount.getBalance());

        if (senderAccountBalance.compareTo(transferCreateDTO.getAmount()) >= 0) {

            decryptedSenderAccount.setBalance(senderAccountBalance
                    .subtract(transferCreateDTO.getAmount()).toString());
            decryptedReceiverAccount.setBalance(receiverAccountBalance
                    .add(transferCreateDTO.getAmount()).toString());

            BankAccount finalEncryptedSenderAccount = encryptionService.encryptBankAccount(decryptedSenderAccount);
            BankAccount finalEncryptedReceiverAccount = encryptionService.encryptBankAccount(decryptedReceiverAccount);

            Transfer plainTransfer = transferMapper.toEntity(transferCreateDTO,
                    finalEncryptedSenderAccount, finalEncryptedReceiverAccount);
            Transfer encryptedTransfer = encryptionService.encryptTransfer(plainTransfer);

            bankAccountService.saveBankAccount(finalEncryptedSenderAccount);
            bankAccountService.saveBankAccount(finalEncryptedReceiverAccount);
            transferRepository.save(encryptedTransfer);

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Insufficient funds.");
        }

    }

    private void verifyUserAccessToAccount(UUID userId, TransferCreateDTO transferCreateDTO) {

        List<BankAccount> encryptedSenderAccounts = bankAccountService.getBankAccounts(userId);
        List<BankAccount> decryptedSenderAccounts = encryptedSenderAccounts.stream()
                .map(encryptionService::decryptBankAccount).toList();
        List<String> accountNumbers = decryptedSenderAccounts.stream().map(BankAccount::getAccountNumber).toList();
        if(!accountNumbers.contains(transferCreateDTO.getSenderAccount())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have access to this account number.");
        }
        encryptedSenderAccounts.forEach(encryptionService::encryptBankAccount);
    }

    private void verifyGivenReceiverNameWithReceiverAccount(TransferCreateDTO transferCreateDTO) {

        BankAccount encryptedReceiverAccount = bankAccountService.getBankAccountByAccountNumber(
                transferCreateDTO.getReceiverAccount());
        BankAccount decryptedReceiverAccount = encryptionService.decryptBankAccount(
                encryptedReceiverAccount);

        BankUser encryptedReceiverBankUser = decryptedReceiverAccount.getBankUser();
        BankUser decryptedReceiverBankUser = encryptionService.decryptBankUser(encryptedReceiverBankUser);
        String receiverFirstName = decryptedReceiverBankUser.getFirstName();
        String receiverLastName = decryptedReceiverBankUser.getLastName();
        String receiverFullName = receiverFirstName + " " + receiverLastName;

        if(!receiverFullName.equalsIgnoreCase(transferCreateDTO.getReceiverName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Receivers name doesnt match the account number");
        }

        encryptionService.encryptBankUser(decryptedReceiverBankUser);
        encryptionService.encryptBankAccount(decryptedReceiverAccount);
    }
    public List<TransferGetDTO> getTransfers(UUID uuid) {

        List<Transfer> encryptedTransfers = transferRepository
                .findAllBySenderBankUserUserIdOrReceiverBankUserUserIdOrderByTimestamp(uuid, uuid);
        List<Transfer> decryptedTransfers = encryptedTransfers.stream()
                .map(encryptionService::decryptTransfer).toList();
        return decryptedTransfers.stream().map(transferMapper::toTransferGetDTO).toList();
    }
}
