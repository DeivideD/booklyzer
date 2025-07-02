package com.pge.booklyzer.application.mapper;

import com.pge.booklyzer.application.dto.book.input.BookInputDTO;
import com.pge.booklyzer.application.dto.book.output.BookFullOutputDTO;
import com.pge.booklyzer.application.dto.book.output.BookOutputDTO;
import com.pge.booklyzer.application.dto.user.input.UserInputDTO;
import com.pge.booklyzer.application.dto.user.output.UserOutputDTO;
import com.pge.booklyzer.domain.model.Book;
import com.pge.booklyzer.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookMapper {
  @Mapping(target = "author.id", source = "authorId")
  Book inputToEntity(BookInputDTO inputDTO);
  @Mapping(target = "authorName", source = "author.name")
  BookOutputDTO toOutputDTO(Book entity);
  BookFullOutputDTO toFullOutputDTO(Book entity);
}
