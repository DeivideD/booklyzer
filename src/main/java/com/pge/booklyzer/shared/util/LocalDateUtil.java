package com.pge.booklyzer.shared.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateUtil {

  public static boolean isDateBetweenNowAnd(LocalDate value, int days) {
    if (value == null) return true;
    LocalDate today = LocalDate.now();
    LocalDate maxDate = today.plusDays(days);

    return value.isAfter(today) && value.isBefore(maxDate);
  }
}
