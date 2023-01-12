package com.seris.bassein.entity.schedule;

import com.seris.bassein.annotations.Attribute;
import com.seris.bassein.entity.BaseModel;
import com.seris.bassein.enums.Status;
import com.seris.bassein.util.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class TimeTable extends BaseModel {

    @Attribute(value = "Хуваарь", skip = true)
    @ManyToOne
    @NotNull
    private Schedule schedule;

    @Attribute(value = "Статус", skip = true)
    @NotNull
    private Status status;

    @Attribute("Орсон цагийн range (эхлэх - дуусах)")
    @NotNull
    @NotEmpty
    private String time;

    @Attribute(value = "Орсон цагийн range (эхлэх)", skip = true)
    @NotNull
    private LocalTime start;

    @Attribute(value = "Орсон цагийн range (дуусах)", skip = true)
    private LocalTime end;

    public String getRange() {
        return Utils.formatRange(start, end);
    }
}
