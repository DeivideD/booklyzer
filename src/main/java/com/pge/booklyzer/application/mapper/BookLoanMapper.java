package com.pge.booklyzer.application.mapper;

import com.pge.booklyzer.application.dto.bookloan.output.BookLoanFullOutputDTO;
import com.pge.booklyzer.application.dto.bookloan.output.BookLoanOutputDTO;
import com.pge.booklyzer.domain.model.BookLoan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookLoanMapper {
  @Mapping(target = "userName", source = "user.name")
  @Mapping(target = "bookTitle", source = "book.title")
  BookLoanOutputDTO toOutputDTO(BookLoan entity);

  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "userName", source = "user.name")
  @Mapping(target = "bookId", source = "book.id")
  @Mapping(target = "bookTitle", source = "book.title")
  BookLoanFullOutputDTO toFullOutputDTO(BookLoan entity);
}
