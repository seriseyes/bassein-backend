package com.seris.bassein.service.component.user.repository;

import com.seris.bassein.entity.user.Bassein;
import com.seris.bassein.service.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasseinRepository extends BaseRepository<Bassein> {
    Optional<Bassein> findFirstBy();
}
