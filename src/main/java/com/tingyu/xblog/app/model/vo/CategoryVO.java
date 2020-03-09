package com.tingyu.xblog.app.model.vo;

import com.tingyu.xblog.app.model.dto.CategoryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Category vo.
 *
 * @author johnniang
 * @date 3/21/19
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CategoryVO extends CategoryDTO {

    private List<CategoryVO> children;
}
