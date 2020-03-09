package com.tingyu.xblog.app.model.vo;

import com.tingyu.xblog.app.model.dto.BaseMetaDTO;
import com.tingyu.xblog.app.model.dto.CategoryDTO;
import com.tingyu.xblog.app.model.dto.TagDTO;
import com.tingyu.xblog.app.model.dto.post.BasePostDetailDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


import java.util.List;
import java.util.Set;

/**
 * Post vo.
 *
 * @author johnniang
 * @author guqing
 * @date 2019-03-21
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PostDetailVO extends BasePostDetailDTO {

    private Set<Integer> tagIds;

    private List<TagDTO> tags;

    private Set<Integer> categoryIds;

    private List<CategoryDTO> categories;

    private Set<Long> postMetaIds;

    private List<BaseMetaDTO> postMetas;
}

