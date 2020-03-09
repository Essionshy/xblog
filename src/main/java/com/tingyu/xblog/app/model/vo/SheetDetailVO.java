package com.tingyu.xblog.app.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.tingyu.xblog.app.model.dto.BaseMetaDTO;
import com.tingyu.xblog.app.model.dto.post.BasePostDetailDTO;

import java.util.List;
import java.util.Set;

/**
 * Sheet detail VO.
 *
 * @author ryanwang
 * @date 2019-12-10
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SheetDetailVO extends BasePostDetailDTO {

    private Set<Long> sheetMetaIds;

    private List<BaseMetaDTO> sheetMetas;
}
