package com.pge.booklyzer.domain.service;

import com.pge.booklyzer.application.dto.book.input.BookInputDTO;
import com.pge.booklyzer.application.dto.book.output.BookFullOutputDTO;
import com.pge.booklyzer.application.dto.book.output.BookOutputDTO;
import com.pge.booklyzer.domain.filter.BookFilter;
import com.pge.booklyzer.domain.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface BookService {
  Page<BookOutputDTO> findAll(Pageable pageable, BookFilter filter);
  BookFullOutputDTO findById(UUID id);
  Optional<Book> findBookById(UUID id);
  BookFullOutputDTO save(BookInputDTO dto);
  BookFullOutputDTO update(UUID id, BookInputDTO dto);
  void increaseAvailableQuantity(UUID id, Integer quantity);
  void reduceAvailableQuantity(UUID id, Integer quantity);
  Integer findAvailableQuantityById(UUID id);
}
