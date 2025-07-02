package com.pge.booklyzer.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pge.booklyzer.application.dto.author.output.AuthorOutputDTO;
import com.pge.booklyzer.application.dto.book.input.BookInputDTO;
import com.pge.booklyzer.application.dto.book.output.BookFullOutputDTO;
import com.pge.booklyzer.application.dto.book.output.BookOutputDTO;
import com.pge.booklyzer.application.mapper.BookMapper;
import com.pge.booklyzer.domain.filter.BookFilter;
import com.pge.booklyzer.domain.model.Book;
import com.pge.booklyzer.domain.repository.BookRepository;
import com.pge.booklyzer.shared.exceptions.BadRequestException;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @Mock
  private BookRepository repository;

  @Mock
  private BookMapper mapper;

  @InjectMocks
  private BookServiceImpl bookService;

  private final UUID bookId = UUID.randomUUID();
  private final String bookTitle = "Clean Code";
  private final String isbn = "978-0132350884";
  private final UUID authorId = UUID.randomUUID();

  @Test
  void findAll_ShouldReturnPageOfBookOutputDTO() {
    Pageable pageable = mock(Pageable.class);
    BookFilter filter = new BookFilter(bookTitle, isbn, authorId, true);
    Book book = new Book();
    BookOutputDTO outputDTO = new BookOutputDTO();
    outputDTO.setId(bookId);
    outputDTO.setTitle(bookTitle);
    outputDTO.setIsbn(isbn);

    Page<Book> page = new PageImpl<>(Collections.singletonList(book));

    when(repository.findAll(pageable, filter.getTitle(), filter.getISBN(), filter.getAuthorId(), filter.isAvailable()))
            .thenReturn(page);
    when(mapper.toOutputDTO(book)).thenReturn(outputDTO);

    Page<BookOutputDTO> result = bookService.findAll(pageable, filter);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(repository).findAll(pageable, filter.getTitle(), filter.getISBN(), filter.getAuthorId(), filter.isAvailable());
    verify(mapper).toOutputDTO(book);
  }

  @Test
  void findById_ShouldReturnBookFullOutputDTOWhenExists() {
    Book book = new Book();
    BookOutputDTO outputDTO = new BookOutputDTO();
    outputDTO.setId(bookId);
    outputDTO.setTitle(bookTitle);
    outputDTO.setIsbn(isbn);
    AuthorOutputDTO authorOutputDTO = new AuthorOutputDTO();
    authorOutputDTO.setName("Robert C. Martin");

    BookFullOutputDTO fullOutputDTO = new BookFullOutputDTO();
    fullOutputDTO.setAuthor(authorOutputDTO);
    fullOutputDTO.setId(bookId);
    fullOutputDTO.setTitle(bookTitle);
    fullOutputDTO.setIsbn(isbn);
    fullOutputDTO.setAvailableQuantity(10);

    when(repository.findById(bookId)).thenReturn(Optional.of(book));
    when(mapper.toFullOutputDTO(book)).thenReturn(fullOutputDTO);

    BookFullOutputDTO result = bookService.findById(bookId);

    assertNotNull(result);
    assertEquals(bookTitle, result.getTitle());
    verify(repository).findById(bookId);
    verify(mapper).toFullOutputDTO(book);
  }

  @Test
  void findById_ShouldThrowNotFoundExceptionWhenNotExists() {
    when(repository.findById(bookId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> bookService.findById(bookId));
    verify(repository).findById(bookId);
  }

  @Test
  void findBookById_ShouldReturnOptionalBookWhenExists() {
    Book book = new Book();
    when(repository.findById(bookId)).thenReturn(Optional.of(book));

    Optional<Book> result = bookService.findBookById(bookId);

    assertTrue(result.isPresent());
    verify(repository).findById(bookId);
  }

  @Test
  void save_ShouldReturnBookFullOutputDTOWhenValidInput() {
    BookInputDTO inputDTO = new BookInputDTO();
    inputDTO.setIsbn(isbn);
    inputDTO.setTitle(bookTitle);
    inputDTO.setAvailableQuantity(10);
    inputDTO.setAuthorId(authorId);

    Book book = new Book();
    BookFullOutputDTO fullOutputDTO = new BookFullOutputDTO();
    fullOutputDTO.setId(bookId);
    fullOutputDTO.setTitle(bookTitle);
    fullOutputDTO.setIsbn(isbn);

    when(mapper.inputToEntity(inputDTO)).thenReturn(book);
    when(repository.save(book)).thenReturn(book);
    when(mapper.toFullOutputDTO(book)).thenReturn(fullOutputDTO);

    BookFullOutputDTO result = bookService.save(inputDTO);

    assertNotNull(result);
    assertEquals(bookTitle, result.getTitle());
    verify(mapper).inputToEntity(inputDTO);
    verify(repository).save(book);
    verify(mapper).toFullOutputDTO(book);
  }

  @Test
  void save_ShouldThrowBadRequestExceptionWhenNullInput() {
    assertThrows(BadRequestException.class, () -> bookService.save(null));
  }

  @Test
  void update_ShouldReturnUpdatedBookFullOutputDTOWhenExists() {
    BookInputDTO inputDTO = new BookInputDTO();
    inputDTO.setIsbn(isbn);
    inputDTO.setTitle(bookTitle);
    inputDTO.setAvailableQuantity(10);
    inputDTO.setAuthorId(authorId);

    Book existingBook = new Book();
    Book updatedBook = new Book();
    BookFullOutputDTO fullOutputDTO = new BookFullOutputDTO();
    fullOutputDTO.setId(bookId);
    fullOutputDTO.setTitle(bookTitle);
    fullOutputDTO.setIsbn(isbn);
    fullOutputDTO.setAvailableQuantity(10);

    when(repository.findById(bookId)).thenReturn(Optional.of(existingBook));
    when(mapper.inputToEntity(inputDTO)).thenReturn(updatedBook);
    when(repository.save(updatedBook)).thenReturn(updatedBook);
    when(mapper.toFullOutputDTO(updatedBook)).thenReturn(fullOutputDTO);

    BookFullOutputDTO result = bookService.update(bookId, inputDTO);

    assertNotNull(result);
    assertEquals(bookTitle, result.getTitle());
    verify(repository).findById(bookId);
    verify(mapper).inputToEntity(inputDTO);
    verify(repository).save(updatedBook);
    verify(mapper).toFullOutputDTO(updatedBook);
  }

  @Test
  void update_ShouldThrowNotFoundExceptionWhenNotExists() {
    BookInputDTO inputDTO = new BookInputDTO();
    inputDTO.setIsbn(isbn);
    inputDTO.setTitle(bookTitle);
    inputDTO.setAvailableQuantity(10);
    inputDTO.setAuthorId(authorId);
    when(repository.findById(bookId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> bookService.update(bookId, inputDTO));
    verify(repository).findById(bookId);
  }

  @Test
  void increaseAvailableQuantity_ShouldCallRepositoryMethod() {
    int quantity = 5;
    doNothing().when(repository).increaseAvailableQuantity(bookId, quantity);

    bookService.increaseAvailableQuantity(bookId, quantity);

    verify(repository).increaseAvailableQuantity(bookId, quantity);
  }

  @Test
  void reduceAvailableQuantity_ShouldCallRepositoryMethod() {
    int quantity = 3;
    doNothing().when(repository).reduceAvailableQuantity(bookId, quantity);

    bookService.reduceAvailableQuantity(bookId, quantity);

    verify(repository).reduceAvailableQuantity(bookId, quantity);
  }

  @Test
  void findAvailableQuantityById_ShouldReturnQuantity() {
    int expectedQuantity = 5;
    when(repository.findAvailableQuantityById(bookId)).thenReturn(expectedQuantity);

    int result = bookService.findAvailableQuantityById(bookId);

    assertEquals(expectedQuantity, result);
    verify(repository).findAvailableQuantityById(bookId);
  }
}