package com.pge.booklyzer.shared.annotation;

import com.pge.booklyzer.shared.validator.ISBNValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ISBNValidator.class)
@Target({FIELD, ANNOTATION_TYPE, METHOD, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ISBN {
  String message() default "ISBN é inválido!";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

}
