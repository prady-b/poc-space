/**
 *
 */
package com.prady.sample.tx.response;

import javax.validation.Path;

/**
 * @author Prady
 *
 */
public class ValidationErrorMessage {

    private String fieldName;
    private String message;

    /**
     * @param propertyPath
     * @param message
     */
    public ValidationErrorMessage(Path field, String message) {
        fieldName = field.toString();
        this.message = message;
    }

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
