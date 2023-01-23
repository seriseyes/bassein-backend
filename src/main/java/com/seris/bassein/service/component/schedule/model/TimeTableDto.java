package com.seris.bassein.service.component.schedule.model;

import com.seris.bassein.entity.schedule.TimeTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeTableDto {
    private Long id;
    private String fullname;
    private int age;
    private String gender;
    private String type;
    private String schedule;
    private String teacher;
    private String targetTime;
    private String day;
    private String locker;
    private TimeTable timeTable;
}
