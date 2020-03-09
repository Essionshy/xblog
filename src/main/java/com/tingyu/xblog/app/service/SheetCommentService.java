package com.tingyu.xblog.app.service;

import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import com.tingyu.xblog.app.model.entity.SheetComment;
import com.tingyu.xblog.app.model.vo.SheetCommentWithSheetVO;
import com.tingyu.xblog.app.service.base.BaseCommentService;

import java.util.List;

/**
 * Sheet comment service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-24
 */
public interface SheetCommentService extends BaseCommentService<SheetComment> {

    /**
     * Converts to with sheet vo
     *
     * @param comment comment
     * @return a comment with sheet vo
     */
    @NonNull
    SheetCommentWithSheetVO convertToWithSheetVo(@NonNull SheetComment comment);

    /**
     * Converts to with sheet vo
     *
     * @param sheetComments sheet comments
     * @return a sheet comments with sheet vo
     */
    @NonNull
    List<SheetCommentWithSheetVO> convertToWithSheetVo(@Nullable List<SheetComment> sheetComments);

    /**
     * Converts to with sheet vo
     *
     * @param sheetCommentPage sheet comments
     * @return a page of sheet comments with sheet vo
     */
    @NonNull
    Page<SheetCommentWithSheetVO> convertToWithSheetVo(@NonNull Page<SheetComment> sheetCommentPage);
}
