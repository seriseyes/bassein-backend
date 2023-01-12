package com.seris.bassein.service.component.user;

import com.seris.bassein.entity.user.Customer;
import com.seris.bassein.entity.user.User;
import com.seris.bassein.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * @author Bayarkhuu.Luv 2022.03.17
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public record UserController(
        UserService service
) {

    @GetMapping("/info")
    public ResponseEntity<?> findCurrentUserInfo() {
        return service.findCurrentUserInfo();
    }

    @PostMapping("/save")
    public ResponseEntity<Response> save(@RequestBody User model) {
        return service.save(model);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> findAll() {
        return service.findAll();
    }

    @PostMapping("/customer/save")
    public ResponseEntity<Response> save(@RequestBody Customer model) {
        return service.saveCustomer(model);
    }

    @GetMapping("/customer/all")
    public ResponseEntity<Response> findAllCustomer() {
        return service.findAllCustomer();
    }

    @GetMapping("/customer/like/regNo")
    public ResponseEntity<Response> findAllCustomerByRegNo(@RequestParam("regNo") String regNo) {
        return service.findAllCustomerByRegNo(regNo);
    }

    @GetMapping("/customer/regNo")
    public ResponseEntity<Response> findCustomerByRegNo(@RequestParam("regNo") String regNo) {
        return service.findCustomerByRegNo(regNo);
    }

    @GetMapping("/all/teacher")
    public ResponseEntity<Response> findAllTeacher() {
        return service.findAllTeacher();
    }
}
