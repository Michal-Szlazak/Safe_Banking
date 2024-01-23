package safe.bank.app.bankservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import safe.bank.app.bankservice.dtos.BankAccountCreateDto;
import safe.bank.app.bankservice.dtos.BankAccountGetDTO;
import safe.bank.app.bankservice.dtos.GeneratedAccountDTO;
import safe.bank.app.bankservice.entities.BankAccount;
import safe.bank.app.bankservice.entities.BankUser;

@Mapper
public interface BankAccountMapper {

    @Mapping(target = "accountName", source = "bankAccountCreateDto.accountName")
    @Mapping(target = "cvv", source = "generatedAccountDTO.cvv")
    @Mapping(target = "accountNumber", source = "generatedAccountDTO.accountNumber")
    @Mapping(target = "expiresAt", source = "generatedAccountDTO.expiresAt")
    @Mapping(target = "iv", source = "generatedAccountDTO.iv")
    @Mapping(target = "balance", constant = "1000")
    BankAccount toEntity(BankAccountCreateDto bankAccountCreateDto,
                         BankUser bankUser,
                         GeneratedAccountDTO generatedAccountDTO);

    BankAccountGetDTO toBankAccountGetDTO(BankAccount bankAccount);
}
