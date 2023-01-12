package com.seris.bassein.service.component.schedule.repository;

import com.seris.bassein.entity.schedule.Schedule;
import com.seris.bassein.service.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends BaseRepository<Schedule> {
    List<Schedule> findAllByCustomer_RegNo(String customerRegNo);

    List<Schedule> findAllByCustomer_RegNoOrCustomer_Phone(String customerRegNo, String customerPhone);
}
