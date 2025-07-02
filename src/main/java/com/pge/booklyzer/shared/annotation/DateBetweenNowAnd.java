package com.pge.booklyzer.shared.annotation;

import com.pge.booklyzer.shared.validator.DateBetweenNowAndValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateBetweenNowAndValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateBetweenNowAnd {
  String message() default "A data deve ser maior que hoje e menor que o prazo de dias";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
  int days();
}
