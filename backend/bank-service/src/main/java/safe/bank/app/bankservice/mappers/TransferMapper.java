package safe.bank.app.bankservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import safe.bank.app.bankservice.dtos.TransferCreateDTO;
import safe.bank.app.bankservice.dtos.TransferGetDTO;
import safe.bank.app.bankservice.entities.BankAccount;
import safe.bank.app.bankservice.entities.Transfer;
import safe.bank.app.bankservice.services.EncryptionService;

@Mapper
public interface TransferMapper {

    @Mapping(target = "iv", ignore = true)
    Transfer toEntity(TransferCreateDTO transferCreateDTO,
                      BankAccount sender,
                      BankAccount receiver);

    @Mapping(source = "transfer.sender.accountNumber", target = "senderNumber")
    @Mapping(source = "transfer.receiver.accountNumber", target = "receiverNumber")
    TransferGetDTO toTransferGetDTO(Transfer transfer);
}
