package com.pge.booklyzer.domain.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookFilter {
  private String title;
  private String ISBN;
  private UUID authorId;
  private boolean available;
}
