package com.pge.booklyzer.application.dto.user.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserBaseDTO {
  private UUID id;
  private String name;
  private String registration;
  private String email;
}
