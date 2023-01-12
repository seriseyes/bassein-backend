package com.seris.bassein.entity.user;

import com.seris.bassein.annotations.Attribute;
import com.seris.bassein.entity.BaseModel;
import com.seris.bassein.enums.Role;
import com.seris.bassein.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class User extends BaseModel {

    @Attribute(value = "Нэр", trim = true, capitalize = true, noSpace = true)
    @NotNull
    @NotEmpty
    @Length(max = 20)
    private String firstname;

    @Attribute(value = "Овог", trim = true, capitalize = true, noSpace = true)
    @NotNull
    @NotEmpty
    @Length(max = 20)
    private String lastname;

    @Attribute(value = "Нэвтрэх нэр", trim = true, lower = true, noSpace = true)
    @Column(unique = true)
    @Length(min = 2, max = 15)
    @NotNull
    @NotEmpty
    private String username;

    @Attribute("Нууц үг")
    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @Attribute(value = "Статус", skip = true)
    private Status status;

    @Attribute("Хэрэглэгчийн эрх")
    @NotNull(message = "Эрх тодорхойгүй байна")
    private Role role;

    public String getFullname() {
        return lastname.charAt(0) + ". " + firstname;
    }
}
