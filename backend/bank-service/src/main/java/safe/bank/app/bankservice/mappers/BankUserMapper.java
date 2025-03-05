package safe.bank.app.bankservice.mappers;

import org.mapstruct.Mapper;
import safe.bank.app.bankservice.dtos.PostBankUserDTO;
import safe.bank.app.bankservice.entities.BankUser;

@Mapper
public interface BankUserMapper {

    BankUser toEntity(PostBankUserDTO userDTO);
}
