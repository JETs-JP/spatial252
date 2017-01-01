package com.oracle.jets.spatial252;

import org.springframework.http.HttpStatus;

public class Spatial252Exception extends Exception {

    private final HttpStatus status;

    public Spatial252Exception(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 1L;


}
