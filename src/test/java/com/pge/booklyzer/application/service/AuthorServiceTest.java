package com.pge.booklyzer.application.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pge.booklyzer.application.dto.author.input.AuthorInputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorFullOutputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorOutputDTO;
import com.pge.booklyzer.application.mapper.AuthorMapper;
import com.pge.booklyzer.domain.factory.AuthorFactory;
import com.pge.booklyzer.domain.filter.AuthorFilter;
import com.pge.booklyzer.domain.model.Author;
import com.pge.booklyzer.domain.repository.AuthorRepository;
import com.pge.booklyzer.shared.exceptions.BadRequestException;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Nested
@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

  @Mock
  private AuthorRepository repository;

  @Mock
  private AuthorMapper mapper;

  @InjectMocks
  private AuthorServiceImpl authorService;

  @Test
  void findAllShouldReturnPageOfAuthorOutputDTO() {
    Pageable pageable = mock(Pageable.class);
    Author author = AuthorFactory.create(UUID.randomUUID(), "John Doe");
    AuthorFilter filter = new AuthorFilter("John");
    AuthorOutputDTO outputDTO = mock(AuthorOutputDTO.class);
    Page<Author> page = new PageImpl<>(Collections.singletonList(author));

    when(repository.findAll(pageable, filter.getName())).thenReturn(page);
    when(mapper.toOutputDTO(any())).thenReturn(outputDTO);

    Page<AuthorOutputDTO> result = authorService.findAll(pageable, filter);

    assertNotNull(result);
    verify(repository).findAll(pageable, filter.getName());
    verify(mapper).toOutputDTO(author);
  }

  @Test
  void findByIdShouldReturnAuthorFullOutputDTOWhenExists() {
    UUID id = UUID.randomUUID();
    Author author = mock(Author.class);
    AuthorFullOutputDTO fullOutputDTO = mock(AuthorFullOutputDTO.class);

    when(repository.findById(id)).thenReturn(Optional.of(author));
    when(mapper.toFullOutputDTO(author)).thenReturn(fullOutputDTO);

    AuthorFullOutputDTO result = authorService.findById(id);

    assertNotNull(result);
    verify(repository).findById(id);
    verify(mapper).toFullOutputDTO(author);
  }

  @Test
  void findByIdShouldThrowNotFoundExceptionWhenNotExists() {
    UUID id = UUID.randomUUID();

    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> authorService.findById(id));
    verify(repository).findById(id);
  }

  @Test
  @Transactional
  void saveShouldReturnAuthorFullOutputDTOWhenValidInput() {
    AuthorInputDTO inputDTO = mock(AuthorInputDTO.class);
    Author author = mock(Author.class);
    AuthorFullOutputDTO fullOutputDTO = mock(AuthorFullOutputDTO.class);

    when(mapper.inputToEntity(inputDTO)).thenReturn(author);
    when(repository.save(author)).thenReturn(author);
    when(mapper.toFullOutputDTO(author)).thenReturn(fullOutputDTO);

    AuthorFullOutputDTO result = authorService.save(inputDTO);

    assertNotNull(result);
    verify(mapper).inputToEntity(inputDTO);
    verify(repository).save(author);
    verify(mapper).toFullOutputDTO(author);
  }

  @Test
  @Transactional
  void saveShouldThrowBadRequestExceptionWhenNullInput() {
    assertThrows(BadRequestException.class, () -> authorService.save(null));
  }

  @Test
  @Transactional
  void updateShouldReturnAuthorFullOutputDTOWhenValidInputAndExists() {
    UUID id = UUID.randomUUID();
    AuthorInputDTO inputDTO = mock(AuthorInputDTO.class);
    Author existingAuthor = mock(Author.class);
    Author updatedAuthor = mock(Author.class);
    AuthorFullOutputDTO fullOutputDTO = mock(AuthorFullOutputDTO.class);

    when(repository.findById(id)).thenReturn(Optional.of(existingAuthor));
    when(mapper.inputToEntity(inputDTO)).thenReturn(updatedAuthor);
    when(repository.save(updatedAuthor)).thenReturn(updatedAuthor);
    when(mapper.toFullOutputDTO(updatedAuthor)).thenReturn(fullOutputDTO);

    AuthorFullOutputDTO result = authorService.update(id, inputDTO);

    assertNotNull(result);
    verify(repository).findById(id);
    verify(mapper).inputToEntity(inputDTO);
    verify(repository).save(updatedAuthor);
    verify(mapper).toFullOutputDTO(updatedAuthor);
  }

  @Test
  @Transactional
  void updateShouldThrowNotFoundExceptionWhenNotExists() {
    UUID id = UUID.randomUUID();
    AuthorInputDTO inputDTO = mock(AuthorInputDTO.class);

    when(repository.findById(id)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> authorService.update(id, inputDTO));
    verify(repository).findById(id);
  }

  @Test
  @Transactional
  void updateShouldThrowBadRequestExceptionWhenNullInput() {
    UUID id = UUID.randomUUID();
    assertThrows(BadRequestException.class, () -> authorService.update(id, null));
  }
}