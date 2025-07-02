package com.pge.booklyzer.application.mapper;

import com.pge.booklyzer.application.dto.author.input.AuthorInputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorFullOutputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorOutputDTO;
import com.pge.booklyzer.domain.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
  Author inputToEntity(AuthorInputDTO inputDTO);
  AuthorOutputDTO toOutputDTO(Author entity);
  AuthorFullOutputDTO toFullOutputDTO(Author entity);

}
