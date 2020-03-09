package com.tingyu.xblog.app.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.tingyu.xblog.app.model.dto.BaseCommentDTO;
import com.tingyu.xblog.app.model.dto.post.BasePostMinimalDTO;

/**
 * PostComment list with post vo.
 *
 * @author johnniang
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class SheetCommentWithSheetVO extends BaseCommentDTO {

    private BasePostMinimalDTO sheet;
}
