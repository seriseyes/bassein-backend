package com.seris.bassein.service.component.settings;

import com.seris.bassein.entity.settings.Settings;
import com.seris.bassein.entity.user.User;
import com.seris.bassein.enums.Status;
import com.seris.bassein.model.Response;
import com.seris.bassein.model.Validation;
import com.seris.bassein.service.component.settings.repository.SettingsRepository;
import com.seris.bassein.service.component.user.repository.UserRepository;
import com.seris.bassein.util.Utils;
import com.seris.bassein.util.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record SettingsService(
        SettingsRepository repository,
        UserRepository userRepository
) {
    public ResponseEntity<Response> findAllByName(String name) {
        return Response.success(repository.findAllByNameAndStatus(name, Status.ACTIVE));
    }

    public ResponseEntity<Response> save(Settings model) {
        Validation validation = Validator.validateEntity(model);
        if (validation.isError()) return validation.toResponseEntity();

        Optional<User> user = userRepository.findFirstByUuid(Utils.getCurrentUserId());
        if (user.isEmpty()) return Response.error("Нэвтэрсэн хэрэглэгч олдсонгүй. Дахин нэвтэрнэ үү");
        if (model.getUser() == null) model.setUser(user.get());
        if (model.getStatus() == null) model.setStatus(Status.ACTIVE);

        repository.save(model);

        return Response.success("Амжилттай хадгалагдлаа");
    }
}
