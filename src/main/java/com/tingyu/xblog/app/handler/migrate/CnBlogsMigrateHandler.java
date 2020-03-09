package com.tingyu.xblog.app.handler.migrate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.tingyu.xblog.app.model.enums.MigrateType;

/**
 * Cnblogs(https://cnblogs.com) migrate handler.
 *
 * @author ryanwang
 * @date 2019-10-30
 */
@Slf4j
@Component
public class CnBlogsMigrateHandler implements MigrateHandler {

    @Override
    public void migrate(MultipartFile file) {
        // TODO
    }

    @Override
    public boolean supportType(MigrateType type) {
        return MigrateType.CNBLOGS.equals(type);
    }
}
