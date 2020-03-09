package com.tingyu.xblog.app.model.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.tingyu.xblog.app.model.entity.SheetMeta;

/**
 * Sheet meta param.
 *
 * @author ryanwang
 * @author ikaisec
 * @date 2019-08-04
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SheetMetaParam extends BaseMetaParam<SheetMeta> {
}
