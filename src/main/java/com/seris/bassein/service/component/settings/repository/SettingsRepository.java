package com.seris.bassein.service.component.settings.repository;

import com.seris.bassein.entity.settings.Settings;
import com.seris.bassein.enums.Status;
import com.seris.bassein.service.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingsRepository extends BaseRepository<Settings> {
    List<Settings> findAllByNameAndStatus(String name, Status status);
}
