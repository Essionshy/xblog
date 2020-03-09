package com.tingyu.xblog.app.service.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Halo Media type.
 *
 * @author johnniang
 * @date 19-4-18
 */
@Slf4j
public class XBlogMediaType extends MediaType {

    /**
     * Public constant media type of {@code application/zip}
     */
    public static final MediaType APPLICATION_ZIP;

    /**
     * A String equivalent of {@link XBlogMediaType#APPLICATION_ZIP}
     */
    public static final String APPLICATION_ZIP_VALUE = "application/zip";

    public static final MediaType APPLICATION_GIT;

    public static final String APPLICATION_GIT_VALUE = "application/git";


    static {
        APPLICATION_ZIP = valueOf(APPLICATION_ZIP_VALUE);
        APPLICATION_GIT = valueOf(APPLICATION_GIT_VALUE);
    }

    public XBlogMediaType(String type) {
        super(type);
    }

    public XBlogMediaType(String type, String subtype) {
        super(type, subtype);
    }

    public XBlogMediaType(String type, String subtype, Charset charset) {
        super(type, subtype, charset);
    }

    public XBlogMediaType(String type, String subtype, double qualityValue) {
        super(type, subtype, qualityValue);
    }

    public XBlogMediaType(MediaType other, Charset charset) {
        super(other, charset);
    }

    public XBlogMediaType(MediaType other, Map<String, String> parameters) {
        super(other, parameters);
    }

    public XBlogMediaType(String type, String subtype, Map<String, String> parameters) {
        super(type, subtype, parameters);
    }

}
