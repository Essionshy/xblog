package com.tingyu.xblog.app.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.tingyu.xblog.app.model.dto.base.OutputConverter;
import com.tingyu.xblog.app.model.entity.Menu;

/**
 * Menu output dto.
 *
 * @author johnniang
 * @author ryanwang
 * @date 4/3/19
 */
@Data
@EqualsAndHashCode
@ToString
public class MenuDTO implements OutputConverter<MenuDTO, Menu> {

    private Integer id;

    private String name;

    private String url;

    private Integer priority;

    private String target;

    private String icon;

    private Integer parentId;

    private String team;
}
