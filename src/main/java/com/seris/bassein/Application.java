package com.seris.bassein;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Map<DayOfWeek, String> daysMap() {
        Map<DayOfWeek, String> daysMap = new HashMap<>();
        daysMap.put(DayOfWeek.MONDAY, "Даваа");
        daysMap.put(DayOfWeek.TUESDAY, "Мягмар");
        daysMap.put(DayOfWeek.WEDNESDAY, "Лхагва");
        daysMap.put(DayOfWeek.THURSDAY, "Пүрэв");
        daysMap.put(DayOfWeek.FRIDAY, "Баасан");
        daysMap.put(DayOfWeek.SATURDAY, "Бямба");
        daysMap.put(DayOfWeek.SUNDAY, "Ням");
        return daysMap;
    }
}
