/**
 *
 */
package com.prady.sample.tx.exception;

import java.text.MessageFormat;

/**
 * @author Prady
 *
 */
public class InsufficientResourcesException extends RuntimeException {

    private static final long serialVersionUID = 4257657089461871369L;

    public InsufficientResourcesException(Long itemId, Integer itemUnits, String itemType) {
        super(MessageFormat.format("{0} {1} does not contains ''{2}'' items", itemType, String.valueOf(itemId),
                String.valueOf(itemUnits)));
    }

}
