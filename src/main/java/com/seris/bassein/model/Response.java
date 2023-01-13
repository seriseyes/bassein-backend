package com.seris.bassein.model;

import com.seris.bassein.enums.State;
import com.seris.bassein.util.pdf.StatefulDocument;
import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.nio.file.Files;

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

    @SneakyThrows
    public static ResponseEntity<byte[]> pdf(StatefulDocument document) {
        String state = document.getState();
        byte[] contents = Files.readAllBytes(new File(state).toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(state, state);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }
}
