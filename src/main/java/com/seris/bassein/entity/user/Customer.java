package com.seris.bassein.entity.user;

import com.seris.bassein.annotations.Attribute;
import com.seris.bassein.entity.BaseModel;
import com.seris.bassein.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Үйлчлүүлэгч
 *
 * @author Bayarkhuu.Luv 2023.01.06
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Customer extends BaseModel {

    @Attribute(value = "Нэр", capitalize = true, trim = true, noSpace = true)
    @NotNull
    @NotEmpty
    @Length(max = 20)
    private String firstname;

    @Attribute(value = "Овог", capitalize = true, trim = true, noSpace = true)
    @NotNull
    @NotEmpty
    @Length(max = 20)
    private String lastname;

    @Attribute(value = "Регистрийн дугаар", trim = true, upper = true, noSpace = true)
    @NotNull
    @NotEmpty
    @Length(min = 10, max = 10)
    @Column(unique = true)
    private String regNo;

    @NotNull
    private Status status;
}
