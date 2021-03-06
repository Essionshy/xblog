package com.tingyu.xblog.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.tingyu.xblog.app.model.entity.CommentBlackList;
import com.tingyu.xblog.app.model.enums.CommentViolationTypeEnum;
import com.tingyu.xblog.app.model.properties.CommentProperties;
import com.tingyu.xblog.app.repository.CommentBlackListRepository;
import com.tingyu.xblog.app.repository.PostCommentRepository;
import com.tingyu.xblog.app.service.CommentBlackListService;
import com.tingyu.xblog.app.service.OptionService;
import com.tingyu.xblog.app.service.base.AbstractCrudService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * Comment BlackList Service Implements
 *
 * @author Lei XinXin
 * @date 2020/1/3
 */

@Service
@Slf4j
public class CommentBlackListServiceImpl extends AbstractCrudService<CommentBlackList, Long> implements CommentBlackListService {
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");
    private final CommentBlackListRepository commentBlackListRepository;
    private final PostCommentRepository postCommentRepository;
    private final OptionService optionService;


    public CommentBlackListServiceImpl(CommentBlackListRepository commentBlackListRepository, PostCommentRepository postCommentRepository, OptionService optionService) {
        super(commentBlackListRepository);
        this.commentBlackListRepository = commentBlackListRepository;
        this.postCommentRepository = postCommentRepository;
        this.optionService = optionService;
    }

    @Override
    public CommentViolationTypeEnum commentsBanStatus(String ipAddress) {
        /*
        N=后期可配置
        1. 获取评论次数；
        2. 判断N分钟内，是否超过规定的次数限制，超过后需要每隔N分钟才能再次评论；
        3. 如果在时隔N分钟内，还有多次评论，可被认定为恶意攻击者；
        4. 对恶意攻击者进行N分钟的封禁；
        */
        Optional<CommentBlackList> blackList = commentBlackListRepository.findByIpAddress(ipAddress);
        LocalDateTime now = LocalDateTime.now();
        Date endTime = new Date(now.atZone(ZONE_ID).toInstant().toEpochMilli());
        Integer banTime = optionService.getByPropertyOrDefault(CommentProperties.COMMENT_BAN_TIME, Integer.class, 10);
        Date startTime = new Date(now.minusMinutes(banTime)
            .atZone(ZONE_ID).toInstant().toEpochMilli());
        Integer range = optionService.getByPropertyOrDefault(CommentProperties.COMMENT_RANGE, Integer.class, 30);
        boolean isPresent = postCommentRepository.countByIpAndTime(ipAddress, startTime, endTime) >= range;
        if (isPresent && blackList.isPresent()) {
            update(now, blackList.get(), banTime);
            return CommentViolationTypeEnum.FREQUENTLY;
        } else if (isPresent) {
            CommentBlackList commentBlackList = CommentBlackList
                .builder()
                .banTime(getBanTime(now, banTime))
                .ipAddress(ipAddress)
                .build();
            super.create(commentBlackList);
            return CommentViolationTypeEnum.FREQUENTLY;
        }
        return CommentViolationTypeEnum.NORMAL;
    }

    private void update(LocalDateTime localDateTime, CommentBlackList blackList, Integer banTime) {
        blackList.setBanTime(getBanTime(localDateTime, banTime));
        int updateResult = commentBlackListRepository.updateByIpAddress(blackList);
        Optional.of(updateResult)
            .filter(result -> result <= 0).ifPresent(result -> log.error("更新评论封禁时间失败"));
    }

    private Date getBanTime(LocalDateTime localDateTime, Integer banTime) {
        return new Date(localDateTime.plusMinutes(banTime).atZone(ZONE_ID).toInstant().toEpochMilli());
    }
}
