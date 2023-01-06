package com.seris.bassein.service.component.schedule;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seris.bassein.entity.schedule.QSchedule;
import com.seris.bassein.entity.schedule.Schedule;
import com.seris.bassein.entity.user.Customer;
import com.seris.bassein.model.Response;
import com.seris.bassein.model.Validation;
import com.seris.bassein.service.component.schedule.model.CustomerWrapper;
import com.seris.bassein.service.component.schedule.model.ScheduleDto;
import com.seris.bassein.service.component.schedule.model.ScheduleWrapper;
import com.seris.bassein.service.component.schedule.repository.ScheduleRepository;
import com.seris.bassein.util.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Service
public record ScheduleService(
        EntityManager entityManager,
        ScheduleRepository scheduleRepository,
        Map<DayOfWeek, String> daysMap
) {

    public ResponseEntity<Response> save(ScheduleDto model) {
        if (model.getCustomers() == null || model.getCustomers().isEmpty()) {
            return Response.error("Үйлчлүүлэгч тодорхойгүй байна");
        }
        if (model.getSchedule() == null) return Response.error("Төлөвлөгөө тодорхойгүй байна");
        if (model.getSchedule().getPlans() == null || model.getSchedule().getPlans().isEmpty()) {
            return Response.error("7 хоногийн аль аль өдөр, хэдэн цагаас явах тодорхойгүй байна");
        }

        Validation validation = Validator.validateEntity(model.getSchedule());
        if (validation.isError()) return validation.toResponseEntity();

        List<Schedule> schedules = new ArrayList<>();

        for (Customer customer : model.getCustomers()) {
            Schedule schedule = new Schedule();
            schedule.setCustomer(customer);
            schedule.setDay(model.getSchedule().getDay());
            schedule.setEnter(model.getSchedule().getEnter());
            schedule.setPlans(model.getSchedule().getPlans());
            schedules.add(schedule);
        }

        scheduleRepository.saveAll(schedules);
        return Response.success("Хуваарь амжилттай үүслээ" + (schedules.size() > 1 ? " Нийт: " + schedules.size() + " үйлчлүүлэгч" : ""));
    }

    public ResponseEntity<Response> findWeekSchedule() {
        ScheduleWrapper model = new ScheduleWrapper();
        model.setExist(false);

        QSchedule qSchedule = QSchedule.schedule;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Schedule> schedules = queryFactory.selectFrom(qSchedule).where(qSchedule.day.gt(qSchedule.enter)).fetch();

        BiConsumer<Consumer<List<CustomerWrapper>>, Integer> filter = (fn, day) -> {
            List<CustomerWrapper> today = schedules.stream().filter(f -> f.getPlans().stream().anyMatch(a -> a.getWeek() == day)).map(e -> {
                CustomerWrapper customerWrapper = new CustomerWrapper();
                customerWrapper.setCustomer(e.getCustomer());
                customerWrapper.setTime(e.getPlans().stream().findFirst().map(ex -> ex.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))).orElse(""));
                return customerWrapper;
            }).toList();
            fn.accept(today);

            if (!model.isExist() && !today.isEmpty()) {
                model.setExist(true);
            }
        };

        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

        switch (dayOfWeek) {
            case MONDAY -> filter.accept(model::setMonday, 1);
            case TUESDAY -> filter.accept(model::setTuesday, 2);
            case WEDNESDAY -> filter.accept(model::setWednesday, 3);
            case THURSDAY -> filter.accept(model::setThursday, 4);
            case FRIDAY -> filter.accept(model::setFriday, 5);
            case SATURDAY -> filter.accept(model::setSaturday, 6);
            case SUNDAY -> filter.accept(model::setSunday, 7);
        }

        model.setDay(dayOfWeek.ordinal() + 1);
        model.setDayName(daysMap.get(dayOfWeek));
        return Response.success(model);
    }
}
