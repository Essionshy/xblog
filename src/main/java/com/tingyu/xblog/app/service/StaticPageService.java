package com.tingyu.xblog.app.service;

import com.tingyu.xblog.app.model.support.StaticPageFile;

import java.nio.file.Path;
import java.util.List;

import static com.tingyu.xblog.app.model.support.XBlogConst.FILE_SEPARATOR;
import static com.tingyu.xblog.app.model.support.XBlogConst.TEMP_DIR;
import static com.tingyu.xblog.app.utils.XBlogUtils.ensureSuffix;

/**
 * Static Page service interface.
 *
 * @author ryanwang
 * @date 2019-12-25
 */
public interface StaticPageService {

    /**
     * Static page folder location.
     */
    String PAGES_FOLDER = "static_pages";


    String STATIC_PAGE_PACK_DIR = ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "static-pages-pack" + FILE_SEPARATOR;

    String[] USELESS_FILE_SUFFIX = {"ftl", "md", "yaml", "yml", "gitignore"};

    /**
     * Generate pages.
     */
    void generate();

    /**
     * Deploy static pages.
     */
    void deploy();

    /**
     * Zip static pages directory.
     *
     * @return zip path
     */
    Path zipStaticPagesDirectory();

    /**
     * List file of generated static page.
     *
     * @return a list of generated static page.
     */
    List<StaticPageFile> listFile();
}
