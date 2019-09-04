/**
 *
 */

package com.prady.sample.reactive.response;

/**
 * @author Prady
 */
public class ErrorResponse {

    public enum ErrorCode {
        NOT_FOUND, GENERAL, VALIDATION, ALREADY_EXISTS
    }

    private final ErrorCode code;
    private final String messages;

    /**
     * @param code
     * @param messages
     */
    public ErrorResponse(ErrorCode code, String messages) {
        super();
        this.code = code;
        this.messages = messages;
    }

    /**
     * @return the code
     */
    public ErrorCode getCode() {
        return code;
    }

    /**
     * @return the messages
     */
    public String getMessages() {
        return messages;
    }

}
