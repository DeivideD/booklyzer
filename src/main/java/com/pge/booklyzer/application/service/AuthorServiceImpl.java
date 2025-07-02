package com.pge.booklyzer.application.service;

import com.pge.booklyzer.application.dto.author.input.AuthorInputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorFullOutputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorOutputDTO;
import com.pge.booklyzer.application.mapper.AuthorMapper;
import com.pge.booklyzer.domain.filter.AuthorFilter;
import com.pge.booklyzer.domain.model.Author;
import com.pge.booklyzer.domain.repository.AuthorRepository;
import com.pge.booklyzer.domain.service.AuthorService;
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
public class AuthorServiceImpl implements AuthorService {
  private final AuthorRepository repository;
  private final AuthorMapper mapper;

  @Override
  public Page<AuthorOutputDTO> findAll(Pageable pageable, AuthorFilter filter) {
    return repository.findAll(pageable, filter.getName()).map(mapper::toOutputDTO);
  }

  @Override
  public AuthorFullOutputDTO findById(UUID id) {
    return repository.findById(id).map(mapper::toFullOutputDTO)
            .orElseThrow(() -> new NotFoundException("Autor não encontrado"));
  }

  @Override
  @Transactional
  public AuthorFullOutputDTO save(AuthorInputDTO dto) {
    validaAuthorInput(dto);
    return Optional.of(dto).map(mapper::inputToEntity).map(entityToSave-> {
      repository.save(entityToSave);
      return mapper.toFullOutputDTO(entityToSave);
    }).orElseThrow(() -> new BadRequestException("Registro inválido"));
  }

  @Override
  @Transactional
  public AuthorFullOutputDTO update(UUID id, AuthorInputDTO dto) {
    validaAuthorInput(dto);
    return repository.findById(id).map(entity -> {
      Author entityToUpdate = mapper.inputToEntity(dto);
      entityToUpdate.setId(id);
      repository.save(entityToUpdate);
      return mapper.toFullOutputDTO(entityToUpdate);
    }).orElseThrow(() -> new NotFoundException("Autor não encontrado"));
  }

  private void validaAuthorInput(AuthorInputDTO dto) {
    if (dto == null) throw new BadRequestException("Autor não pode ser nulo");
  }
  
}

