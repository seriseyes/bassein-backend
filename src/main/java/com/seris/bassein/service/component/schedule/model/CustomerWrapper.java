package com.seris.bassein.service.component.schedule.model;

import com.seris.bassein.entity.user.Customer;
import lombok.Data;

@Data
public class CustomerWrapper {
    private Customer customer;
    private String time;
}
