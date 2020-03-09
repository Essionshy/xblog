package com.tingyu.xblog.app.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tingyu.xblog.app.model.dto.BaseMetaDTO;
import com.tingyu.xblog.app.model.dto.CategoryDTO;
import com.tingyu.xblog.app.model.dto.TagDTO;
import com.tingyu.xblog.app.model.dto.post.BasePostSimpleDTO;

import java.util.List;

/**
 * Post list vo.
 *
 * @author johnniang
 * @author guqing
 * @author ryanwang
 * @date 2019-03-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostListVO extends BasePostSimpleDTO {

    private Long commentCount;

    private List<TagDTO> tags;

    private List<CategoryDTO> categories;

    private List<BaseMetaDTO> postMetas;
}
