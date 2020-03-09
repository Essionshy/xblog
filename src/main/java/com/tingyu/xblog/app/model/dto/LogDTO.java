package com.tingyu.xblog.app.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.tingyu.xblog.app.model.dto.base.OutputConverter;
import com.tingyu.xblog.app.model.entity.Log;
import com.tingyu.xblog.app.model.enums.LogType;

import java.util.Date;

/**
 * @author johnniang
 * @date 3/19/19
 */
@Data
@ToString
@EqualsAndHashCode
public class LogDTO implements OutputConverter<LogDTO, Log> {

    private Long id;

    private String logKey;

    private LogType type;

    private String content;

    private String ipAddress;

    private Date createTime;
}
