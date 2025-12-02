package com.apiRest.VUTTR.exceptions;

import java.time.Instant;

public record ErrorResponse(String timeStamp,
                            int status,
                            String error,
                            Object message,
                            String path) {

    public ErrorResponse(int status, String error, Object message, String path) {
        this(Instant.now().toString(), status, error, message, path);
    }

}
