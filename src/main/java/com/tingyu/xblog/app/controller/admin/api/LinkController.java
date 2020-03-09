package com.tingyu.xblog.app.controller.admin.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import com.tingyu.xblog.app.model.dto.LinkDTO;
import com.tingyu.xblog.app.model.entity.Link;
import com.tingyu.xblog.app.model.params.LinkParam;
import com.tingyu.xblog.app.service.LinkService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Link Controller
 *
 * @author ryanwang
 * @date 2019-03-21
 */
@RestController
@RequestMapping("/api/admin/links")
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    @ApiOperation("Lists links")
    public List<LinkDTO> listLinks(@SortDefault(sort = "team", direction = DESC) Sort sort) {
        return linkService.listDtos(sort.and(Sort.by(ASC, "priority")));
    }

    @GetMapping("{id:\\d+}")
    @ApiOperation("Gets link detail by id")
    public LinkDTO getBy(@PathVariable("id") Integer id) {
        return new LinkDTO().convertFrom(linkService.getById(id));
    }

    @GetMapping("parse")
    @ApiOperation("Gets link by parse url")
    public LinkDTO getByParse(@RequestParam("url") String url) {
        return linkService.getByParse(url);
    }

    @PostMapping
    @ApiOperation("Creates a link")
    public LinkDTO createBy(@RequestBody @Valid LinkParam linkParam) {
        Link link = linkService.createBy(linkParam);
        return new LinkDTO().convertFrom(link);
    }

    @PutMapping("{id:\\d+}")
    @ApiOperation("Updates a link")
    public LinkDTO updateBy(@PathVariable("id") Integer id,
                            @RequestBody @Valid LinkParam linkParam) {
        Link link = linkService.getById(id);
        linkParam.update(link);
        return new LinkDTO().convertFrom(linkService.update(link));
    }

    @DeleteMapping("{id:\\d+}")
    @ApiOperation("Delete link by id")
    public void deletePermanently(@PathVariable("id") Integer id) {
        linkService.removeById(id);
    }

    @GetMapping("teams")
    @ApiOperation("Lists all link teams")
    public List<String> teams() {
        return linkService.listAllTeams();
    }
}
