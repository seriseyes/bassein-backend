package com.seris.bassein.service.component.output;

import com.seris.bassein.util.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/output")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public record OutputController(
        OutputService outputService
) {
    @GetMapping("/bassein")
    public ResponseEntity<byte[]> printBassein(@RequestParam("dateFrom") String dateFrom, @RequestParam("dateTo") String dateTo) {
        String[] dateFromSplit = dateFrom.split("-");
        String[] dateToSplit = dateTo.split("-");

        dateFrom = dateFromSplit[0] + "-" + (Utils.addLeadingZero(dateFromSplit[1])) + "-" + (Utils.addLeadingZero(dateFromSplit[2]));
        dateTo = dateToSplit[0] + "-" + (Utils.addLeadingZero(dateToSplit[1])) + "-" + (Utils.addLeadingZero(dateToSplit[2]));

        return outputService.printBassein(LocalDate.parse(dateFrom, Utils.dateFormatFront), LocalDate.parse(dateTo, Utils.dateFormatFront));
    }

    @GetMapping("/person")
    public ResponseEntity<byte[]> printPerson(@RequestParam("regNo") String regNo) {
        return outputService.printPerson(regNo);
    }
}
