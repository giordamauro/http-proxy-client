package com.http.proxy.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;
import org.hibernate.validator.internal.constraintvalidators.FutureValidatorForDate;
import org.hibernate.validator.internal.constraintvalidators.LengthValidator;
import org.hibernate.validator.internal.constraintvalidators.MaxValidatorForCharSequence;
import org.hibernate.validator.internal.constraintvalidators.MaxValidatorForNumber;
import org.hibernate.validator.internal.constraintvalidators.MinValidatorForCharSequence;
import org.hibernate.validator.internal.constraintvalidators.MinValidatorForNumber;
import org.hibernate.validator.internal.constraintvalidators.NotBlankValidator;
import org.hibernate.validator.internal.constraintvalidators.NotNullValidator;
import org.hibernate.validator.internal.constraintvalidators.PastValidatorForDate;
import org.hibernate.validator.internal.constraintvalidators.PatternValidator;
import org.hibernate.validator.internal.constraintvalidators.SizeValidatorForArray;
import org.hibernate.validator.internal.constraintvalidators.SizeValidatorForCharSequence;
import org.hibernate.validator.internal.constraintvalidators.SizeValidatorForCollection;

import com.http.proxy.ValidationHandler;
import com.http.proxy.impl.validator.NotEmptyValidator;

public class ValidationHandlerImpl implements ValidationHandler {

	private static final Validator validator;

	private static final Map<Class<? extends Annotation>, ConstraintValidator<?, ?>> validators = new HashMap<>();
	private static final Map<Class<? extends Annotation>, Map<Class<?>, ConstraintValidator<?, ?>>> specificValidators = new HashMap<>();

	static {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

		validators.put(NotNull.class, new NotNullValidator());
		validators.put(Email.class, new EmailValidator());
		validators.put(NotBlank.class, new NotBlankValidator());
		validators.put(NotEmpty.class, new NotEmptyValidator());
		validators.put(Length.class, new LengthValidator());
		validators.put(Future.class, new FutureValidatorForDate());
		validators.put(Past.class, new PastValidatorForDate());
		validators.put(Pattern.class, new PatternValidator());

		Map<Class<?>, ConstraintValidator<?, ?>> minValidators = new HashMap<>();
		minValidators.put(CharSequence.class, new MinValidatorForCharSequence());
		minValidators.put(Number.class, new MinValidatorForNumber());
		specificValidators.put(Min.class, minValidators);

		Map<Class<?>, ConstraintValidator<?, ?>> maxValidators = new HashMap<>();
		maxValidators.put(CharSequence.class, new MaxValidatorForCharSequence());
		maxValidators.put(Number.class, new MaxValidatorForNumber());
		specificValidators.put(Max.class, maxValidators);

		Map<Class<?>, ConstraintValidator<?, ?>> sizeValidators = new HashMap<>();
		maxValidators.put(Object[].class, new SizeValidatorForArray());
		maxValidators.put(Collection.class, new SizeValidatorForCollection());
		maxValidators.put(CharSequence.class, new SizeValidatorForCharSequence());
		specificValidators.put(Max.class, sizeValidators);

	}

	@Override
	public void validateMethodCall(Class<?> interfaceClass, Method method, Object[] args) throws Exception {

		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		List<String> messages = new ArrayList<>();

		for (int i = 0; i < parameterAnnotations.length; i++) {

			Annotation[] argumentAnnotations = parameterAnnotations[i];
			for (Annotation annotation : argumentAnnotations) {

				ConstraintValidator<?, ?> validatorForAnnotation = getValidatorForAnnotation(annotation);
				if (validatorForAnnotation != null) {

					validatorForAnnotation.initialize(annotation);

					Object value = args[i];
					if (!validatorForAnnotation.isValid(value, null)) {

						String field = getFieldForIndex(parameterAnnotations, i);
						String violationMessage = getAnnotationMessage(annotation);
						messages.add(String.format("Violation on argument '%s' of value '%s': %s", field, value, violationMessage));
					}
				}
			}
		}

		validateArguments(interfaceClass, method, args);
	}

	private ConstraintValidator<?, ?> getValidatorForAnnotation(Annotation annotation) {

		ConstraintValidator<?, ?> annValidator = validators.get(annotation.annotationType());

		if (annValidator == null) {
			Map<Class<?>, ConstraintValidator<?, ?>> map = specificValidators.get(annotation.annotationType());
		}

		return annValidator;
	}

	private <T extends Annotation> String getAnnotationMessage(T annotation) {

		Class<? extends Annotation> annotationType = annotation.annotationType();
		try {
			Method method = annotationType.getMethod("message");
			Object value = method.invoke(annotation);

			return String.valueOf(value);

		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private void validateArguments(Class<?> interfaceClass, Method method, Object[] args) {

		int argumentIndex = 0;
		for (Object obj : args) {

			if (obj != null) {
				Set<ConstraintViolation<Object>> objectViolations = validator.validate(obj);

				if (!objectViolations.isEmpty()) {

					List<String> messages = new ArrayList<>();

					for (ConstraintViolation<Object> violation : objectViolations) {
						messages.add(String.format("Violation on argument %s of type %s. Property '%s' value '%s': %s", argumentIndex, violation.getRootBeanClass().getName(),
								violation.getPropertyPath(), violation.getInvalidValue(), violation.getMessage()));
					}

					throwValidationException(interfaceClass, method, messages);
				}
			}

			argumentIndex++;
		}
	}

	private void throwValidationException(Class<?> interfaceClass, Method method, List<String> messages) {

		String methodName = getMethodName(method);

		throw new IllegalArgumentException(String.format("Invalid Api call to %s '%s': %s", interfaceClass, methodName, messages));
	}

	private String getMethodName(Method method) {

		String methodName = method.getName() + "(";

		Class<?>[] parameterTypes = method.getParameterTypes();

		for (int i = 0; i < parameterTypes.length; i++) {

			Class<?> parameterType = parameterTypes[i];

			String field = getFieldForIndex(method.getParameterAnnotations(), i);
			methodName += parameterType.getSimpleName() + " " + field + ", ";
		}

		if (parameterTypes.length > 0) {
			methodName = methodName.substring(0, methodName.length() - 2);
		}

		methodName += ")";

		return methodName;
	}

	private String getFieldForIndex(Annotation[][] parameterAnnotations, int index) {

		Annotation[] paramAnnotations = parameterAnnotations[index];

		String fieldName = getFieldName(paramAnnotations);
		if (fieldName == null) {
			fieldName = "arg" + index;
		}

		return fieldName;
	}

	private String getFieldName(Annotation[] paramAnnotations) {

		String fieldName = null;

		int i = 0;
		while (i < paramAnnotations.length && fieldName == null) {

			Annotation ann = paramAnnotations[i];
			if (ann.annotationType().equals(PathParam.class)) {
				PathParam pathAnn = (PathParam) ann;
				fieldName = pathAnn.value();
			} else if (ann.annotationType().equals(FormParam.class)) {
				FormParam formAnn = (FormParam) ann;
				fieldName = formAnn.value();
			} else if (ann.annotationType().equals(QueryParam.class)) {
				QueryParam queryAnn = (QueryParam) ann;
				fieldName = queryAnn.value();
			}
			i++;
		}

		return fieldName;
	}
}
