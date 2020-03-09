package com.tingyu.xblog.app.controller.content;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tingyu.xblog.app.config.properties.XBlogProperties;
import com.tingyu.xblog.app.exception.ServiceException;
import com.tingyu.xblog.app.model.entity.User;
import com.tingyu.xblog.app.model.properties.BlogProperties;
import com.tingyu.xblog.app.model.support.XBlogConst;
import com.tingyu.xblog.app.service.OptionService;
import com.tingyu.xblog.app.service.UserService;
import com.tingyu.xblog.app.utils.XBlogUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main controller.
 *
 * @author ryanwang
 * @date 2019-04-23
 */
@Controller
public class MainController {

    /**
     * Index redirect uri.
     */
    private final static String INDEX_REDIRECT_URI = "index.html";

    /**
     * Install redirect uri.
     */
    private final static String INSTALL_REDIRECT_URI = INDEX_REDIRECT_URI + "#install";

    private final UserService userService;

    private final OptionService optionService;

    private final XBlogProperties XBlogProperties;

    public MainController(UserService userService, OptionService optionService, XBlogProperties XBlogProperties) {
        this.userService = userService;
        this.optionService = optionService;
        this.XBlogProperties = XBlogProperties;
    }

    @GetMapping("${halo.admin-path:admin}")
    public void admin(HttpServletResponse response) throws IOException {
        String adminIndexRedirectUri = XBlogUtils.ensureBoth(XBlogProperties.getAdminPath(), XBlogUtils.URL_SEPARATOR) + INDEX_REDIRECT_URI;
        response.sendRedirect(adminIndexRedirectUri);
    }

    @GetMapping("version")
    @ResponseBody
    public String version() {
        return XBlogConst.HALO_VERSION;
    }

    @GetMapping("install")
    public void installation(HttpServletResponse response) throws IOException {
        String installRedirectUri = StringUtils.appendIfMissing(this.XBlogProperties.getAdminPath(), "/") + INSTALL_REDIRECT_URI;
        response.sendRedirect(installRedirectUri);
    }

    @GetMapping("avatar")
    public void avatar(HttpServletResponse response) throws IOException {
        User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));
        if (StringUtils.isNotEmpty(user.getAvatar())) {
            response.sendRedirect(XBlogUtils.normalizeUrl(user.getAvatar()));
        }
    }

    @GetMapping("logo")
    public void logo(HttpServletResponse response) throws IOException {
        String blogLogo = optionService.getByProperty(BlogProperties.BLOG_LOGO).orElse("").toString();
        if (StringUtils.isNotEmpty(blogLogo)) {
            response.sendRedirect(XBlogUtils.normalizeUrl(blogLogo));
        }
    }

    @GetMapping("favicon.ico")
    public void favicon(HttpServletResponse response) throws IOException {
        String favicon = optionService.getByProperty(BlogProperties.BLOG_FAVICON).orElse("").toString();
        if (StringUtils.isNotEmpty(favicon)) {
            response.sendRedirect(XBlogUtils.normalizeUrl(favicon));
        }
    }
}
