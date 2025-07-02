package com.pge.booklyzer.shared.validator;

import com.pge.booklyzer.shared.annotation.ISBN;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISBNValidator implements ConstraintValidator<ISBN, String> {
  @Override
  public void initialize(ISBN constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String isbn, ConstraintValidatorContext context) {
    if (isbn == null) {
      return false;
    }

    String cleanedIsbn = isbn.replaceAll("[\\- ]", "");

    if (!cleanedIsbn.matches("^\\d{9}[\\dXx]$") && !cleanedIsbn.matches("^\\d{13}$")) {
      return false;
    }

    if (cleanedIsbn.length() == 10) {
      return isValidISBN10(cleanedIsbn);
    } else if (cleanedIsbn.length() == 13) {
      return isValidISBN13(cleanedIsbn);
    }

    return false;
  }

  private boolean isValidISBN10(String isbn) {
    int sum = 0;
    for (int i = 0; i < 9; i++) {
      int digit = Character.getNumericValue(isbn.charAt(i));
      sum += (i + 1) * digit;
    }

    char lastChar = isbn.charAt(9);
    int checkDigit;
    if (lastChar == 'X' || lastChar == 'x') {
      checkDigit = 10;
    } else {
      checkDigit = Character.getNumericValue(lastChar);
    }

    return (sum % 11) == checkDigit;
  }

  private boolean isValidISBN13(String isbn) {
    int sum = 0;
    for (int i = 0; i < 12; i++) {
      int digit = Character.getNumericValue(isbn.charAt(i));
      sum += (i % 2 == 0) ? digit : digit * 3;
    }

    int checkDigit = Character.getNumericValue(isbn.charAt(12));
    int calculatedCheck = (10 - (sum % 10)) % 10;

    return checkDigit == calculatedCheck;
  }
}
