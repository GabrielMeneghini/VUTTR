package com.apiRest.VUTTR.exceptions;

public class KeyFileNotFoundException extends RuntimeException {

    public KeyFileNotFoundException(String path) {
        super("Key file not found for path: " + path);
    }

}
