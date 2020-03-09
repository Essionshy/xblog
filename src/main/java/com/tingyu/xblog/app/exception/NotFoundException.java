package com.tingyu.xblog.app.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception of entity not found.
 *
 * @author johnniang
 */
public class NotFoundException extends XBlogException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
