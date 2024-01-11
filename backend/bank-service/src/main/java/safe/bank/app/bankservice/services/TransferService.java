package safe.bank.app.bankservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import safe.bank.app.bankservice.dtos.TransferCreateDTO;
import safe.bank.app.bankservice.dtos.TransferGetDTO;
import safe.bank.app.bankservice.entities.BankAccount;
import safe.bank.app.bankservice.entities.Transfer;
import safe.bank.app.bankservice.mappers.TransferMapper;
import safe.bank.app.bankservice.repositories.TransferRepository;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final BankAccountService bankAccountService;
    private final TransferMapper transferMapper;

    @Transactional
    public void sendTransaction(TransferCreateDTO transferCreateDTO, UUID userId) {

        List<BankAccount> senderAccounts = bankAccountService.getBankAccounts(userId);
        List<String> accountNumbers = senderAccounts.stream().map(BankAccount::getAccountNumber).toList();
        if(!accountNumbers.contains(transferCreateDTO.getSenderAccount())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have access to this account number.");
        }

        BankAccount receiverAccount = bankAccountService.getBankAccountByAccountNumber(
                transferCreateDTO.getReceiverAccount());

        String receiverFirstName = receiverAccount.getBankUser().getFirstName();
        String receiverLastName = receiverAccount.getBankUser().getLastName();
        String receiverFullName = receiverFirstName + " " + receiverLastName;

        if(!receiverFullName.equalsIgnoreCase(transferCreateDTO.getReceiverName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Receivers name doesnt match the account number");
        }

        BankAccount senderAccount = bankAccountService.getBankAccountByAccountNumber(
                transferCreateDTO.getSenderAccount());

        if (senderAccount.getBalance().compareTo(transferCreateDTO.getAmount()) >= 0) {

            senderAccount.setBalance(senderAccount.getBalance()
                    .subtract(transferCreateDTO.getAmount()));
            receiverAccount.setBalance(receiverAccount.getBalance()
                    .add(transferCreateDTO.getAmount()));

            Transfer transfer = transferMapper.toEntity(transferCreateDTO,
                    senderAccount, receiverAccount);

            bankAccountService.saveBankAccount(senderAccount);
            bankAccountService.saveBankAccount(receiverAccount);
            transferRepository.save(transfer);

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Insufficient funds.");
        }

    }

    public List<TransferGetDTO> getTransfers(UUID uuid) {

        List<Transfer> transfers = transferRepository.findAllBySenderBankUserUserId(uuid);
        return transfers.stream().map(transferMapper::toTransferGetDTO).toList();
    }
}
