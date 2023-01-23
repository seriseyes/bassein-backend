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
    private String role;
    private String username;

    public JwtResponse(String token, String type, LocalDateTime expire) {
        this.token = token;
        this.type = type;
        this.expire = expire;
    }
}

