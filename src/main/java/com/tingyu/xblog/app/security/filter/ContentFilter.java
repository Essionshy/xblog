package com.tingyu.xblog.app.security.filter;

import com.tingyu.xblog.app.cache.StringCacheStore;
import com.tingyu.xblog.app.config.properties.XBlogProperties;
import com.tingyu.xblog.app.security.service.OneTimeTokenService;
import com.tingyu.xblog.app.service.OptionService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Content filter
 *
 * @author johnniang
 * @date 19-5-6
 */
public class ContentFilter extends AbstractAuthenticationFilter {

    public ContentFilter(XBlogProperties XBlogProperties,
                         OptionService optionService,
                         StringCacheStore cacheStore,
                         OneTimeTokenService oneTimeTokenService) {
        super(XBlogProperties, optionService, cacheStore, oneTimeTokenService);
    }

    @Override
    protected String getTokenFromRequest(HttpServletRequest request) {
        return null;
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Do nothing
        filterChain.doFilter(request, response);
    }
}
