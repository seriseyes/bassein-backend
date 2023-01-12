package com.seris.bassein.service.component.schedule;

import com.seris.bassein.entity.schedule.Schedule;
import com.seris.bassein.entity.schedule.TimeTable;
import com.seris.bassein.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public record ScheduleController(
        ScheduleService scheduleService
) {
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody Schedule model) {
        return scheduleService.save(model);
    }

    @GetMapping("/came")
    public ResponseEntity<Response> markAsCame(@RequestParam("came") Long id, @RequestParam("start") String start, @RequestParam("end") String end) {
        return scheduleService.markAsCame(id, LocalTime.parse(start), LocalTime.parse(end));
    }

    @GetMapping("/cancel")
    public ResponseEntity<Response> cancelCame(@RequestParam("timeTableId") Long timeTableId) {
        return scheduleService.cancelCame(timeTableId);
    }

    @GetMapping("/customer")
    public ResponseEntity<Response> findAllByCustomerRegNo(@RequestParam("regNo") String regNo) {
        return scheduleService.findAllByCustomerRegNo(regNo);
    }

    @PostMapping("/save/timeTable")
    public ResponseEntity<Response> saveTimeTable(@RequestBody TimeTable model) {
        return scheduleService.saveTimeTable(model);
    }

    @GetMapping("/today")
    public ResponseEntity<Response> findAllTodayTimeTable(@RequestParam("start") String start, @RequestParam("end") String end) {
        return scheduleService.findAllTodayTimeTable(LocalTime.parse(start), LocalTime.parse(end));
    }

    @GetMapping("/now")
    public ResponseEntity<Response> findAllNowTime() {
        return scheduleService.findAllNowTime();
    }
}
