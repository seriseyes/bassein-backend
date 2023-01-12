package com.seris.bassein.service.component.schedule.repository;

import com.seris.bassein.entity.schedule.TimeTable;
import com.seris.bassein.enums.Status;
import com.seris.bassein.service.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeTableRepository extends BaseRepository<TimeTable> {
    List<TimeTable> findAllByTimeAndStatusAndCreatedBetween(String time, Status status, LocalDateTime createdFrom, LocalDateTime createdTo);
}
