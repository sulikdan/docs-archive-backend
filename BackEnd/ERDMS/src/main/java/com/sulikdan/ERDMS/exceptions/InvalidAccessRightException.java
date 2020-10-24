package com.sulikdan.ERDMS.exceptions;

/**
 * Created by Daniel Šulik on 22-Oct-20
 * <p>
 * Class InvalidAccessRightException is used for invalid access from user to actions that are not under his rights.
 * As getting files, modifying or deleting them.
 */
public class InvalidAccessRightException extends RuntimeException {

    private static final long serialVersionUID = 5093016701353385170L;

    public InvalidAccessRightException() {
        super();
    }

    public InvalidAccessRightException(String message) {
        super(message);
    }

    public InvalidAccessRightException(String message, Throwable cause) {
        super(message, cause);
    }
}
