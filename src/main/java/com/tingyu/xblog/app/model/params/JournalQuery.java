package com.tingyu.xblog.app.model.params;

import lombok.Data;
import com.tingyu.xblog.app.model.enums.JournalType;

/**
 * Journal query params.
 *
 * @author ryanwang
 * @date 2019/04/26
 */
@Data
public class JournalQuery {

    /**
     * Keyword.
     */
    private String keyword;

    private JournalType type;
}
