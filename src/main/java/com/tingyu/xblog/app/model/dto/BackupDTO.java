package com.tingyu.xblog.app.model.dto;

import lombok.Data;

/**
 * @author ryanwang
 * @date 2019-05-25
 */
@Data
public class BackupDTO {

    @Deprecated
    private String downloadUrl;

    private String downloadLink;

    private String filename;

    private Long updateTime;

    private Long fileSize;
}
