package com.seris.bassein.service.component.schedule;

import com.seris.bassein.model.Response;
import com.seris.bassein.service.component.schedule.model.ScheduleDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public record ScheduleController(
        ScheduleService scheduleService
) {
    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody ScheduleDto model) {
        return scheduleService.save(model);
    }

    @GetMapping("/week")
    public ResponseEntity<Response> findWeekSchedule() {
        return scheduleService.findWeekSchedule();
    }

    @GetMapping("/came")
    public ResponseEntity<Response> markAsCame(@RequestParam("came") Long id) {
        return scheduleService.markAsCame(id);
    }

    @GetMapping("/customer")
    public ResponseEntity<Response> findAllByCustomerRegNo(@RequestParam("regNo") String regNo) {
        return scheduleService.findAllByCustomerRegNo(regNo);
    }
}
