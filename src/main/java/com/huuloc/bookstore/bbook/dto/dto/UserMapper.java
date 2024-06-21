package com.huuloc.bookstore.bbook.dto.dto;

import com.huuloc.bookstore.bbook.dto.UserDto;
import com.huuloc.bookstore.bbook.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(User user);
}
