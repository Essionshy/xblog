package com.tingyu.xblog.app.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.tingyu.xblog.app.model.dto.base.OutputConverter;
import com.tingyu.xblog.app.model.entity.User;

import java.util.Date;

/**
 * User output dto.
 *
 * @author johnniang
 * @date 3/16/19
 */
@Data
@ToString
@EqualsAndHashCode
public class UserDTO implements OutputConverter<UserDTO, User> {

    private Integer id;

    private String username;

    private String nickname;

    private String email;

    private String avatar;

    private String description;

    private Date createTime;

    private Date updateTime;
}
