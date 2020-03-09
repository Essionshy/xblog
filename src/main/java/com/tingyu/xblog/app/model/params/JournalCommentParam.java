package com.tingyu.xblog.app.model.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.tingyu.xblog.app.model.entity.JournalComment;

/**
 * Journal comment param.
 *
 * @author johnniang
 * @date 3/22/19
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class JournalCommentParam extends BaseCommentParam<JournalComment> {

}
