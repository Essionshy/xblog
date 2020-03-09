package com.tingyu.xblog.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.tingyu.xblog.app.model.dto.base.OutputConverter;
import com.tingyu.xblog.app.model.entity.Option;

/**
 * Option output dto.
 *
 * @author johnniang
 * @date 3/20/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDTO implements OutputConverter<OptionDTO, Option> {

    private String key;

    private Object value;

}
