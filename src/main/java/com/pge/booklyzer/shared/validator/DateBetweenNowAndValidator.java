package com.pge.booklyzer.shared.validator;

import com.pge.booklyzer.shared.annotation.DateBetweenNowAnd;
import com.pge.booklyzer.shared.util.LocalDateUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateBetweenNowAndValidator implements ConstraintValidator<DateBetweenNowAnd, LocalDate> {
  private int days;

  @Override
  public void initialize(DateBetweenNowAnd constraintAnnotation) {
    this.days = constraintAnnotation.days();
  }

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext constraintValidatorContext) {
   return LocalDateUtil.isDateBetweenNowAnd(value, days);
  }
}
