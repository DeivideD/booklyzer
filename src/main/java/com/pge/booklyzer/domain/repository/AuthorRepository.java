package com.pge.booklyzer.domain.repository;

import com.pge.booklyzer.domain.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
  @Query("select a from Author a where (:name is null or :name = '' or a.name ilike %:name%) ")
  Page<Author> findAll(Pageable pageable, @Param("name") String name);
}
