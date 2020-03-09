package com.tingyu.xblog.app.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.tingyu.xblog.app.model.dto.post.BasePostSimpleDTO;

/**
 * Sheet list dto.
 *
 * @author johnniang
 * @date 19-4-24
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SheetListVO extends BasePostSimpleDTO {

    private Long commentCount;
}
