package com.seris.bassein.entity.schedule;

import com.seris.bassein.annotations.Attribute;
import com.seris.bassein.entity.BaseModel;
import com.seris.bassein.entity.settings.Settings;
import com.seris.bassein.entity.user.Customer;
import com.seris.bassein.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Schedule extends BaseModel {

    @Attribute(value = "Үйлчлүүлэгч", skip = true)
    @ManyToOne
    @NotNull
    private Customer customer;

    @Attribute(value = "Хэдэн удаа явах", notZero = true)
    private int day;

    @Attribute(value = "Хэдэн удаа явсан")
    private int enter;

    @Attribute("Хөнгөлөлт")
    @ManyToOne
    private Settings discount;

    @Attribute("Төрөл")
    @NotNull
    @ManyToOne
    private Settings swimType;

    @Attribute("Багш")
    @ManyToOne
    private User teacher;

    public Schedule(Customer customer) {
        this.customer = customer;
    }
}
