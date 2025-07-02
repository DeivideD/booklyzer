package com.pge.booklyzer.domain.repository;

import com.pge.booklyzer.domain.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
  @Query("""
          select b from Book b
           where (:title is null or :title = '' or lower(b.title) like lower(concat('%', :title, '%')))
           and (:isbn is null or :isbn = '' or lower(b.isbn) like lower(concat('%', :isbn, '%')))
           and (:authorId is null or b.author.id = :authorId)
           and (:available is false or b.availableQuantity > 0)
          """)
  Page<Book> findAll(Pageable pageable, String title, String isbn, UUID authorId, boolean available);

  @Query("select b.availableQuantity from Book b where b.id = :id")
  Integer findAvailableQuantityById(UUID id);

  @Modifying
  @Query("update Book b set b.availableQuantity = b.availableQuantity + :quantity where b.id = :id")
  void increaseAvailableQuantity(UUID id, Integer quantity);

  @Modifying
  @Query("update Book b set b.availableQuantity = b.availableQuantity - :quantity where b.id = :id")
  void reduceAvailableQuantity(UUID id, Integer quantity);

}
