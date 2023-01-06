package com.seris.bassein.model;

import com.seris.bassein.enums.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public class Validation {
    private State state;
    @Getter
    private String message;

    public static Validation error(String message) {
        return new Validation(State.ERROR, message);
    }

    public static Validation success(String message) {
        return new Validation(State.SUCCESS, message);
    }

    public static Validation success() {
        return new Validation(State.SUCCESS, null);
    }

    public boolean isError() {
        return state == State.ERROR;
    }

    public boolean isSuccess() {
        return state == State.SUCCESS;
    }

    public ResponseEntity<Response> toResponseEntity() {
        return isError() ? Response.error(message) : Response.success(message);
    }
}
