package com.seris.bassein.service.component.schedule;

import com.seris.bassein.entity.schedule.Schedule;
import com.seris.bassein.entity.schedule.TimeTable;
import com.seris.bassein.entity.user.Bassein;
import com.seris.bassein.entity.user.Customer;
import com.seris.bassein.enums.Status;
import com.seris.bassein.model.Response;
import com.seris.bassein.model.Validation;
import com.seris.bassein.service.component.schedule.model.TimeTableDto;
import com.seris.bassein.service.component.schedule.repository.ScheduleRepository;
import com.seris.bassein.service.component.schedule.repository.TimeTableRepository;
import com.seris.bassein.service.component.user.model.Day;
import com.seris.bassein.service.component.user.repository.BasseinRepository;
import com.seris.bassein.service.component.user.repository.CustomerRepository;
import com.seris.bassein.util.Utils;
import com.seris.bassein.util.Validator;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.seris.bassein.util.Utils.timeFormat;

@Log4j
@Service
public record ScheduleService(
        EntityManager entityManager,
        ScheduleRepository scheduleRepository,
        CustomerRepository customerRepository,
        TimeTableRepository timeTableRepository,
        BasseinRepository basseinRepository
) {

    public ResponseEntity<Response> save(Schedule model) {
        Validation validation = Validator.validateEntity(model);
        if (validation.isError()) return validation.toResponseEntity();
        scheduleRepository.save(model);
        return Response.success("Амжилттай хадгаллаа");
    }

    public ResponseEntity<Response> markAsCame(Long id, LocalTime start, LocalTime end) {
        Optional<Schedule> model = scheduleRepository.findById(id);
        if (model.isEmpty()) return Response.error("Алдаа гарлаа [S000]");

        if (model.get().getDay() == model.get().getEnter()) return Response.error("Эрх дууссан байна");

        model.get().setEnter(model.get().getEnter() + 1);

        TimeTable timeTable = new TimeTable();
        timeTable.setSchedule(scheduleRepository.save(model.get()));
        timeTable.setTime(Utils.formatRange(start, end));
        timeTable.setStart(start);
        timeTable.setEnd(end);
        timeTable.setStatus(Status.ACTIVE);
        timeTableRepository.save(timeTable);
        return Response.success("Үйлдэл амжилттай");
    }

    public ResponseEntity<Response> findAllByCustomerRegNo(String regNo) {
        Optional<Customer> customer = customerRepository.findFirstByRegNoOrPhone(regNo, regNo);
        if (customer.isEmpty())
            return Response.error(regNo + " регистрийн дугаараар бүртгэлтэй үйлчлүүлэгч олдсонгүй");
        List<Schedule> models = scheduleRepository.findAllByCustomer_RegNoOrCustomer_Phone(regNo, regNo);
        return models.isEmpty()
                ? Response.error("Уг үйлчлүүлэгчид бүртгэлтэй хуваарь олдсонгүй", List.of(new Schedule(customer.get())))
                : Response.success(models);
    }

    public ResponseEntity<Response> findAllTodayTimeTable(LocalTime start, LocalTime end) {
        Optional<Bassein> bassein = basseinRepository.findFirstBy();
        if (bassein.isEmpty()) return Response.error("Алдаа гарлаа [S001]");

        List<TimeTable> timeTables = timeTableRepository.findAllByTimeAndStatusAndCreatedBetween(Utils.formatRange(start, end), Status.ACTIVE
                , LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
                , LocalDateTime.of(LocalDate.now(), LocalTime.MAX));

        return Response.success(timeTables.stream().map(e -> {
            List<Schedule> schedules = scheduleRepository.findAllByCustomer_RegNo(e.getSchedule().getCustomer().getRegNo());
            return TimeTableDto.builder()
                    .id(e.getId())
                    .fullname(e.getSchedule().getCustomer().getFullname())
                    .age(Utils.getAgeByRegNo(e.getSchedule().getCustomer().getRegNo()))
                    .gender(Utils.getGenderByRegNo(e.getSchedule().getCustomer().getRegNo()).getMon())
                    .type(e.getSchedule().getSwimType().getLabel())
                    .schedule(schedules.stream().map(s -> s.getEnter() + "/" + s.getDay()).collect(Collectors.joining(", ")))
                    .teacher(e.getSchedule().getTeacher() == null ? "" : e.getSchedule().getTeacher().getFullname())
                    .targetTime(e.getCreated().format(timeFormat))
                    .day(e.getRange())
                    .timeTable(e)
                    .build();
        }).toList());
    }

    public ResponseEntity<Response> saveTimeTable(TimeTable model) {
        Validation validation = Validator.validateEntity(model);
        if (!validation.isError()) return Response.error(validation.getMessage());
        timeTableRepository.save(model);
        return Response.success("Амжилттай хадгаллаа");
    }

    public ResponseEntity<Response> findAllNowTime() {
        Optional<Bassein> bassein = basseinRepository.findFirstBy();
        if (bassein.isEmpty()) return Response.error("Алдаа гарлаа [S002]");

        LocalDateTime now = LocalDateTime.now();
        List<Day> nowChecked = bassein.get().getNow(f -> now.toLocalTime().isAfter(f.getStart()) && now.toLocalTime().isBefore(f.getEnd()))
                .stream()
                .peek(el -> el.setClose(true))
                .toList();
        List<Day> nowUnChecked = bassein.get().getNow(f -> true);

        return Response.success(Stream.of(nowChecked, nowUnChecked).flatMap(List::stream)
                .peek(el -> el.setLabel(el.getStart().format(timeFormat) + " - " + el.getEnd().format(timeFormat)))
                .toList());
    }

    public ResponseEntity<Response> cancelCame(Long timeTableId) {
        Optional<TimeTable> timeTable = timeTableRepository.findById(timeTableId);
        if (timeTable.isEmpty()) return Response.error("Хуваарь олдсонгүй [S003]");

        Schedule schedule = timeTable.get().getSchedule();
        schedule.setEnter(schedule.getEnter() - 1);
        scheduleRepository.save(schedule);

        timeTable.get().setStatus(Status.INACTIVE);
        timeTableRepository.save(timeTable.get());
        return Response.success("Үйлдэл амжилттай");
    }
}
