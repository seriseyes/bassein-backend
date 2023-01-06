package com.seris.bassein.service.security.service;

import lombok.extern.log4j.Log4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class PasswordEncoderService {

    /**
     * Нууц үг encode хийх
     *
     * @param value нууц үг
     * @return encode хийсэн нууц үг
     */
    public String encodeBCrypto(String value) {
        BCryptPasswordEncoder passwordEncoder = getEncoder();
        return passwordEncoder.encode(value);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }

    public BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
