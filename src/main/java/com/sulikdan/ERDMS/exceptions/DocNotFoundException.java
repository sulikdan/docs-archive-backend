package com.sulikdan.ERDMS.exceptions;

/**
 * Created by Daniel Šulik on 23-Oct-20
 * <p>
 * Class DocNotFoundException is used in case Doc not found or when somebody requests document, but he has no rights,
 * therefore, nothing there to return.
 */
public class DocNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2191665524385662236L;

    public DocNotFoundException() {
        super();
    }

    public DocNotFoundException(String message) {
        super(message);
    }

    public DocNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
