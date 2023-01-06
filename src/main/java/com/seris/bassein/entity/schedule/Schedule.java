package com.seris.bassein.entity.schedule;

import com.seris.bassein.annotations.Attribute;
import com.seris.bassein.entity.BaseModel;
import com.seris.bassein.entity.user.Customer;
import com.seris.bassein.service.component.schedule.model.TimePer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Schedule extends BaseModel {

    @Attribute(value = "Үйлчлүүлэгч", skip = true)
    @ManyToOne
    @NotNull
    private Customer customer;

    @Attribute(value = "Хэдэн удаа явах", notZero = true)
    private int day;

    @Attribute(value = "Хэдэн удаа явсан")
    private int enter;

    @Attribute(value = "Явсан огноонууд")
    @Type(type = "json")
    @Column(columnDefinition = "TEXT")
    private List<LocalDateTime> enterDates;

    @Attribute("Төлөвлөгөө")
    @Type(type = "json")
    @Column(columnDefinition = "TEXT")
    private List<TimePer> plans;
}
