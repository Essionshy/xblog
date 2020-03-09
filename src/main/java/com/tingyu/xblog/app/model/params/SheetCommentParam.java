package com.tingyu.xblog.app.model.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.tingyu.xblog.app.model.entity.SheetComment;

/**
 * Sheet comment param.
 *
 * @author johnniang
 * @date 19-4-25
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SheetCommentParam extends BaseCommentParam<SheetComment> {

}
