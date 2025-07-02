package com.pge.booklyzer.application.mapper;

import com.pge.booklyzer.application.dto.user.input.UserInputDTO;
import com.pge.booklyzer.application.dto.user.output.UserFullOutputDTO;
import com.pge.booklyzer.application.dto.user.output.UserOutputDTO;
import com.pge.booklyzer.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User inputToEntity(UserInputDTO inputDTO);
  UserOutputDTO toOutputDTO(User entity);
  UserFullOutputDTO toFullOutputDTO(User entity);

}
