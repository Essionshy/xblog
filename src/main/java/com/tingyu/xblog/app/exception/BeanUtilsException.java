package com.tingyu.xblog.app.exception;

import org.springframework.http.HttpStatus;

/**
 * BeanUtils exception.
 *
 * @author johnniang
 */
public class BeanUtilsException extends XBlogException {

    public BeanUtilsException(String message) {
        super(message);
    }

    public BeanUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
