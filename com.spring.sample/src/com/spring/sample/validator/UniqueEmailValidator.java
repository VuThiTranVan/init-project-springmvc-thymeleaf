package com.spring.sample.validator;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;

import com.spring.sample.service.UserService;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Object> {

	private static final Logger logger = LoggerFactory.getLogger(UniqueEmailValidator.class);

	private String name;
	private String message;

	@Autowired
	@Qualifier("userService")
	UserService userService;

	@Override
	public void initialize(final UniqueEmail constraintAnnotation) {
		this.name = constraintAnnotation.name();
		this.message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		boolean valid = true;
		try {
			String email = BeanUtils.getProperty(value, name);
			String ids = BeanUtils.getProperty(value, "id");
			if (StringUtils.isEmpty(email)) {
				return false;
			} else {
				Integer id = null;
				if (ids != null) {
					id = Integer.parseInt(ids);
				}
				return !userService.existingEmail(email, id);
			}
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			logger.error(e.getMessage(), e);
		}

		if (!valid) {
			context.buildConstraintViolationWithTemplate(message).addPropertyNode(name).addConstraintViolation()
					.disableDefaultConstraintViolation();
		}

		return valid;
	}
}