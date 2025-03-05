package safe.bank.app.bankservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import safe.bank.app.bankservice.CvvGenerator;
import safe.bank.app.bankservice.dtos.TransferCreateDTO;
import safe.bank.app.bankservice.dtos.TransferGetDTO;
import safe.bank.app.bankservice.entities.BankAccount;
import safe.bank.app.bankservice.entities.BankUser;
import safe.bank.app.bankservice.entities.Transfer;
import safe.bank.app.bankservice.mappers.TransferMapper;
import safe.bank.app.bankservice.repositories.BankAccountRepository;
import safe.bank.app.bankservice.repositories.TransferRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final BankAccountRepository bankAccountRepository;
    private final TransferMapper transferMapper;
    private final CvvGenerator cvvGenerator;

    @Transactional()
    public void sendTransfer(TransferCreateDTO transferCreateDTO, UUID userId) {

        verifyUserAccessToAccount(userId, transferCreateDTO);
        verifyGivenReceiverNameWithReceiverAccount(transferCreateDTO);

        BankAccount receiverAccount = bankAccountRepository.findBankAccountByAccountNumber(transferCreateDTO.getReceiverAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        BankAccount senderAccount = bankAccountRepository.findBankAccountByAccountNumber(transferCreateDTO.getSenderAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        BigDecimal senderAccountBalance = new BigDecimal(senderAccount.getBalance());
        BigDecimal receiverAccountBalance = new BigDecimal(receiverAccount.getBalance());

        if (senderAccountBalance.compareTo(transferCreateDTO.getAmount()) >= 0) {

            senderAccount.setBalance(senderAccountBalance
                    .subtract(transferCreateDTO.getAmount()).toString());
            receiverAccount.setBalance(receiverAccountBalance
                    .add(transferCreateDTO.getAmount()).toString());

            Transfer transfer = transferMapper.toEntity(transferCreateDTO,
                    senderAccount, receiverAccount);

            transferRepository.save(transfer);

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Insufficient funds.");
        }

    }

    public void verifyUserAccessToAccount(UUID userId, TransferCreateDTO transferCreateDTO) {

        Optional<BankAccount> optSenderAccount = bankAccountRepository.findBankAccountByAccountNumber(
                transferCreateDTO.getSenderAccount()
        );

        BankAccount senderAccount = optSenderAccount.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bank account doesnt exist")
        );

        if(!senderAccount.getBankUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You do not have access to this account number.");
        }

        boolean validCvv = cvvGenerator.validateCVV(
                transferCreateDTO.getCvv(),
                senderAccount.getAccountNumber(),
                senderAccount.getExpiresAt()
        );

        if(!validCvv) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid CVV provided.");
        }
    }
    
    public void verifyGivenReceiverNameWithReceiverAccount(TransferCreateDTO transferCreateDTO) {

        BankAccount receiverAccount = bankAccountRepository.findBankAccountByAccountNumber(transferCreateDTO.getReceiverAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        BankUser bankUser = receiverAccount.getBankUser();

        String receiverFirstName = bankUser.getFirstName();
        String receiverLastName = bankUser.getLastName();
        String receiverFullName = receiverFirstName + " " + receiverLastName;

        if(!receiverFullName.equalsIgnoreCase(transferCreateDTO.getReceiverName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Receivers name doesnt match the account number");
        }
    }
    public List<TransferGetDTO> getTransfers(UUID uuid) {

        List<Transfer> encryptedTransfers = transferRepository
                .findAllBySenderBankUserUserIdOrReceiverBankUserUserIdOrderByTimestamp(uuid, uuid);

        return encryptedTransfers.stream().map(transferMapper::toTransferGetDTO).toList();
    }
}
