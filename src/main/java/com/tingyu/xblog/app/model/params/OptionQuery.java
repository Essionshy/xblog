package com.tingyu.xblog.app.model.params;

import lombok.Data;
import com.tingyu.xblog.app.model.enums.OptionType;

/**
 * Option query params.
 *
 * @author ryanwang
 * @date 2019-12-02
 */
@Data
public class OptionQuery {

    private String keyword;

    private OptionType type;
}
