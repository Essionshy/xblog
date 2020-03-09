package com.tingyu.xblog.app.controller.content.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tingyu.xblog.app.model.vo.ArchiveMonthVO;
import com.tingyu.xblog.app.model.vo.ArchiveYearVO;
import com.tingyu.xblog.app.service.PostService;

import java.util.List;

/**
 * Content archive controller.
 *
 * @author johnniang
 * @date 2019-04-02
 */
@RestController("ApiContentArchiveController")
@RequestMapping("/api/content/archives")
public class ArchiveController {

    private final PostService postService;

    public ArchiveController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("years")
    public List<ArchiveYearVO> listYearArchives() {
        return postService.listYearArchives();
    }

    @GetMapping("months")
    public List<ArchiveMonthVO> listMonthArchives() {
        return postService.listMonthArchives();
    }
}
