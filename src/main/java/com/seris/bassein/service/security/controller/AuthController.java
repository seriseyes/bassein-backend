package com.seris.bassein.service.security.controller;

import com.seris.bassein.entity.settings.Settings;
import com.seris.bassein.entity.user.Bassein;
import com.seris.bassein.entity.user.User;
import com.seris.bassein.enums.Role;
import com.seris.bassein.enums.Status;
import com.seris.bassein.service.component.schedule.ScheduleService;
import com.seris.bassein.service.component.settings.repository.SettingsRepository;
import com.seris.bassein.service.component.user.UserService;
import com.seris.bassein.service.component.user.model.Day;
import com.seris.bassein.service.component.user.repository.BasseinRepository;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Log4j
public record AuthController(
        JwtTokenUtil jwtTokenUtil,
        JwtUserDetailsService userDetailsService,
        AuthenticationManager authenticationManager,
        UserRepository userRepository,
        UserService userService,
        BasseinRepository basseinRepository,
        SettingsRepository settingsRepository
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
        user.setRole(Role.RECEPTION);
        userService.save(user);
        user = userRepository.findFirstByUsername("admin").orElse(null);
        if (user == null) return ResponseEntity.ok("ERROR");

        List<Day> workingDays = new ArrayList<>();
        workingDays.add(new Day(LocalTime.of(7, 0), LocalTime.of(8, 0)));
        workingDays.add(new Day(LocalTime.of(7, 45), LocalTime.of(8, 45)));
        workingDays.add(new Day(LocalTime.of(8, 30), LocalTime.of(9, 30)));
        workingDays.add(new Day(LocalTime.of(9, 15), LocalTime.of(10, 15)));
        workingDays.add(new Day(LocalTime.of(10, 0), LocalTime.of(11, 0)));
        workingDays.add(new Day(LocalTime.of(10, 45), LocalTime.of(11, 45)));
        workingDays.add(new Day(LocalTime.of(11, 30), LocalTime.of(12, 30)));
        workingDays.add(new Day(LocalTime.of(12, 15), LocalTime.of(13, 15)));

        workingDays.add(new Day(LocalTime.of(14, 15), LocalTime.of(15, 15)));
        workingDays.add(new Day(LocalTime.of(15, 0), LocalTime.of(16, 0)));
        workingDays.add(new Day(LocalTime.of(15, 45), LocalTime.of(16, 45)));
        workingDays.add(new Day(LocalTime.of(16, 30), LocalTime.of(17, 30)));
        workingDays.add(new Day(LocalTime.of(17, 15), LocalTime.of(18, 15)));
        workingDays.add(new Day(LocalTime.of(18, 0), LocalTime.of(19, 0)));
        workingDays.add(new Day(LocalTime.of(18, 45), LocalTime.of(19, 45)));
        workingDays.add(new Day(LocalTime.of(19, 30), LocalTime.of(20, 0)));

        List<Day> weekend = new ArrayList<>();
        weekend.add(new Day(LocalTime.of(7, 45), LocalTime.of(8, 45)));
        weekend.add(new Day(LocalTime.of(8, 30), LocalTime.of(9, 30)));
        weekend.add(new Day(LocalTime.of(9, 15), LocalTime.of(10, 15)));
        weekend.add(new Day(LocalTime.of(10, 0), LocalTime.of(11, 0)));
        weekend.add(new Day(LocalTime.of(10, 45), LocalTime.of(11, 45)));
        weekend.add(new Day(LocalTime.of(11, 30), LocalTime.of(12, 30)));
        weekend.add(new Day(LocalTime.of(12, 15), LocalTime.of(13, 15)));

        weekend.add(new Day(LocalTime.of(14, 15), LocalTime.of(15, 15)));
        weekend.add(new Day(LocalTime.of(15, 0), LocalTime.of(16, 0)));
        weekend.add(new Day(LocalTime.of(15, 45), LocalTime.of(16, 45)));
        weekend.add(new Day(LocalTime.of(16, 30), LocalTime.of(17, 30)));
        weekend.add(new Day(LocalTime.of(17, 15), LocalTime.of(18, 15)));
        weekend.add(new Day(LocalTime.of(18, 0), LocalTime.of(19, 0)));
        weekend.add(new Day(LocalTime.of(18, 45), LocalTime.of(19, 45)));

        Bassein model = new Bassein();
        model.setMonday(workingDays);
        model.setTuesday(workingDays);
        model.setWednesday(workingDays);
        model.setThursday(workingDays);
        model.setFriday(workingDays);
        model.setSaturday(weekend);
        model.setSunday(weekend);

        basseinRepository.save(model);

        settingsRepository.saveAll(List.of(
                Settings.builder().label("Гэр бүл").name("discount").user(user).status(Status.ACTIVE).value(40000).has(false).build(),
                Settings.builder().label("Сургалт").name("swimType").user(user).status(Status.ACTIVE).value(120000).has(true).build(),
                Settings.builder().label("Чөлөөт").name("swimType").user(user).status(Status.ACTIVE).value(80000).has(false).build(),
                Settings.builder().label("Дугуйлан").name("swimType").user(user).status(Status.ACTIVE).value(100000).has(false).build(),
                Settings.builder().label("Ганцаарчилсан").name("swimType").user(user).status(Status.ACTIVE).value(200000).has(true).build(),
                Settings.builder().label("Байгууллага").name("discount").user(user).status(Status.ACTIVE).value(30000).has(false).build(),
                Settings.builder().label("Сургууль").name("discount").user(user).status(Status.ACTIVE).value(30000).has(false).build()
        ));

        return ResponseEntity.ok("username: admin, password: 1234");
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
