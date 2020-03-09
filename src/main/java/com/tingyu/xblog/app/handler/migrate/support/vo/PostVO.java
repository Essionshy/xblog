package com.tingyu.xblog.app.handler.migrate.support.vo;

import lombok.Data;
import com.tingyu.xblog.app.model.entity.BaseComment;
import com.tingyu.xblog.app.model.entity.BasePost;
import com.tingyu.xblog.app.model.entity.Category;
import com.tingyu.xblog.app.model.entity.Tag;

import java.util.List;

/**
 * @author guqing
 * @date 2020-01-18 16:52
 */
@Data
public class PostVO {
    private BasePost basePost;
    private List<Tag> tags;
    private List<Category> categories;
    private List<BaseComment> comments;
}
