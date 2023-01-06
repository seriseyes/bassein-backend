package com.seris.bassein.service.component.user.repository;

import com.seris.bassein.entity.user.User;
import com.seris.bassein.service.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findFirstByUuid(String uuid);

    Optional<User> findFirstByUsername(String username);

    boolean existsByUsername(String username);
}
