package com.tingyu.xblog.app.model.dto;

import com.tingyu.xblog.app.model.dto.base.OutputConverter;
import lombok.Data;

/**
 * Statistic with user info DTO.
 *
 * @author ryanwang
 * @date 2019-12-16
 */
@Data
public class StatisticWithUserDTO extends StatisticDTO implements OutputConverter<StatisticWithUserDTO, StatisticDTO> {

    private UserDTO user;
}
