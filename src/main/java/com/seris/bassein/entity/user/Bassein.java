package com.seris.bassein.entity.user;

import com.seris.bassein.annotations.Attribute;
import com.seris.bassein.entity.BaseModel;
import com.seris.bassein.service.component.user.model.Day;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Bassein extends BaseModel {

    @Type(type = "json")
    @Column(columnDefinition = "TEXT")
    private List<Day> monday;

    @Type(type = "json")
    @Column(columnDefinition = "TEXT")
    private List<Day> tuesday;

    @Type(type = "json")
    @Column(columnDefinition = "TEXT")
    private List<Day> wednesday;

    @Type(type = "json")
    @Column(columnDefinition = "TEXT")
    private List<Day> thursday;

    @Type(type = "json")
    @Column(columnDefinition = "TEXT")
    private List<Day> friday;

    @Type(type = "json")
    @Column(columnDefinition = "TEXT")
    private List<Day> saturday;

    @Type(type = "json")
    @Column(columnDefinition = "TEXT")
    private List<Day> sunday;

    public List<Day> getNow(Predicate<Day> filter) {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();

        switch (dayOfWeek) {
            case MONDAY -> {
                return new ArrayList<>(monday.stream().filter(filter).toList());
            }
            case TUESDAY -> {
                return new ArrayList<>(tuesday.stream().filter(filter).toList());
            }
            case WEDNESDAY -> {
                return new ArrayList<>(wednesday.stream().filter(filter).toList());
            }
            case THURSDAY -> {
                return new ArrayList<>(thursday.stream().filter(filter).toList());
            }
            case FRIDAY -> {
                return new ArrayList<>(friday.stream().filter(filter).toList());
            }
            case SATURDAY -> {
                return new ArrayList<>(saturday.stream().filter(filter).toList());
            }
            case SUNDAY -> {
                return new ArrayList<>(sunday.stream().filter(filter).toList());
            }
        }

        return null;
    }
}
