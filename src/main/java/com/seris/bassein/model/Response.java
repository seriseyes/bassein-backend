package com.seris.bassein.model;

import com.seris.bassein.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String message;
    private State state;
    private Object data;

    public static ResponseEntity<Response> success(String message) {
        return ResponseEntity.ok(Response.builder()
                .state(State.SUCCESS)
                .message(message)
                .build());
    }

    public static ResponseEntity<Response> success(Object data) {
        return ResponseEntity.ok(Response.builder()
                .state(State.SUCCESS)
                .data(data)
                .build());
    }

    public static ResponseEntity<Response> success(String message, Object data) {
        return ResponseEntity.ok(Response.builder()
                .message(message)
                .state(State.SUCCESS)
                .data(data)
                .build());
    }

    public static ResponseEntity<Response> error(String message) {
        return ResponseEntity.ok(Response.builder()
                .state(State.ERROR)
                .message(message)
                .build());
    }

    public static ResponseEntity<Response> error(String message, Object data) {
        return ResponseEntity.ok(Response.builder()
                .state(State.ERROR)
                .message(message)
                .data(data)
                .build());
    }
}
