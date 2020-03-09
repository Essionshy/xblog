package com.tingyu.xblog.app.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import com.tingyu.xblog.app.model.enums.Mode;

import java.time.Duration;

import static com.tingyu.xblog.app.model.support.XBlogConst.*;
import static com.tingyu.xblog.app.utils.XBlogUtils.ensureSuffix;


/**
 * XBlog configuration properties.
 *
 * @author johnniang
 */
@Data
@ConfigurationProperties("xblog")
public class XBlogProperties {

    /**
     * Doc api disabled. (Default is true)
     */
    private boolean docDisabled = true;

    /**
     * Production env. (Default is true)
     */
    private boolean productionEnv = true;

    /**
     * Authentication enabled
     */
    private boolean authEnabled = true;

    /**
     * XBlog startup mode.
     */
    private Mode mode = Mode.PRODUCTION;

    /**
     * Admin path.
     */
    private String adminPath = "admin";

    /**
     * Work directory.
     */
    private String workDir = ensureSuffix(USER_HOME, FILE_SEPARATOR) + ".xblog" + FILE_SEPARATOR;

    /**
     * XBlog backup directory.(Not recommended to modify this config);
     */
    private String backupDir = ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "xblog-backup" + FILE_SEPARATOR;

    /**
     * Upload prefix.
     */
    private String uploadUrlPrefix = "upload";

    /**
     * Download Timeout.
     */
    private Duration downloadTimeout = Duration.ofSeconds(30);

    /**
     * cache store impl
     * memory
     * level
     */
    private String cache = "memory";

}
