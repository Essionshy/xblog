package com.tingyu.xblog.app.repository;

import org.springframework.lang.NonNull;
import com.tingyu.xblog.app.model.entity.User;
import com.tingyu.xblog.app.repository.base.BaseRepository;

import java.util.Optional;

/**
 * User repository.
 *
 * @author johnniang
 */
public interface UserRepository extends BaseRepository<User, Integer> {

    /**
     * Gets user by username.
     *
     * @param username username must not be blank
     * @return an optional user
     */
    @NonNull
    Optional<User> findByUsername(@NonNull String username);

    /**
     * Gets user by email.
     *
     * @param email email must not be blank
     * @return an optional user
     */
    @NonNull
    Optional<User> findByEmail(@NonNull String email);
}