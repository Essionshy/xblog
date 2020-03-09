package com.tingyu.xblog.app.model.vo;

import com.tingyu.xblog.app.model.dto.BaseCommentDTO;
import com.tingyu.xblog.app.model.dto.post.BasePostMinimalDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * PostComment list with post vo.
 *
 * @author johnniang
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class PostCommentWithPostVO extends BaseCommentDTO {

    private BasePostMinimalDTO post;
}
