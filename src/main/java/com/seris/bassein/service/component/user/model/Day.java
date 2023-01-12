package com.seris.bassein.service.component.user.model;

import com.seris.bassein.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Day {
    private LocalTime start;
    private LocalTime end;
    private String label;
    private boolean close;

    public Day(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return Utils.formatRange(start, end);
    }
}
