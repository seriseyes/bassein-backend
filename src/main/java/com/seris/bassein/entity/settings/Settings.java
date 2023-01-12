package com.seris.bassein.entity.settings;

import com.seris.bassein.annotations.Attribute;
import com.seris.bassein.entity.BaseModel;
import com.seris.bassein.entity.user.User;
import com.seris.bassein.enums.Status;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Settings extends BaseModel {
    @Attribute("Нэр")
    @NotNull
    @NotEmpty
    private String name;

    @Attribute("Утга")
    @NotNull
    @NotEmpty
    private String label;

    @Attribute("Тэмдэглэл")
    @Length(max = 2500)
    private String note;

    @Attribute("value")
    private double value;

    @Attribute("Төлөв")
    @Column(columnDefinition = "boolean default false")
    private boolean has;

    @Attribute(value = "Үүсгэсэн хэрэглэгч", skip = true)
    @ManyToOne
    @NotNull
    private User user;

    @Attribute(value = "Статус", skip = true)
    @NotNull
    private Status status;
}
