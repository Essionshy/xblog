package com.tingyu.xblog.app.handler.staticdeploy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.tingyu.xblog.app.model.enums.StaticDeployType;
import com.tingyu.xblog.app.service.OptionService;

/**
 * Git deploy handler.
 *
 * @author ryanwang
 * @date 2019-12-26
 */
@Slf4j
@Component
public class GitStaticDeployHandler implements StaticDeployHandler {

    private final OptionService optionService;

    public GitStaticDeployHandler(OptionService optionService) {
        this.optionService = optionService;
    }

    @Override
    public void deploy() {

    }

    @Override
    public boolean supportType(StaticDeployType type) {
        return StaticDeployType.GIT.equals(type);
    }
}
