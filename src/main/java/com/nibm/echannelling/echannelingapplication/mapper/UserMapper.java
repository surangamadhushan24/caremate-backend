package com.nibm.echannelling.echannelingapplication.mapper;


import com.nibm.echannelling.echannelingapplication.dto.UserDTO;
import com.nibm.echannelling.echannelingapplication.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto(User user);
}
