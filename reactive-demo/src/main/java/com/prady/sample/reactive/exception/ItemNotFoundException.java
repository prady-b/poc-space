/**
 *
 */

package com.prady.sample.reactive.exception;

import java.text.MessageFormat;

/**
 * @author Prady
 */
public class ItemNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7598017657764989747L;

    public ItemNotFoundException(String itemId, String itemType) {
        super(MessageFormat.format("{0} ''{1}'' not found", itemType, itemId));
    }
}
