package safe.bank.app.authservice.mappers;

import org.mapstruct.Mapper;
import safe.bank.app.authservice.dtos.BankUserDTO;
import safe.bank.app.authservice.dtos.UserPostDTO;

@Mapper
public interface BankUserMapper {

    BankUserDTO fromUserToBankUserDTO(UserPostDTO userPostDTO);
}
