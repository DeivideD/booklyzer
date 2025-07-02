package com.pge.booklyzer.domain.model;

import com.pge.booklyzer.shared.annotation.ISBN;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)

  private UUID id;

  @Column(unique = true)
  private String title;

  @ManyToOne(optional = false)
  @JoinColumn(name = "author_id")
  private Author author;

  @ISBN
  @Column(unique = true)
  private String isbn;

  @Column(nullable = false)
  @Min(0)
  private Integer availableQuantity;

  public Book(UUID id) {
    this.id = id;
  }
}
