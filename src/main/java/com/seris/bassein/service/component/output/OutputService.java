package com.seris.bassein.service.component.output;

import com.itextpdf.text.pdf.PdfPTable;
import com.seris.bassein.util.Utils;
import com.seris.bassein.util.pdf.Cells;
import com.seris.bassein.util.pdf.Defaults;
import com.seris.bassein.util.pdf.PageTemplate;
import com.seris.bassein.util.pdf.StatefulDocument;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;

@Service
public record OutputService() {
    @SneakyThrows
    public ResponseEntity<byte[]> printBassein(LocalDate dateFrom, LocalDate dateTo) {
        StatefulDocument document = Defaults.getDocument("Бассейны хэмжээнд", PageTemplate.A4rotate);

        PdfPTable table = new PdfPTable(1);
        table.addCell(Cells.get(dateFrom.format(Utils.dateFormat)).add(" - ").add(dateTo.format(Utils.dateFormat)));
        document.add(table);

        String state = document.getState();
        byte[] contents = Files.readAllBytes(new File(state).toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(state, state);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @SneakyThrows
    public ResponseEntity<byte[]> printPerson(String regNo) {
        StatefulDocument document = Defaults.getDocument(regNo, PageTemplate.A4rotate);

        PdfPTable table = new PdfPTable(1);
        table.addCell(Cells.get(regNo));
        document.add(table);

        String state = document.getState();
        byte[] contents = Files.readAllBytes(new File(state).toPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(state, state);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }
}
