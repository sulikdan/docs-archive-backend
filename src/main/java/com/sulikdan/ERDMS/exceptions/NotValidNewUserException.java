package com.sulikdan.ERDMS.exceptions;

/**
 * Created by Daniel Šulik on 20-Oct-20
 * <p>
 * Class NotValidNewUserException is used for .....
 */
public class NotValidNewUserException extends RuntimeException {

    private static final long serialVersionUID = -8694886029674011942L;

    public NotValidNewUserException() {
        super();
    }

    public NotValidNewUserException(String message) {
        super(message);
    }

    public NotValidNewUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
