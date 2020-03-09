package com.tingyu.xblog.app.listener.post;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.tingyu.xblog.app.event.post.PostVisitEvent;
import com.tingyu.xblog.app.service.PostService;

/**
 * Visit event listener.
 *
 * @author johnniang
 * @date 19-4-22
 */
@Component
public class PostVisitEventListener extends AbstractVisitEventListener {

    public PostVisitEventListener(PostService postService) {
        super(postService);
    }

    @Async
    @EventListener
    public void onPostVisitEvent(PostVisitEvent event) throws InterruptedException {
        handleVisitEvent(event);
    }
}
