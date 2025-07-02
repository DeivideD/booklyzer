package com.pge.booklyzer.domain.service;

import com.pge.booklyzer.application.dto.user.input.UserInputDTO;
import com.pge.booklyzer.application.dto.user.output.UserFullOutputDTO;
import com.pge.booklyzer.application.dto.user.output.UserOutputDTO;
import com.pge.booklyzer.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.UUID;

public interface UserService extends UserDetailsService {
  Page<UserOutputDTO> findAll(Pageable pageable);
  UserFullOutputDTO findById(UUID id);
  Optional<User> findUserById(UUID id);
  UserFullOutputDTO save(UserInputDTO dto);
  UserFullOutputDTO update(UUID id, UserInputDTO dto);
}
