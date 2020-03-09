package com.tingyu.xblog.app.security.authentication;

import org.springframework.lang.NonNull;
import com.tingyu.xblog.app.security.support.UserDetail;

/**
 * Authentication.
 *
 * @author johnniang
 */
public interface Authentication {

    /**
     * Get user detail.
     *
     * @return user detail
     */
    @NonNull
    UserDetail getDetail();
}
