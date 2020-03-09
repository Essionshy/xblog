package com.tingyu.xblog.app.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import com.tingyu.xblog.app.cache.StringCacheStore;
import com.tingyu.xblog.app.config.properties.XBlogProperties;
import com.tingyu.xblog.app.exception.AuthenticationException;
import com.tingyu.xblog.app.exception.ForbiddenException;
import com.tingyu.xblog.app.model.properties.ApiProperties;
import com.tingyu.xblog.app.model.properties.CommentProperties;
import com.tingyu.xblog.app.security.service.OneTimeTokenService;
import com.tingyu.xblog.app.service.OptionService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.tingyu.xblog.app.model.support.XBlogConst.API_ACCESS_KEY_HEADER_NAME;
import static com.tingyu.xblog.app.model.support.XBlogConst.API_ACCESS_KEY_QUERY_NAME;

/**
 * Api authentication Filter
 *
 * @author johnniang
 */
@Slf4j
public class ApiAuthenticationFilter extends AbstractAuthenticationFilter {

    private final OptionService optionService;

    public ApiAuthenticationFilter(XBlogProperties XBlogProperties,
                                   OptionService optionService,
                                   StringCacheStore cacheStore,
                                   OneTimeTokenService oneTimeTokenService) {
        super(XBlogProperties, optionService, cacheStore, oneTimeTokenService);
        this.optionService = optionService;
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!XBlogProperties.isAuthEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get api_enable from option
        Boolean apiEnabled = optionService.getByPropertyOrDefault(ApiProperties.API_ENABLED, Boolean.class, false);

        if (!apiEnabled) {
            throw new ForbiddenException("API has been disabled by blogger currently");
        }

        // Get access key
        String accessKey = getTokenFromRequest(request);

        if (StringUtils.isBlank(accessKey)) {
            // If the access key is missing
            throw new AuthenticationException("Missing API access key");
        }

        // Get access key from option
        Optional<String> optionalAccessKey = optionService.getByProperty(ApiProperties.API_ACCESS_KEY, String.class);

        if (!optionalAccessKey.isPresent()) {
            // If the access key is not set
            throw new AuthenticationException("API access key hasn't been set by blogger");
        }

        if (!StringUtils.equals(accessKey, optionalAccessKey.get())) {
            // If the access key is mismatch
            throw new AuthenticationException("API access key is mismatch").setErrorData(accessKey);
        }

        // Do filter
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean result = super.shouldNotFilter(request);

        if (antPathMatcher.match("/api/content/*/comments", request.getServletPath())) {
            Boolean commentApiEnabled = optionService.getByPropertyOrDefault(CommentProperties.API_ENABLED, Boolean.class, true);
            if (!commentApiEnabled) {
                // If the comment api is disabled
                result = false;
            }
        }
        return result;
    }

    @Override
    protected String getTokenFromRequest(@NonNull HttpServletRequest request) {
        return getTokenFromRequest(request, API_ACCESS_KEY_QUERY_NAME, API_ACCESS_KEY_HEADER_NAME);
    }
}
