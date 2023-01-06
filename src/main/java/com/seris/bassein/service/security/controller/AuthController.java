package com.seris.bassein.service.security.controller;

import com.seris.bassein.entity.user.User;
import com.seris.bassein.enums.Role;
import com.seris.bassein.service.component.user.UserService;
import com.seris.bassein.service.component.user.repository.UserRepository;
import com.seris.bassein.service.security.config.JwtTokenUtil;
import com.seris.bassein.service.security.model.JwtRequest;
import com.seris.bassein.service.security.model.JwtResponse;
import com.seris.bassein.service.security.service.JwtUserDetailsService;
import com.seris.bassein.util.Utils;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Log4j
public record AuthController(
        JwtTokenUtil jwtTokenUtil,
        JwtUserDetailsService userDetailsService,
        AuthenticationManager authenticationManager,
        UserRepository userRepository,
        UserService userService
) {


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) throws Exception {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final JwtResponse token = jwtTokenUtil.generateToken(userDetails);
        response.addCookie(Utils.getCookie("token", token.getToken()));
        response.addCookie(Utils.getCookie("username", userDetails.getUsername()));
        response.addCookie(Utils.getCookie("role", userDetails.getAuthorities().stream().filter(f -> !f.getAuthority().startsWith("id-")).findFirst().map(GrantedAuthority::getAuthority).orElse("error")));
        return ResponseEntity.ok(token);
    }

    @GetMapping("/create")
    public ResponseEntity<String> createInitialUser() {
        if (userRepository.existsByUsername("admin"))
            return ResponseEntity.badRequest().body("Admin user үүссэн байна.");
        User user = new User();
        user.setFirstname("admin");
        user.setLastname("admin");
        user.setUsername("admin");
        user.setPassword("1234");
        user.setRole(Role.ADMIN);
        return ResponseEntity.ok("username: admin, password: 1234, message: " + userService.save(user).getBody());
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
        return ResponseEntity.ok(jwtTokenUtil.validateToken(token, userDetails));
    }

}
