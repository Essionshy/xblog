package com.tingyu.xblog.app.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception caused by service.
 *
 * @author johnniang
 */
public class ServiceException extends XBlogException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
