package com.seris.bassein.service.component.schedule.model;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleWrapper {
    private int day;
    private String dayName;
    private boolean exist;
    private List<CustomerWrapper> monday;
    private List<CustomerWrapper> tuesday;
    private List<CustomerWrapper> wednesday;
    private List<CustomerWrapper> thursday;
    private List<CustomerWrapper> friday;
    private List<CustomerWrapper> saturday;
    private List<CustomerWrapper> sunday;
}
