package com.tingyu.xblog.app.controller.admin.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import com.tingyu.xblog.app.model.dto.CategoryDTO;
import com.tingyu.xblog.app.model.entity.Category;
import com.tingyu.xblog.app.model.params.CategoryParam;
import com.tingyu.xblog.app.model.vo.CategoryVO;
import com.tingyu.xblog.app.service.CategoryService;
import com.tingyu.xblog.app.service.PostCategoryService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Category controller.
 *
 * @author johnniang
 * @date 2019-03-21
 */
@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    private final PostCategoryService postCategoryService;

    public CategoryController(CategoryService categoryService,
                              PostCategoryService postCategoryService) {
        this.categoryService = categoryService;
        this.postCategoryService = postCategoryService;
    }

    @GetMapping("{categoryId:\\d+}")
    @ApiOperation("Gets category detail")
    public CategoryDTO getBy(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.convertTo(categoryService.getById(categoryId));
    }

    @GetMapping
    @ApiOperation("Lists all categories")
    public List<? extends CategoryDTO> listAll(
        @SortDefault(sort = "updateTime", direction = DESC) Sort sort,
        @RequestParam(name = "more", required = false, defaultValue = "false") boolean more) {
        if (more) {
            return postCategoryService.listCategoryWithPostCountDto(sort);
        }

        return categoryService.convertTo(categoryService.listAll(sort));
    }

    @GetMapping("tree_view")
    @ApiOperation("List all categories as tree")
    public List<CategoryVO> listAsTree(@SortDefault(sort = "name", direction = ASC) Sort sort) {
        return categoryService.listAsTree(sort);
    }

    @PostMapping
    @ApiOperation("Creates category")
    public CategoryDTO createBy(@RequestBody @Valid CategoryParam categoryParam) {
        // Convert to category
        Category category = categoryParam.convertTo();

        // Save it
        return categoryService.convertTo(categoryService.create(category));
    }

    @PutMapping("{categoryId:\\d+}")
    @ApiOperation("Updates category")
    public CategoryDTO updateBy(@PathVariable("categoryId") Integer categoryId,
                                @RequestBody @Valid CategoryParam categoryParam) {
        Category categoryToUpdate = categoryService.getById(categoryId);
        categoryParam.update(categoryToUpdate);
        return categoryService.convertTo(categoryService.update(categoryToUpdate));
    }

    @DeleteMapping("{categoryId:\\d+}")
    @ApiOperation("Deletes category")
    public void deletePermanently(@PathVariable("categoryId") Integer categoryId) {
        categoryService.removeCategoryAndPostCategoryBy(categoryId);
    }
}
