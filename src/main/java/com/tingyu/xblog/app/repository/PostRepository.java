package com.tingyu.xblog.app.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.tingyu.xblog.app.model.entity.Post;
import com.tingyu.xblog.app.model.enums.PostStatus;
import com.tingyu.xblog.app.repository.base.BasePostRepository;

import java.util.Optional;


/**
 * Post repository.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-19
 */
public interface PostRepository extends BasePostRepository<Post>, JpaSpecificationExecutor<Post> {

    /**
     * Count all post visits.
     *
     * @return visits.
     */
    @Override
    @Query("select sum(p.visits) from Post p")
    Long countVisit();

    /**
     * Count all post likes.
     *
     * @return likes.
     */
    @Override
    @Query("select sum(p.likes) from Post p")
    Long countLike();

    /**
     * Find by post year and month and slug.
     *
     * @param year  post create year
     * @param month post create month
     * @param slug  post slug
     * @return a optional of post
     */
    @Query("select post from Post post where DateUtil.year(post.createTime) = :year and DateUtil.month(post.createTime) = :month and post.slug = :slug")
    Optional<Post> findBy(@Param("year") Integer year, @Param("month") Integer month, @Param("slug") String slug);

    /**
     * Find by post year and month and slug and status.
     *
     * @param year   post create year
     * @param month  post create month
     * @param slug   post slug
     * @param status post status
     * @return a optional of post
     */
    @Query("select post from Post post where DateUtil.year(post.createTime) = :year and DateUtil.month(post.createTime) = :month and post.slug = :slug and post.status = :status")
    Optional<Post> findBy(@Param("year") Integer year, @Param("month") Integer month, @Param("slug") String slug, @Param("status") PostStatus status);

    /**
     * Find by post year and month and day and slug.
     *
     * @param year  post create year
     * @param month post create month
     * @param day   post create day
     * @param slug  post slug
     * @return a optional of post
     */
    @Query("select post from Post post where DateUtil.year(post.createTime) = :year and DateUtil.month(post.createTime) = :month and DateUtil.dayOfMonth(post.createTime) = :day and post.slug = :slug")
    Optional<Post> findBy(@Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day, @Param("slug") String slug);

    /**
     * Find by post year and month and day and slug and status.
     *
     * @param year   post create year
     * @param month  post create month
     * @param day    post create day
     * @param slug   post slug
     * @param status post status
     * @return a optional of post
     */
    @Query("select post from Post post where DateUtil.year(post.createTime) = :year and DateUtil.month(post.createTime) = :month and DateUtil.dayOfMonth(post.createTime) = :day and post.slug = :slug and post.status = :status")
    Optional<Post> findBy(@Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day, @Param("slug") String slug, @Param("status") PostStatus status);
}
