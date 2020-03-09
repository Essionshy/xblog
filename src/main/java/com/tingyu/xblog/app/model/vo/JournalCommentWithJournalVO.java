package com.tingyu.xblog.app.model.vo;

import com.tingyu.xblog.app.model.dto.BaseCommentDTO;
import com.tingyu.xblog.app.model.dto.JournalDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Journal comment with journal vo.
 *
 * @author johnniang
 * @date 19-4-25
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class JournalCommentWithJournalVO extends BaseCommentDTO {

    private JournalDTO journal;
}
