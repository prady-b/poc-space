/**
 *
 */

package com.prady.sample.reactive.exception;

import java.text.MessageFormat;

/**
 * @author Prady
 */
public class ItemAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -5355053606481348640L;

    public ItemAlreadyExistsException(String itemId, String itemType) {
        super(MessageFormat.format("{0} ''{1}'' already exists", itemType, itemId));
    }
}
