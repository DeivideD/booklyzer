package com.pge.booklyzer.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.pge.booklyzer.application.dto.user.input.UserInputDTO;
import com.pge.booklyzer.application.dto.user.output.UserFullOutputDTO;
import com.pge.booklyzer.application.dto.user.output.UserOutputDTO;
import com.pge.booklyzer.application.mapper.UserMapper;
import com.pge.booklyzer.domain.model.User;
import com.pge.booklyzer.domain.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository repository;

  @Mock
  private UserMapper mapper;

  @InjectMocks
  private UserServiceImpl userService;

  private final UUID userId = UUID.randomUUID();
  private final String email = "user@example.com";
  private final String name = "John Doe";

  @Test
  void findAll_ShouldReturnPageOfUserOutputDTO() {
    Pageable pageable = mock(Pageable.class);
    User user = new User();
    UserOutputDTO outputDTO = new UserOutputDTO();
    outputDTO.setId(userId);
    outputDTO.setName(name);
    outputDTO.setEmail(email);

    Page<User> page = new PageImpl<>(Collections.singletonList(user));

    when(repository.findAll(pageable)).thenReturn(page);
    when(mapper.toOutputDTO(user)).thenReturn(outputDTO);

    Page<UserOutputDTO> result = userService.findAll(pageable);

    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    verify(repository).findAll(pageable);
    verify(mapper).toOutputDTO(user);
  }

  @Test
  void findById_ShouldReturnUserFullOutputDTOWhenExists() {
    User user = new User();
    UserFullOutputDTO fullOutputDTO = new UserFullOutputDTO();
    fullOutputDTO.setId(userId);
    fullOutputDTO.setName(name);
    fullOutputDTO.setEmail(email);

    when(repository.findById(userId)).thenReturn(Optional.of(user));
    when(mapper.toFullOutputDTO(user)).thenReturn(fullOutputDTO);

    UserFullOutputDTO result = userService.findById(userId);

    assertNotNull(result);
    assertEquals(name, result.getName());
    verify(repository).findById(userId);
    verify(mapper).toFullOutputDTO(user);
  }

  @Test
  void findById_ShouldThrowNotFoundExceptionWhenNotExists() {
    when(repository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.findById(userId));
    verify(repository).findById(userId);
  }

  @Test
  void findUserById_ShouldReturnOptionalUserWhenExists() {
    User user = new User();
    when(repository.findById(userId)).thenReturn(Optional.of(user));

    Optional<User> result = userService.findUserById(userId);

    assertTrue(result.isPresent());
    verify(repository).findById(userId);
  }

  @Test
  void save_ShouldReturnUserFullOutputDTOWhenValidInput() {
    UserInputDTO inputDTO = new UserInputDTO(name, email, "password", "USER");
    User user = new User();
    UserFullOutputDTO fullOutputDTO = new UserFullOutputDTO();
    fullOutputDTO.setId(userId);
    fullOutputDTO.setName(name);
    fullOutputDTO.setEmail(email);

    when(mapper.inputToEntity(inputDTO)).thenReturn(user);
    when(repository.save(user)).thenReturn(user);
    when(mapper.toFullOutputDTO(user)).thenReturn(fullOutputDTO);

    UserFullOutputDTO result = userService.save(inputDTO);

    assertNotNull(result);
    assertEquals(email, result.getEmail());
    verify(mapper).inputToEntity(inputDTO);
    verify(repository).save(user);
    verify(mapper).toFullOutputDTO(user);
  }

  @Test
  void save_ShouldThrowBadRequestExceptionWhenNullInput() {
    assertThrows(BadRequestException.class, () -> userService.save(null));
  }

  @Test
  void update_ShouldReturnUpdatedUserFullOutputDTOWhenExists() {
    UserInputDTO inputDTO = new UserInputDTO(name, email, "password", "USER");
    User existingUser = new User();
    User updatedUser = new User();
    UserFullOutputDTO fullOutputDTO = new UserFullOutputDTO();
    fullOutputDTO.setId(userId);
    fullOutputDTO.setName(name);
    fullOutputDTO.setEmail(email);

    when(repository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(mapper.inputToEntity(inputDTO)).thenReturn(updatedUser);
    when(repository.save(updatedUser)).thenReturn(updatedUser);
    when(mapper.toFullOutputDTO(updatedUser)).thenReturn(fullOutputDTO);

    UserFullOutputDTO result = userService.update(userId, inputDTO);

    assertNotNull(result);
    assertEquals(name, result.getName());
    verify(repository).findById(userId);
    verify(mapper).inputToEntity(inputDTO);
    verify(repository).save(updatedUser);
    verify(mapper).toFullOutputDTO(updatedUser);
  }

  @Test
  void update_ShouldThrowNotFoundExceptionWhenNotExists() {
    UserInputDTO inputDTO = new UserInputDTO(name, email, "password", "USER");
    when(repository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.update(userId, inputDTO));
    verify(repository).findById(userId);
  }

  @Test
  void loadUserByUsername_ShouldReturnUserDetailsWhenExists() {
    User user = new User();
    user.setEmail(email);

    when(repository.findByEmail(email)).thenReturn(Optional.of(user));

    UserDetails result = userService.loadUserByUsername(email);

    assertNotNull(result);
    assertEquals(email, result.getUsername());
    verify(repository).findByEmail(email);
  }

  @Test
  void loadUserByUsername_ShouldThrowUsernameNotFoundExceptionWhenNotExists() {
    when(repository.findByEmail(email)).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    verify(repository).findByEmail(email);
  }
}