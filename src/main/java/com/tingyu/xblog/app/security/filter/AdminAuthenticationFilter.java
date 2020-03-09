package com.tingyu.xblog.app.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import com.tingyu.xblog.app.cache.StringCacheStore;
import com.tingyu.xblog.app.config.properties.XBlogProperties;
import com.tingyu.xblog.app.exception.AuthenticationException;
import com.tingyu.xblog.app.model.entity.User;
import com.tingyu.xblog.app.security.authentication.AuthenticationImpl;
import com.tingyu.xblog.app.security.context.SecurityContextHolder;
import com.tingyu.xblog.app.security.context.SecurityContextImpl;
import com.tingyu.xblog.app.security.service.OneTimeTokenService;
import com.tingyu.xblog.app.security.support.UserDetail;
import com.tingyu.xblog.app.security.util.SecurityUtils;
import com.tingyu.xblog.app.service.OptionService;
import com.tingyu.xblog.app.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.tingyu.xblog.app.model.support.XBlogConst.ADMIN_TOKEN_HEADER_NAME;
import static com.tingyu.xblog.app.model.support.XBlogConst.ADMIN_TOKEN_QUERY_NAME;

/**
 * Admin authentication filter.
 *
 * @author johnniang
 */
@Slf4j
public class AdminAuthenticationFilter extends AbstractAuthenticationFilter {

    private final XBlogProperties XBlogProperties;

    private final UserService userService;

    public AdminAuthenticationFilter(StringCacheStore cacheStore,
                                     UserService userService,
                                     XBlogProperties XBlogProperties,
                                     OptionService optionService,
                                     OneTimeTokenService oneTimeTokenService) {
        super(XBlogProperties, optionService, cacheStore, oneTimeTokenService);
        this.userService = userService;
        this.XBlogProperties = XBlogProperties;
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!XBlogProperties.isAuthEnabled()) {
            // Set security
            userService.getCurrentUser().ifPresent(user ->
                SecurityContextHolder.setContext(new SecurityContextImpl(new AuthenticationImpl(new UserDetail(user)))));

            // Do filter
            filterChain.doFilter(request, response);
            return;
        }

        // Get token from request
        String token = getTokenFromRequest(request);

        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("未登录，请登录后访问");
        }

        // Get user id from cache
        Optional<Integer> optionalUserId = cacheStore.getAny(SecurityUtils.buildTokenAccessKey(token), Integer.class);

        if (!optionalUserId.isPresent()) {
            throw new AuthenticationException("Token 已过期或不存在").setErrorData(token);
        }

        // Get the user
        User user = userService.getById(optionalUserId.get());

        // Build user detail
        UserDetail userDetail = new UserDetail(user);

        // Set security
        SecurityContextHolder.setContext(new SecurityContextImpl(new AuthenticationImpl(userDetail)));

        // Do filter
        filterChain.doFilter(request, response);
    }

    @Override
    protected String getTokenFromRequest(@NonNull HttpServletRequest request) {
        return getTokenFromRequest(request, ADMIN_TOKEN_QUERY_NAME, ADMIN_TOKEN_HEADER_NAME);
    }

}
