package com.tingyu.xblog.app.model.dto;

import lombok.Data;
import com.tingyu.xblog.app.model.dto.base.OutputConverter;
import com.tingyu.xblog.app.model.entity.Photo;

import java.util.Date;

/**
 * @author ryanwang
 * @date 2019-03-21
 */
@Data
public class PhotoDTO implements OutputConverter<PhotoDTO, Photo> {

    private Integer id;

    private String name;

    private String thumbnail;

    private Date takeTime;

    private String url;

    private String team;

    private String location;

    private String description;
}
