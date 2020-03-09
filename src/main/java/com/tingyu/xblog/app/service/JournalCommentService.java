package com.tingyu.xblog.app.service;

import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import com.tingyu.xblog.app.model.entity.JournalComment;
import com.tingyu.xblog.app.model.vo.JournalCommentWithJournalVO;
import com.tingyu.xblog.app.service.base.BaseCommentService;

import java.util.List;

/**
 * Journal comment service interface.
 *
 * @author johnniang
 * @date 2019-04-25
 */
public interface JournalCommentService extends BaseCommentService<JournalComment> {

    @NonNull
    List<JournalCommentWithJournalVO> convertToWithJournalVo(@Nullable List<JournalComment> journalComments);

    @NonNull
    Page<JournalCommentWithJournalVO> convertToWithJournalVo(@NonNull Page<JournalComment> journalCommentPage);
}
