package com.tingyu.xblog.app.model.enums.converter;

import com.tingyu.xblog.app.model.enums.AttachmentType;

import javax.persistence.Converter;

/**
 * Attachment type converter
 *
 * @author johnniang
 * @date 3/27/19
 */
@Converter(autoApply = true)
public class AttachmentTypeConverter extends AbstractConverter<AttachmentType, Integer> {

    public AttachmentTypeConverter() {
        super(AttachmentType.class);
    }
}
