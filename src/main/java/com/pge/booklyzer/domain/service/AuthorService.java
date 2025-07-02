package com.pge.booklyzer.domain.service;

import com.pge.booklyzer.application.dto.author.input.AuthorInputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorFullOutputDTO;
import com.pge.booklyzer.application.dto.author.output.AuthorOutputDTO;
import com.pge.booklyzer.application.dto.user.input.UserInputDTO;
import com.pge.booklyzer.application.dto.user.output.UserFullOutputDTO;
import com.pge.booklyzer.application.dto.user.output.UserOutputDTO;
import com.pge.booklyzer.domain.filter.AuthorFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AuthorService {
  Page<AuthorOutputDTO> findAll(Pageable pageable, AuthorFilter filter);
  AuthorFullOutputDTO findById(UUID id);
  AuthorFullOutputDTO save(AuthorInputDTO dto);
  AuthorFullOutputDTO update(UUID id, AuthorInputDTO dto);
}
