package com.pge.booklyzer.domain.factory;

import com.pge.booklyzer.domain.model.Book;
import com.pge.booklyzer.domain.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookFactory {
  public static Book create() {
    return new Book();
  }

  public static Book create(UUID id) {
    Book object = create();
    object.setId(id);
    return object;
  }

  public static Book create(UUID id, String title) {
    Book object = create(id);
    object.setTitle(title);
    return object;
  }

}
