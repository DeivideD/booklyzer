package com.pge.booklyzer.domain.factory;

import com.pge.booklyzer.domain.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFactory {
  public static User create() {
    return new User();
  }

  public static User create(UUID id) {
    User object = create();
    object.setId(id);
    return object;
  }

  public static User create(UUID id, String name) {
    User object = create(id);
    object.setName(name);
    return object;
  }


}
