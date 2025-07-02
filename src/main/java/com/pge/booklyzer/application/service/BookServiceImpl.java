package com.pge.booklyzer.application.service;

import com.pge.booklyzer.application.dto.book.input.BookInputDTO;
import com.pge.booklyzer.application.dto.book.output.BookFullOutputDTO;
import com.pge.booklyzer.application.dto.book.output.BookOutputDTO;
import com.pge.booklyzer.application.mapper.BookMapper;
import com.pge.booklyzer.domain.filter.BookFilter;
import com.pge.booklyzer.domain.model.Book;
import com.pge.booklyzer.domain.repository.BookRepository;
import com.pge.booklyzer.domain.service.BookService;
import com.pge.booklyzer.shared.exceptions.BadRequestException;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
  private final BookRepository repository;
  private final BookMapper mapper;

  @Override
  public Page<BookOutputDTO> findAll(Pageable pageable, BookFilter filter) {
    return repository
            .findAll(pageable, filter.getTitle(), filter.getISBN(), filter.getAuthorId(), filter.isAvailable())
            .map(mapper::toOutputDTO);
  }

  @Override
  public BookFullOutputDTO findById(UUID id) {
    return repository.findById(id).map(mapper::toFullOutputDTO)
            .orElseThrow(() -> new NotFoundException("Livro não encontrado"));
  }

  @Override
  public Optional<Book> findBookById(UUID id) {
    return repository.findById(id);
  }

  @Override
  @Transactional
  public BookFullOutputDTO save(BookInputDTO dto) {
    validaBookInput(dto);
    return Optional.of(dto).map(mapper::inputToEntity).map(entityToSave -> {
      repository.save(entityToSave);
      return mapper.toFullOutputDTO(entityToSave);
    }).orElseThrow(() -> new BadRequestException("Registro inválido"));
  }

  @Override
  @Transactional
  public BookFullOutputDTO update(UUID id, BookInputDTO dto) {
    validaBookInput(dto);
    return repository.findById(id).map(entity -> {
      Book entityToUpdate = mapper.inputToEntity(dto);
      entityToUpdate.setId(id);
      repository.save(entityToUpdate);
      return mapper.toFullOutputDTO(entityToUpdate);
    }).orElseThrow(() -> new NotFoundException("Livro não encontrado"));
  }

  @Transactional
  @Override
  public void increaseAvailableQuantity(UUID id, Integer quantity) {
    repository.increaseAvailableQuantity(id, quantity);
  }

  @Transactional
  @Override
  public void reduceAvailableQuantity(UUID id, Integer quantity) {
    repository.reduceAvailableQuantity(id, quantity);
  }

  @Override
  public Integer findAvailableQuantityById(UUID id) {
    return repository.findAvailableQuantityById(id);
  }

  private void validaBookInput(BookInputDTO dto) {
    if (dto == null) throw new BadRequestException("Livro não pode ser nulo");
  }

}

