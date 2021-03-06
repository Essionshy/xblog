package com.tingyu.xblog.app.listener.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.tingyu.xblog.app.event.options.OptionUpdatedEvent;
import com.tingyu.xblog.app.event.theme.ThemeActivatedEvent;
import com.tingyu.xblog.app.event.theme.ThemeUpdatedEvent;
import com.tingyu.xblog.app.event.user.UserUpdatedEvent;
import com.tingyu.xblog.app.handler.theme.config.support.ThemeProperty;
import com.tingyu.xblog.app.model.properties.BlogProperties;
import com.tingyu.xblog.app.model.properties.SeoProperties;
import com.tingyu.xblog.app.model.support.XBlogConst;
import com.tingyu.xblog.app.service.OptionService;
import com.tingyu.xblog.app.service.ThemeService;
import com.tingyu.xblog.app.service.ThemeSettingService;
import com.tingyu.xblog.app.service.UserService;

/**
 * Freemarker config aware listener.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-20
 */
@Slf4j
@Component
public class FreemarkerConfigAwareListener {

    private final OptionService optionService;

    private final Configuration configuration;

    private final ThemeService themeService;

    private final ThemeSettingService themeSettingService;

    private final UserService userService;

    public FreemarkerConfigAwareListener(OptionService optionService,
                                         Configuration configuration,
                                         ThemeService themeService,
                                         ThemeSettingService themeSettingService,
                                         UserService userService) {
        this.optionService = optionService;
        this.configuration = configuration;
        this.themeService = themeService;
        this.themeSettingService = themeSettingService;
        this.userService = userService;
    }

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public void onApplicationStartedEvent(ApplicationStartedEvent applicationStartedEvent) throws TemplateModelException {
        log.debug("Received application started event");

        loadThemeConfig();
        loadOptionsConfig();
        loadUserConfig();
    }

    @EventListener
    public void onThemeActivatedEvent(ThemeActivatedEvent themeActivatedEvent) throws TemplateModelException {
        log.debug("Received theme activated event");

        loadThemeConfig();
    }

    @EventListener
    public void onThemeUpdatedEvent(ThemeUpdatedEvent event) throws TemplateModelException {
        log.debug("Received theme updated event");

        loadThemeConfig();
    }

    @EventListener
    public void onUserUpdate(UserUpdatedEvent event) throws TemplateModelException {
        log.debug("Received user updated event, user id: [{}]", event.getUserId());

        loadUserConfig();
    }

    @EventListener
    public void onOptionUpdate(OptionUpdatedEvent event) throws TemplateModelException {
        log.debug("Received option updated event");

        loadOptionsConfig();
        loadThemeConfig();
    }


    private void loadUserConfig() throws TemplateModelException {
        configuration.setSharedVariable("user", userService.getCurrentUser().orElse(null));
        log.debug("Loaded user");
    }

    private void loadOptionsConfig() throws TemplateModelException {

        String context = optionService.isEnabledAbsolutePath() ? optionService.getBlogBaseUrl() + "/" : "/";

        configuration.setSharedVariable("options", optionService.listOptions());
        configuration.setSharedVariable("context", context);
        configuration.setSharedVariable("version", XBlogConst.HALO_VERSION);

        configuration.setSharedVariable("blog_title", optionService.getBlogTitle());
        configuration.setSharedVariable("blog_url", optionService.getBlogBaseUrl());
        configuration.setSharedVariable("blog_logo", optionService.getByPropertyOrDefault(BlogProperties.BLOG_LOGO, String.class, BlogProperties.BLOG_LOGO.defaultValue()));
        configuration.setSharedVariable("seo_keywords", optionService.getByPropertyOrDefault(SeoProperties.KEYWORDS, String.class, SeoProperties.KEYWORDS.defaultValue()));
        configuration.setSharedVariable("seo_description", optionService.getByPropertyOrDefault(SeoProperties.DESCRIPTION, String.class, SeoProperties.DESCRIPTION.defaultValue()));

        configuration.setSharedVariable("rss_url", optionService.getBlogBaseUrl() + "/rss.xml");
        configuration.setSharedVariable("atom_url", optionService.getBlogBaseUrl() + "/atom.xml");
        configuration.setSharedVariable("sitemap_xml_url", optionService.getBlogBaseUrl() + "/sitemap.xml");
        configuration.setSharedVariable("sitemap_html_url", optionService.getBlogBaseUrl() + "/sitemap.html");
        configuration.setSharedVariable("links_url", context + optionService.getLinksPrefix());
        configuration.setSharedVariable("photos_url", context + optionService.getPhotosPrefix());
        configuration.setSharedVariable("journals_url", context + optionService.getJournalsPrefix());
        configuration.setSharedVariable("archives_url", context + optionService.getArchivesPrefix());
        configuration.setSharedVariable("categories_url", context + optionService.getCategoriesPrefix());
        configuration.setSharedVariable("tags_url", context + optionService.getTagsPrefix());

        log.debug("Loaded options");
    }

    private void loadThemeConfig() throws TemplateModelException {

        // Get current activated theme.
        ThemeProperty activatedTheme = themeService.getActivatedTheme();

        String themeBasePath = (optionService.isEnabledAbsolutePath() ? optionService.getBlogBaseUrl() : "") + "/themes/" + activatedTheme.getFolderName();

        configuration.setSharedVariable("theme", activatedTheme);

        // TODO: It will be removed in future versions
        configuration.setSharedVariable("static", themeBasePath);

        configuration.setSharedVariable("theme_base", themeBasePath);

        configuration.setSharedVariable("settings", themeSettingService.listAsMapBy(themeService.getActivatedThemeId()));
        log.debug("Loaded theme and settings");
    }
}
