/**
 *
 */
package com.prady.sample.tx.response;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

/**
 * @author Prady
 *
 */
public class ValidationErrorResponse extends ErrorResponse {

    private List<ValidationErrorMessage> validationViolations;

    /**
     * @param code
     * @param messages
     * @param validationViolations
     */
    public ValidationErrorResponse(List<ValidationErrorMessage> validationViolations) {
        super(ErrorCode.VALIDATION, "Validation");
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
