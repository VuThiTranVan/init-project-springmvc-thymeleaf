package com.spring.sample.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface NullOrNotBlank {
	String message() default "Fields are required";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}