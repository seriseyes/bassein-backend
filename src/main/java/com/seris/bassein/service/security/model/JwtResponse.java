package com.seris.bassein.service.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type;
    private LocalDateTime expire;
}

