package com.tingyu.xblog.app.service;

import org.springframework.lang.NonNull;
import com.tingyu.xblog.app.model.dto.EnvironmentDTO;
import com.tingyu.xblog.app.model.dto.StatisticDTO;
import com.tingyu.xblog.app.model.params.LoginParam;
import com.tingyu.xblog.app.model.params.ResetPasswordParam;
import com.tingyu.xblog.app.security.token.AuthToken;

/**
 * Admin service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-29
 */
public interface AdminService {

    /**
     * Expired seconds.
     */
    int ACCESS_TOKEN_EXPIRED_SECONDS = 24 * 3600;

    int REFRESH_TOKEN_EXPIRED_DAYS = 30;

    String APPLICATION_CONFIG_NAME = "application.yaml";

    String LOG_PATH = "logs/spring.log";

    /**
     * Authenticates.
     *
     * @param loginParam login param must not be null
     * @return authentication token
     */
    @NonNull
    AuthToken authenticate(@NonNull LoginParam loginParam);

    /**
     * Clears authentication.
     */
    void clearToken();

    /**
     * Send reset password code to administrator's email.
     *
     * @param param param must not be null
     */
    void sendResetPasswordCode(@NonNull ResetPasswordParam param);

    /**
     * Reset password by code.
     *
     * @param param param must not be null
     */
    void resetPasswordByCode(@NonNull ResetPasswordParam param);

    /**
     * Get system counts.
     *
     * @return count dto
     */
    @NonNull
    @Deprecated
    StatisticDTO getCount();

    /**
     * Get system environments
     *
     * @return environments
     */
    @NonNull
    EnvironmentDTO getEnvironments();

    /**
     * Refreshes token.
     *
     * @param refreshToken refresh token must not be blank
     * @return authentication token
     */
    @NonNull
    AuthToken refreshToken(@NonNull String refreshToken);

    /**
     * Updates halo admin assets.
     */
    void updateAdminAssets();

    /**
     * Get application.yaml content.
     *
     * @return application.yaml content
     */
    String getApplicationConfig();

    /**
     * Save application.yaml content.
     *
     * @param content new content
     */
    void updateApplicationConfig(@NonNull String content);

    /**
     * Get halo logs content.
     *
     * @param lines lines
     * @return logs content.
     */
    String getLogFiles(@NonNull Long lines);
}
