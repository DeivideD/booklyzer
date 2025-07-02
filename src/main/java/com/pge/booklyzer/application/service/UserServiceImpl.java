package com.pge.booklyzer.application.service;

import com.pge.booklyzer.application.dto.user.input.UserInputDTO;
import com.pge.booklyzer.application.dto.user.output.UserFullOutputDTO;
import com.pge.booklyzer.application.dto.user.output.UserOutputDTO;
import com.pge.booklyzer.application.mapper.UserMapper;
import com.pge.booklyzer.domain.model.User;
import com.pge.booklyzer.domain.repository.UserRepository;
import com.pge.booklyzer.domain.service.UserService;
import com.pge.booklyzer.shared.exceptions.BadRequestException;
import com.pge.booklyzer.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository repository;
  private final UserMapper mapper;

  @Override
  public Page<UserOutputDTO> findAll(Pageable pageable) {
    return repository.findAll(pageable).map(mapper::toOutputDTO);
  }

  @Override
  public UserFullOutputDTO findById(UUID id) {
    return repository.findById(id).map(mapper::toFullOutputDTO)
            .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
  }

  @Override
  public Optional<User> findUserById(UUID id) {
    return repository.findById(id);
  }

  @Override
  @Transactional
  public UserFullOutputDTO save(UserInputDTO dto) {
    validaUserInput(dto);
    return Optional.of(dto).map(mapper::inputToEntity).map(entityToSave-> {
      repository.save(entityToSave);
      return mapper.toFullOutputDTO(entityToSave);
    }).orElseThrow(() -> new BadRequestException("Registro inválido"));
  }

  @Override
  @Transactional
  public UserFullOutputDTO update(UUID id, UserInputDTO dto) {
    validaUserInput(dto);
    return repository.findById(id).map(entity -> {
      User entityToUpdate = mapper.inputToEntity(dto);
      entityToUpdate.setId(id);
      repository.save(entityToUpdate);
      return mapper.toFullOutputDTO(entityToUpdate);
    }).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
  }

  private void validaUserInput(UserInputDTO dto) {
    if (dto == null) throw new BadRequestException("Usuário não pode ser nulo");
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return repository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Não foi possível localizar"));
  }
}

