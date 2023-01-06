package com.seris.bassein.service.component.schedule.model;

import com.seris.bassein.annotations.Attribute;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class TimePer {
    @Attribute("Эхлэх цаг")
    @NotNull
    private LocalTime time;
    @Attribute(value = "Хэддэх өдөр", notZero = true)
    private int week;
}
