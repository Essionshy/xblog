package com.tingyu.xblog.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;
import com.tingyu.xblog.app.cache.InMemoryCacheStore;
import com.tingyu.xblog.app.cache.LevelCacheStore;
import com.tingyu.xblog.app.cache.StringCacheStore;
import com.tingyu.xblog.app.config.properties.XBlogProperties;
import com.tingyu.xblog.app.filter.CorsFilter;
import com.tingyu.xblog.app.filter.LogFilter;
import com.tingyu.xblog.app.security.filter.AdminAuthenticationFilter;
import com.tingyu.xblog.app.security.filter.ApiAuthenticationFilter;
import com.tingyu.xblog.app.security.filter.ContentFilter;
import com.tingyu.xblog.app.security.handler.ContentAuthenticationFailureHandler;
import com.tingyu.xblog.app.security.handler.DefaultAuthenticationFailureHandler;
import com.tingyu.xblog.app.security.service.OneTimeTokenService;
import com.tingyu.xblog.app.service.OptionService;
import com.tingyu.xblog.app.service.UserService;
import com.tingyu.xblog.app.utils.XBlogUtils;
import com.tingyu.xblog.app.utils.HttpClientUtils;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Halo configuration.
 *
 * @author johnniang
 */
@Configuration
@EnableConfigurationProperties(XBlogProperties.class)
@Slf4j
public class XBlogConfiguration {

    @Autowired
    XBlogProperties XBlogProperties;

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.failOnEmptyBeans(false);
        return builder.build();
    }

    @Bean
    public RestTemplate httpsRestTemplate(RestTemplateBuilder builder)
        throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestTemplate httpsRestTemplate = builder.build();
        httpsRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientUtils.createHttpsClient(
            (int) XBlogProperties.getDownloadTimeout().toMillis())));
        return httpsRestTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringCacheStore stringCacheStore() {
        StringCacheStore stringCacheStore;
        switch (XBlogProperties.getCache()) {
            case "level":
                stringCacheStore = new LevelCacheStore();
                break;

            case "memory":
            default:
                //memory or default
                stringCacheStore = new InMemoryCacheStore();
                break;

        }
        log.info("xblog cache store load impl : [{}]", stringCacheStore.getClass());
        return stringCacheStore;

    }

    /**
     * Creates a CorsFilter.
     *
     * @return Cors filter registration bean
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> corsFilter = new FilterRegistrationBean<>();

        corsFilter.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        corsFilter.setFilter(new CorsFilter());
        corsFilter.addUrlPatterns("/api/*");

        return corsFilter;
    }

    /**
     * Creates a LogFilter.
     *
     * @return Log filter registration bean
     */
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> logFilter = new FilterRegistrationBean<>();

        logFilter.setOrder(Ordered.HIGHEST_PRECEDENCE + 9);
        logFilter.setFilter(new LogFilter());
        logFilter.addUrlPatterns("/*");

        return logFilter;
    }

    @Bean
    public FilterRegistrationBean<ContentFilter> contentFilter(XBlogProperties XBlogProperties,
                                                               OptionService optionService,
                                                               StringCacheStore cacheStore,
                                                               OneTimeTokenService oneTimeTokenService) {
        ContentFilter contentFilter = new ContentFilter(XBlogProperties, optionService, cacheStore, oneTimeTokenService);
        contentFilter.setFailureHandler(new ContentAuthenticationFailureHandler());

        String adminPattern = XBlogUtils.ensureBoth(XBlogProperties.getAdminPath(), "/") + "**";

        contentFilter.addExcludeUrlPatterns(
            adminPattern,
            "/api/**",
            "/install",
            "/version",
            "/js/**",
            "/css/**");

        FilterRegistrationBean<ContentFilter> contentFrb = new FilterRegistrationBean<>();
        contentFrb.addUrlPatterns("/*");
        contentFrb.setFilter(contentFilter);
        contentFrb.setOrder(-1);

        return contentFrb;
    }

    @Bean
    public FilterRegistrationBean<ApiAuthenticationFilter> apiAuthenticationFilter(XBlogProperties XBlogProperties,
                                                                                   ObjectMapper objectMapper,
                                                                                   OptionService optionService,
                                                                                   StringCacheStore cacheStore,
                                                                                   OneTimeTokenService oneTimeTokenService) {
        ApiAuthenticationFilter apiFilter = new ApiAuthenticationFilter(XBlogProperties, optionService, cacheStore, oneTimeTokenService);
        apiFilter.addExcludeUrlPatterns(
            "/api/content/*/comments",
            "/api/content/**/comments/**",
            "/api/content/options/comment"
        );

        DefaultAuthenticationFailureHandler failureHandler = new DefaultAuthenticationFailureHandler();
        failureHandler.setProductionEnv(XBlogProperties.isProductionEnv());
        failureHandler.setObjectMapper(objectMapper);

        // Set failure handler
        apiFilter.setFailureHandler(failureHandler);

        FilterRegistrationBean<ApiAuthenticationFilter> authenticationFilter = new FilterRegistrationBean<>();
        authenticationFilter.setFilter(apiFilter);
        authenticationFilter.addUrlPatterns("/api/content/*");
        authenticationFilter.setOrder(0);

        return authenticationFilter;
    }

    @Bean
    public FilterRegistrationBean<AdminAuthenticationFilter> adminAuthenticationFilter(StringCacheStore cacheStore,
                                                                                       UserService userService,
                                                                                       XBlogProperties XBlogProperties,
                                                                                       ObjectMapper objectMapper,
                                                                                       OptionService optionService,
                                                                                       OneTimeTokenService oneTimeTokenService) {
        AdminAuthenticationFilter adminAuthenticationFilter = new AdminAuthenticationFilter(cacheStore, userService,
                XBlogProperties, optionService, oneTimeTokenService);

        DefaultAuthenticationFailureHandler failureHandler = new DefaultAuthenticationFailureHandler();
        failureHandler.setProductionEnv(XBlogProperties.isProductionEnv());
        failureHandler.setObjectMapper(objectMapper);

        // Config the admin filter
        adminAuthenticationFilter.addExcludeUrlPatterns(
            "/api/admin/login",
            "/api/admin/refresh/*",
            "/api/admin/installations",
            "/api/admin/recoveries/migrations/*",
            "/api/admin/migrations/*",
            "/api/admin/is_installed",
            "/api/admin/password/code",
            "/api/admin/password/reset"
        );
        adminAuthenticationFilter.setFailureHandler(failureHandler);

        FilterRegistrationBean<AdminAuthenticationFilter> authenticationFilter = new FilterRegistrationBean<>();
        authenticationFilter.setFilter(adminAuthenticationFilter);
        authenticationFilter.addUrlPatterns("/api/admin/*", "/api/content/comments");
        authenticationFilter.setOrder(1);

        return authenticationFilter;
    }
}
