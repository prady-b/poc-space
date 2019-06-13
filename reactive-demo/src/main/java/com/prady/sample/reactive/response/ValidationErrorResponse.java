/**
 *
 */

package com.prady.sample.reactive.response;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

/**
 * @author Prady
 */
public class ValidationErrorResponse extends ErrorResponse {

    private List<ValidationErrorMessage> validationViolations;

    public ValidationErrorResponse(ValidationErrorMessage validationViolation) {
        super(ERROR_CODE.VALIDATION, "Validation");
        validationViolations = Arrays.asList(validationViolation);
    }

    /**
     * @param code
     * @param messages
     * @param validationViolations
     */
    public ValidationErrorResponse(List<ValidationErrorMessage> validationViolations) {
        super(ERROR_CODE.VALIDATION, "Validation");
        this.validationViolations = validationViolations;
    }

    /**
     * @return the validationViolations
     */
    public List<ValidationErrorMessage> getValidationViolations() {
        return validationViolations;
    }

    public static List<ValidationErrorMessage> getValidationMessages(Set<ConstraintViolation<?>> violations) {
        return violations.stream().map(v -> new ValidationErrorMessage(v.getPropertyPath(), v.getMessage())).collect(Collectors.toList());
    }

}
