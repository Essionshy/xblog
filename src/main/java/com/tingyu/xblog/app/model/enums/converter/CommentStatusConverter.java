package com.tingyu.xblog.app.model.enums.converter;

import com.tingyu.xblog.app.model.enums.CommentStatus;

import javax.persistence.Converter;

/**
 * PostComment status converter.
 *
 * @author johnniang
 * @date 3/27/19
 */
@Converter(autoApply = true)
public class CommentStatusConverter extends AbstractConverter<CommentStatus, Integer> {

    public CommentStatusConverter() {
        super(CommentStatus.class);
    }

}
