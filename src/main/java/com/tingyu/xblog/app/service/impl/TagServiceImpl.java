package com.tingyu.xblog.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.tingyu.xblog.app.exception.AlreadyExistsException;
import com.tingyu.xblog.app.exception.NotFoundException;
import com.tingyu.xblog.app.model.dto.TagDTO;
import com.tingyu.xblog.app.model.entity.Tag;
import com.tingyu.xblog.app.repository.TagRepository;
import com.tingyu.xblog.app.service.OptionService;
import com.tingyu.xblog.app.service.TagService;
import com.tingyu.xblog.app.service.base.AbstractCrudService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TagService implementation class.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
@Slf4j
@Service
public class TagServiceImpl extends AbstractCrudService<Tag, Integer> implements TagService {

    private final TagRepository tagRepository;

    private final OptionService optionService;

    public TagServiceImpl(TagRepository tagRepository,
                          OptionService optionService) {
        super(tagRepository);
        this.tagRepository = tagRepository;
        this.optionService = optionService;
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        // Check if the tag is exist
        long count = tagRepository.countByNameOrSlug(tag.getName(), tag.getSlug());

        log.debug("Tag count: [{}]", count);

        if (count > 0) {
            // If the tag has exist already
            throw new AlreadyExistsException("该标签已存在").setErrorData(tag);
        }

        // Get tag name
        return super.create(tag);
    }

    @Override
    public Tag getBySlugOfNonNull(String slug) {
        return tagRepository.getBySlug(slug).orElseThrow(() -> new NotFoundException("查询不到该标签的信息").setErrorData(slug));
    }

    @Override
    public Tag getBySlug(String slug) {
        return tagRepository.getBySlug(slug).orElse(null);
    }

    @Override
    public Tag getByName(String name) {
        return tagRepository.getByName(name).orElse(null);
    }

    @Override
    public TagDTO convertTo(Tag tag) {
        Assert.notNull(tag, "Tag must not be null");

        TagDTO tagDTO = new TagDTO().convertFrom(tag);

        StringBuilder fullPath = new StringBuilder();

        if (optionService.isEnabledAbsolutePath()) {
            fullPath.append(optionService.getBlogBaseUrl());
        }

        fullPath.append("/")
            .append(optionService.getTagsPrefix())
            .append("/")
            .append(tag.getSlug())
            .append(optionService.getPathSuffix());

        tagDTO.setFullPath(fullPath.toString());

        return tagDTO;
    }

    @Override
    public List<TagDTO> convertTo(List<Tag> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptyList();
        }

        return tags.stream()
            .map(this::convertTo)
            .collect(Collectors.toList());
    }
}
