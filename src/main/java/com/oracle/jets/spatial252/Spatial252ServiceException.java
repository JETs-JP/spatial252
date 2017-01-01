package com.oracle.jets.spatial252;

import org.springframework.http.HttpStatus;

public class Spatial252ServiceException extends Spatial252Exception {

    public Spatial252ServiceException(Exception cause, HttpStatus status) {
        super(cause, status);
    }

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;

}
