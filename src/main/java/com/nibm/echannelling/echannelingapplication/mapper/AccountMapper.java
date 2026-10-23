package com.nibm.echannelling.echannelingapplication.mapper;

import com.nibm.echannelling.echannelingapplication.dto.AccountDto;
import com.nibm.echannelling.echannelingapplication.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toDto(User user);
}
