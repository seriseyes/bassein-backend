package com.seris.bassein.service.component.user;

import com.seris.bassein.entity.user.Customer;
import com.seris.bassein.entity.user.User;
import com.seris.bassein.enums.Status;
import com.seris.bassein.model.Response;
import com.seris.bassein.model.Validation;
import com.seris.bassein.service.component.user.repository.CustomerRepository;
import com.seris.bassein.service.component.user.repository.UserRepository;
import com.seris.bassein.service.security.service.PasswordEncoderService;
import com.seris.bassein.util.Utils;
import com.seris.bassein.util.Validator;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Log4j
@Service
public record UserService(
        UserRepository userRepository,
        CustomerRepository customerRepository,
        PasswordEncoderService passwordEncoderService
) {

    public ResponseEntity<Response> save(User model) {
        Validation validation = Validator.validateEntity(model);
        if (validation.isError()) return validation.toResponseEntity();

        if (userRepository.existsByUsername(model.getUsername())) {
            return Response.error("Нэвтрэх нэр давхцаж байна. Өөр нэр сонгоно уу");
        }

        model.setPassword(passwordEncoderService.encodeBCrypto(model.getPassword()));
        if (model.getStatus() == null) model.setStatus(Status.ACTIVE);

        boolean isNew = model.getId() == null;

        userRepository.save(model);
        return Response.success(isNew ? "Амжилттай хэрэглэгчийн бүртгэл үүслээ" : "Амжилттай хэрэглэгчийн бүртгэл шинэчлэгдлээ");
    }

    public ResponseEntity<?> findCurrentUserInfo() {
        Optional<User> user = userRepository.findFirstByUuid(Utils.getCurrentUserId());
        if (user.isEmpty()) return ResponseEntity.badRequest().body("Хэрэглэгч олдсонгүй. Дахин нэвтрэнэ үү");

        Map<String, String> map = new HashMap<>();
        map.put("notification", "0");
        map.put("firstname", user.get().getFirstname());
        map.put("lastname", user.get().getLastname());
        return ResponseEntity.ok(map);
    }

    public User findCurrentUser() {
        return userRepository.findFirstByUuid(Utils.getCurrentUserId()).orElse(null);
    }

    public ResponseEntity<Response> findAll() {
        return Response.success(userRepository.findAll());
    }

    public ResponseEntity<Response> saveCustomer(Customer model) {
        Validation validation = Validator.validateEntity(model);
        if (validation.isError()) return validation.toResponseEntity();

        if (!model.getRegNo().matches(Utils.regNoFormat)) return Response.error("Регистрийн дугаар буруу байна");

        if (customerRepository.existsByRegNo(model.getRegNo())) {
            return Response.error("Регистрийн дугаар давхцаж байна. Бүртгэлтэй үйлчлүүлэгч байна");
        }

        if (model.getStatus() == null) model.setStatus(Status.ACTIVE);

        boolean isNew = model.getId() == null;
        customerRepository.save(model);
        return Response.success(isNew ? "Амжилттай бүртгэл үүслээ" : "Амжилттай бүртгэл шинэчлэгдлээ");
    }

    public ResponseEntity<Response> findAllCustomer() {
        return Response.success(customerRepository.findAllByStatus(Status.ACTIVE));
    }

    public ResponseEntity<Response> findAllCustomerByRegNo(String regNo) {
        return Response.success(regNo.isEmpty()
                ? customerRepository.findAllByStatus(Status.ACTIVE)
                : customerRepository.findAllByRegNoLikeAndStatus("%" + regNo + "%", Status.ACTIVE));
    }

    public ResponseEntity<Response> findCustomerByRegNo(String regNo) {
        return customerRepository.findFirstByRegNo(regNo).map(e -> Response.success(e.getRegNo() + " үйлчлүүлэгч олдлоо", e)).orElse(Response.error("Регистрийн дугаараар үйлчлүүлэгч олдсонгүй"));
    }
}
