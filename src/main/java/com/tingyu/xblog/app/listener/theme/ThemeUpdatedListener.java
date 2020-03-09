package com.tingyu.xblog.app.listener.theme;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.tingyu.xblog.app.cache.StringCacheStore;
import com.tingyu.xblog.app.event.options.OptionUpdatedEvent;
import com.tingyu.xblog.app.event.theme.ThemeUpdatedEvent;
import com.tingyu.xblog.app.service.ThemeService;

/**
 * Theme updated listener.
 *
 * @author johnniang
 * @date 19-4-29
 */
@Component
public class ThemeUpdatedListener {

    private final StringCacheStore cacheStore;

    public ThemeUpdatedListener(StringCacheStore cacheStore) {
        this.cacheStore = cacheStore;
    }

    @EventListener
    public void onApplicationEvent(ThemeUpdatedEvent event) {
        cacheStore.delete(ThemeService.THEMES_CACHE_KEY);
    }

    @EventListener
    public void onOptionUpdatedEvent(OptionUpdatedEvent optionUpdatedEvent) {
        cacheStore.delete(ThemeService.THEMES_CACHE_KEY);
    }
}
