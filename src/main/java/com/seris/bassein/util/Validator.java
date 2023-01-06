package com.seris.bassein.util;

import com.seris.bassein.annotations.Attribute;
import com.seris.bassein.model.Validation;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.function.Function;

@Log4j
public class Validator {

    @SneakyThrows
    public static Validation validateEntity(Object entity) {
        if (entity == null) return Validation.error("Entity null байна.");

        Field[] fields = entity.getClass().getDeclaredFields();

        Function<Field, Validation> nullMessage = field -> {
            if (field.isAnnotationPresent(Attribute.class)) {
                return Validation.error(field.getAnnotation(Attribute.class).value() + " тодорхойгүй байна.");
            } else {
                return Validation.error(field.getName() + " тодорхойгүй байна.");
            }
        };

        for (Field field : fields) {
            field.setAccessible(true);
            Attribute annotation = field.getAnnotation(Attribute.class);
            if (annotation != null && annotation.skip()) continue;

            if (field.isAnnotationPresent(NotNull.class) && field.get(entity) == null)
                return nullMessage.apply(field);

            if (field.isAnnotationPresent(NotEmpty.class)) {
                Object value = field.get(entity);
                if (value == null || value.toString().trim().isEmpty()) return nullMessage.apply(field);
            }

            if (field.isAnnotationPresent(Length.class)) {
                Length length = field.getAnnotation(Length.class);
                String message = annotation != null ? annotation.value() : field.getName();

                if (field.get(entity).toString().length() > length.max()) {
                    return Validation.error(message + " " + length.max() + " тэмдэгтээс илүүгүй байх ёстой");
                }
                if (field.get(entity).toString().length() < length.min()) {
                    return Validation.error(message + " " + length.min() + " тэмдэгтээс багагүй байх ёстой");
                }
            }

            if (annotation != null) {
                if (annotation.trim()) field.set(entity, field.get(entity).toString().trim());
                if (annotation.capitalize()) field.set(entity, StringUtils.capitalize(field.get(entity).toString()));
                if (annotation.upper()) field.set(entity, field.get(entity).toString().toUpperCase());
                if (annotation.lower()) field.set(entity, field.get(entity).toString().toLowerCase());
                if (annotation.noSpace()) field.set(entity, field.get(entity).toString().replaceAll(" ", ""));
                if (annotation.notZero()) {
                    int i = Integer.parseInt(field.get(entity).toString());
                    if (i <= 0) {
                        return Validation.error(annotation.value() + (i == 0 ? " 0 байж болохгүй": " сөрөг тоо байж болохгүй"));
                    }
                };
            }
        }

        return Validation.success();
    }
}
