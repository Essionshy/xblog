package com.tingyu.xblog.app.service.impl;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.tingyu.xblog.app.exception.BadRequestException;
import com.tingyu.xblog.app.exception.ForbiddenException;
import com.tingyu.xblog.app.exception.NotFoundException;
import com.tingyu.xblog.app.model.dto.post.BasePostMinimalDTO;
import com.tingyu.xblog.app.model.entity.Post;
import com.tingyu.xblog.app.model.entity.PostComment;
import com.tingyu.xblog.app.model.enums.CommentViolationTypeEnum;
import com.tingyu.xblog.app.model.enums.PostPermalinkType;
import com.tingyu.xblog.app.model.properties.CommentProperties;
import com.tingyu.xblog.app.model.vo.PostCommentWithPostVO;
import com.tingyu.xblog.app.repository.PostCommentRepository;
import com.tingyu.xblog.app.repository.PostRepository;
import com.tingyu.xblog.app.service.CommentBlackListService;
import com.tingyu.xblog.app.service.OptionService;
import com.tingyu.xblog.app.service.PostCommentService;
import com.tingyu.xblog.app.service.UserService;
import com.tingyu.xblog.app.utils.ServiceUtils;
import com.tingyu.xblog.app.utils.ServletUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * PostCommentService implementation class
 *
 * @author ryanwang
 * @author johnniang
 * @date 2019-03-14
 */
@Slf4j
@Service
public class PostCommentServiceImpl extends BaseCommentServiceImpl<PostComment> implements PostCommentService {

    private final PostRepository postRepository;

    private final CommentBlackListService commentBlackListService;

    public PostCommentServiceImpl(PostCommentRepository postCommentRepository,
                                  PostRepository postRepository,
                                  UserService userService,
                                  OptionService optionService,
                                  CommentBlackListService commentBlackListService,
                                  ApplicationEventPublisher eventPublisher) {
        super(postCommentRepository, optionService, userService, eventPublisher);
        this.postRepository = postRepository;
        this.commentBlackListService = commentBlackListService;
    }

    @Override
    public Page<PostCommentWithPostVO> convertToWithPostVo(Page<PostComment> commentPage) {
        Assert.notNull(commentPage, "PostComment page must not be null");

        return new PageImpl<>(convertToWithPostVo(commentPage.getContent()), commentPage.getPageable(), commentPage.getTotalElements());

    }

    @Override
    public PostCommentWithPostVO convertToWithPostVo(PostComment comment) {
        Assert.notNull(comment, "PostComment must not be null");
        PostCommentWithPostVO postCommentWithPostVO = new PostCommentWithPostVO().convertFrom(comment);

        BasePostMinimalDTO basePostMinimalDTO = new BasePostMinimalDTO().convertFrom(postRepository.getOne(comment.getPostId()));

        postCommentWithPostVO.setPost(buildPostFullPath(basePostMinimalDTO));
        return postCommentWithPostVO;
    }

    @Override
    public List<PostCommentWithPostVO> convertToWithPostVo(List<PostComment> postComments) {
        if (CollectionUtils.isEmpty(postComments)) {
            return Collections.emptyList();
        }

        // Fetch goods ids
        Set<Integer> postIds = ServiceUtils.fetchProperty(postComments, PostComment::getPostId);

        // Get all posts
        Map<Integer, Post> postMap = ServiceUtils.convertToMap(postRepository.findAllById(postIds), Post::getId);

        return postComments.stream()
            .filter(comment -> postMap.containsKey(comment.getPostId()))
            .map(comment -> {
                // Convert to vo
                PostCommentWithPostVO postCommentWithPostVO = new PostCommentWithPostVO().convertFrom(comment);

                BasePostMinimalDTO basePostMinimalDTO = new BasePostMinimalDTO().convertFrom(postMap.get(comment.getPostId()));

                postCommentWithPostVO.setPost(buildPostFullPath(basePostMinimalDTO));

                return postCommentWithPostVO;
            }).collect(Collectors.toList());
    }

    private BasePostMinimalDTO buildPostFullPath(BasePostMinimalDTO basePostMinimalDTO) {
        PostPermalinkType permalinkType = optionService.getPostPermalinkType();

        String pathSuffix = optionService.getPathSuffix();

        String archivesPrefix = optionService.getArchivesPrefix();

        StringBuilder fullPath = new StringBuilder();

        if (optionService.isEnabledAbsolutePath()) {
            fullPath.append(optionService.getBlogBaseUrl());
        }

        fullPath.append("/");

        if (permalinkType.equals(PostPermalinkType.DEFAULT)) {
            fullPath.append(archivesPrefix)
                .append("/")
                .append(basePostMinimalDTO.getSlug())
                .append(pathSuffix);
        } else if (permalinkType.equals(PostPermalinkType.ID)) {
            fullPath.append("?p=")
                .append(basePostMinimalDTO.getId());
        } else if (permalinkType.equals(PostPermalinkType.DATE)) {
            fullPath.append(DateUtil.year(basePostMinimalDTO.getCreateTime()))
                .append("/")
                .append(DateUtil.month(basePostMinimalDTO.getCreateTime()) + 1)
                .append("/")
                .append(basePostMinimalDTO.getSlug())
                .append(pathSuffix);
        } else if (permalinkType.equals(PostPermalinkType.DAY)) {
            fullPath.append(DateUtil.year(basePostMinimalDTO.getCreateTime()))
                .append("/")
                .append(DateUtil.month(basePostMinimalDTO.getCreateTime()) + 1)
                .append("/")
                .append(DateUtil.dayOfMonth(basePostMinimalDTO.getCreateTime()))
                .append("/")
                .append(basePostMinimalDTO.getSlug())
                .append(pathSuffix);
        }

        basePostMinimalDTO.setFullPath(fullPath.toString());

        return basePostMinimalDTO;
    }

    @Override
    public void validateTarget(Integer postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new NotFoundException("查询不到该文章的信息").setErrorData(postId));

        if (post.getDisallowComment()) {
            throw new BadRequestException("该文章已经被禁止评论").setErrorData(postId);
        }
    }

    @Override
    public void validateCommentBlackListStatus() {
        CommentViolationTypeEnum banStatus = commentBlackListService.commentsBanStatus(ServletUtils.getRequestIp());
        Integer banTime = optionService.getByPropertyOrDefault(CommentProperties.COMMENT_BAN_TIME, Integer.class, 10);
        if (banStatus == CommentViolationTypeEnum.FREQUENTLY) {
            throw new ForbiddenException(String.format("您的评论过于频繁，请%s分钟之后再试。", banTime));
        }
    }

}
