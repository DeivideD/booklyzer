package com.pge.booklyzer.domain.factory;

import com.pge.booklyzer.domain.model.Author;
import com.pge.booklyzer.domain.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorFactory {
  public static Author create() {
    return new Author();
  }

  public static Author create(UUID id) {
    Author object = create();
    object.setId(id);
    return object;
  }

  public static Author create(UUID id, String name) {
    Author object = create(id);
    object.setName(name);
    return object;
  }


}
