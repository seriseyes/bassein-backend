package com.seris.bassein.service.component.schedule.model;

import com.seris.bassein.entity.schedule.Schedule;
import com.seris.bassein.entity.user.Customer;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleDto {
    private List<Customer> customers;
    private Schedule schedule;
}
