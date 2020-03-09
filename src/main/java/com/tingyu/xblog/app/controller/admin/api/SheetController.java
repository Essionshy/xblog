package com.tingyu.xblog.app.controller.admin.api;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import com.tingyu.xblog.app.cache.StringCacheStore;
import com.tingyu.xblog.app.model.dto.InternalSheetDTO;
import com.tingyu.xblog.app.model.dto.post.BasePostMinimalDTO;
import com.tingyu.xblog.app.model.entity.Sheet;
import com.tingyu.xblog.app.model.enums.PostStatus;
import com.tingyu.xblog.app.model.params.SheetParam;
import com.tingyu.xblog.app.model.vo.SheetDetailVO;
import com.tingyu.xblog.app.model.vo.SheetListVO;
import com.tingyu.xblog.app.service.OptionService;
import com.tingyu.xblog.app.service.SheetService;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Sheet controller.
 *
 * @author johnniang
 * @author ryanwang
 * @date 19-4-24
 */
@RestController
@RequestMapping("/api/admin/sheets")
public class SheetController {

    private final SheetService sheetService;

    private final StringCacheStore cacheStore;

    private final OptionService optionService;

    public SheetController(SheetService sheetService,
                           StringCacheStore cacheStore,
                           OptionService optionService) {
        this.sheetService = sheetService;
        this.cacheStore = cacheStore;
        this.optionService = optionService;
    }

    @GetMapping("{sheetId:\\d+}")
    @ApiOperation("Gets a sheet")
    public SheetDetailVO getBy(@PathVariable("sheetId") Integer sheetId) {
        Sheet sheet = sheetService.getById(sheetId);
        return sheetService.convertToDetailVo(sheet);
    }

    @GetMapping
    @ApiOperation("Gets a page of sheet")
    public Page<SheetListVO> pageBy(@PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
        Page<Sheet> sheetPage = sheetService.pageBy(pageable);
        return sheetService.convertToListVo(sheetPage);
    }

    @GetMapping("internal")
    @ApiOperation("Lists internal sheets")
    public List<InternalSheetDTO> internalSheets() {
        return sheetService.listInternal();
    }

    @PostMapping
    @ApiOperation("Creates a sheet")
    public SheetDetailVO createBy(@RequestBody @Valid SheetParam sheetParam,
                                  @RequestParam(value = "autoSave", required = false, defaultValue = "false") Boolean autoSave) {
        Sheet sheet = sheetService.createBy(sheetParam.convertTo(), sheetParam.getSheetMetas(), autoSave);
        return sheetService.convertToDetailVo(sheet);
    }

    @PutMapping("{sheetId:\\d+}")
    @ApiOperation("Updates a sheet")
    public SheetDetailVO updateBy(
        @PathVariable("sheetId") Integer sheetId,
        @RequestBody @Valid SheetParam sheetParam,
        @RequestParam(value = "autoSave", required = false, defaultValue = "false") Boolean autoSave) {
        Sheet sheetToUpdate = sheetService.getById(sheetId);

        sheetParam.update(sheetToUpdate);

        Sheet sheet = sheetService.updateBy(sheetToUpdate, sheetParam.getSheetMetas(), autoSave);

        return sheetService.convertToDetailVo(sheet);
    }

    @PutMapping("{sheetId:\\d+}/{status}")
    @ApiOperation("Updates a sheet")
    public void updateStatusBy(
        @PathVariable("sheetId") Integer sheetId,
        @PathVariable("status") PostStatus status) {
        Sheet sheet = sheetService.getById(sheetId);

        // Set status
        sheet.setStatus(status);

        // Update
        sheetService.update(sheet);
    }

    @DeleteMapping("{sheetId:\\d+}")
    @ApiOperation("Deletes a sheet")
    public SheetDetailVO deleteBy(@PathVariable("sheetId") Integer sheetId) {
        Sheet sheet = sheetService.removeById(sheetId);
        return sheetService.convertToDetailVo(sheet);
    }

    @GetMapping("preview/{sheetId:\\d+}")
    @ApiOperation("Gets a sheet preview link")
    public String preview(@PathVariable("sheetId") Integer sheetId) throws UnsupportedEncodingException {
        Sheet sheet = sheetService.getById(sheetId);

        sheet.setSlug(URLEncoder.encode(sheet.getSlug(), StandardCharsets.UTF_8.name()));

        BasePostMinimalDTO sheetMinimalDTO = sheetService.convertToMinimal(sheet);

        String token = IdUtil.simpleUUID();

        // cache preview token
        cacheStore.putAny(token, token, 10, TimeUnit.MINUTES);

        StringBuilder previewUrl = new StringBuilder();

        if (!optionService.isEnabledAbsolutePath()) {
            previewUrl.append(optionService.getBlogBaseUrl());
        }

        previewUrl.append(sheetMinimalDTO.getFullPath())
            .append("?token=")
            .append(token);

        // build preview post url and return
        return previewUrl.toString();
    }
}