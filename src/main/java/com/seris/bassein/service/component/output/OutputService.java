package com.seris.bassein.service.component.output;

import com.google.common.collect.Table;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.google.common.collect.Table.Cell;
import com.querydsl.core.Tuple;
import com.querydsl.core.util.MathUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.seris.bassein.entity.schedule.QSchedule;
import com.seris.bassein.entity.schedule.QTimeTable;
import com.seris.bassein.entity.schedule.Schedule;
import com.seris.bassein.entity.schedule.TimeTable;
import com.seris.bassein.entity.settings.QSettings;
import com.seris.bassein.entity.settings.Settings;
import com.seris.bassein.entity.user.QBassein;
import com.seris.bassein.entity.user.QCustomer;
import com.seris.bassein.entity.user.QUser;
import com.seris.bassein.model.Response;
import com.seris.bassein.util.Utils;
import com.seris.bassein.util.pdf.Cells;
import com.seris.bassein.util.pdf.Defaults;
import com.seris.bassein.util.pdf.PageTemplate;
import com.seris.bassein.util.pdf.StatefulDocument;
import jdk.jshell.execution.Util;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import net.sourceforge.jtds.jdbc.DateTime;
import org.apache.tomcat.jni.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Log4j
public record OutputService(
        EntityManager entityManager
) {
    @SneakyThrows
    public ResponseEntity<byte[]> printBassein(LocalDate dateFrom, LocalDate dateTo) {
        StatefulDocument document = Defaults.getDocument("Бассейны хэмжээнд", PageTemplate.A4rotate);

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QSchedule qSchedule = QSchedule.schedule;
        QTimeTable qTimeTable = QTimeTable.timeTable;
        QSettings qSettings = QSettings.settings;

        List<Tuple> tuples = jpaQueryFactory.select(
                qSchedule,
                qTimeTable)
                .from(qTimeTable)
                .innerJoin(qSchedule).on(qSchedule.id.eq(qTimeTable.schedule.id))
                .where(qTimeTable.updated.between(dateFrom.atStartOfDay(), Utils.dateToMax(dateTo)))
                .fetch();

        List<TimeTable> timeTables = jpaQueryFactory.select(qTimeTable).from(qTimeTable).where(qTimeTable.updated.between(Utils.dateFromMin(dateFrom), Utils.dateToMax(dateTo))).fetch();

        PdfPTable table = new PdfPTable(3); // 3 Columns
        table.addCell(Cells.get(dateFrom.format(Utils.dateFormat)).add(" - ").add(dateTo.format(Utils.dateFormat)));

        table.addCell(Cells.get("Нийт бассеинд орсон"));
        table.addCell(Cells.get("Тухайн өдрийн нийт орлого"));
        table.setHeaderRows(1);

        List<LocalDate> dates = dateFrom.datesUntil(dateTo.plusDays(1)).toList();

        for (LocalDate date1 : dates) {
            long total = timeTables.stream().filter(f -> f.getUpdated().toLocalDate().equals(date1)).count();
            int sums = timeTables.stream().filter(f -> f.getUpdated().toLocalDate().equals(date1)).map(m -> m.getSchedule().getSwimType().getValue()).mapToInt(Double::intValue).sum();
            log.info(sums);
            int discount = timeTables.stream().filter(f -> f.getUpdated().toLocalDate().equals(date1) && f.getSchedule().getDiscount() != null).map(m -> m.getSchedule().getDiscount().getValue()).mapToInt(Double::intValue).sum();
            log.info(discount);
            table.addCell(new PdfPCell(Phrase.getInstance(date1.format(Utils.dateFormat))));
            table.addCell(total != 0 ? String.valueOf(total) : "");
            table.addCell(String.valueOf(sums - discount));
        }

        document.add(table);
        return Response.pdf(document);
    }

    @SneakyThrows
    public ResponseEntity<byte[]> printPerson(String regNo) {
        StatefulDocument document = Defaults.getDocument(regNo, PageTemplate.A4rotate);

        QCustomer qCustomer = QCustomer.customer;
        QTimeTable qTimeTable = QTimeTable.timeTable;
        QSchedule qSchedule = QSchedule.schedule;

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        List<Tuple> tuples = jpaQueryFactory.select(qCustomer, qSchedule, qTimeTable)
                .from(qTimeTable)
                .innerJoin(qSchedule).on(qSchedule.id.eq(qTimeTable.schedule.id))
                .innerJoin(qCustomer).on(qCustomer.id.eq(qSchedule.customer.id))
                .where(qCustomer.regNo.eq(regNo))
                .fetch();

        log.info(tuples);

//        PdfPTable table = new PdfPTable(3);
//        table.addCell(tuples.get(0).get(qCustomer.lastname) + " " + tuples.get(0).get(qCustomer.firstname));
//        table.addCell(regNo);
//        table.addCell(" ");
//        table.setHeaderRows(1);

        PdfPTable table = new PdfPTable(3); // 3 Columns
        table.addCell(Cells.get("date"));
        table.addCell(Cells.get("Нийт бассеинд орсон"));
        table.addCell(Cells.get("Тухайн өдрийн нийт орлого"));
        table.setHeaderRows(1);





        document.add(table);
        return Response.pdf(document);
    }
}
