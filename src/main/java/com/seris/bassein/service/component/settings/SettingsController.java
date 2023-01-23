package com.seris.bassein.service.component.settings;

import com.seris.bassein.entity.settings.Settings;
import com.seris.bassein.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public record SettingsController(
        SettingsService settingsService
) {

    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody Settings model) {
        return settingsService.save(model);
    }

    @GetMapping("/all/name")
    public ResponseEntity<Response> findAllByName(@RequestParam("name") String name) {
        return settingsService.findAllByName(name);
    }
}
