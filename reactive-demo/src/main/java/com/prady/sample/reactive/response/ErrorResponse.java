/**
 *
 */

package com.prady.sample.reactive.response;

/**
 * @author Prady
 */
public class ErrorResponse {

    public enum ERROR_CODE {
        NOT_FOUND, GENERAL, VALIDATION, ALREADY_EXISTS
    }

    private ERROR_CODE code;
    private String messages;

    /**
     * @param code
     * @param messages
     */
    public ErrorResponse(ERROR_CODE code, String messages) {
        super();
        this.code = code;
        this.messages = messages;
    }

    /**
     * @return the code
     */
    public ERROR_CODE getCode() {
        return code;
    }

    /**
     * @return the messages
     */
    public String getMessages() {
        return messages;
    }

}
