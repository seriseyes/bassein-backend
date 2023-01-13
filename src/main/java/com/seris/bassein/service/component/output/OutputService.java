package com.seris.bassein.service.component.output;

import com.itextpdf.text.pdf.PdfPTable;
import com.seris.bassein.model.Response;
import com.seris.bassein.util.Utils;
import com.seris.bassein.util.pdf.Cells;
import com.seris.bassein.util.pdf.Defaults;
import com.seris.bassein.util.pdf.PageTemplate;
import com.seris.bassein.util.pdf.StatefulDocument;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public record OutputService() {
    @SneakyThrows
    public ResponseEntity<byte[]> printBassein(LocalDate dateFrom, LocalDate dateTo) {
        StatefulDocument document = Defaults.getDocument("Бассейны хэмжээнд", PageTemplate.A4rotate);

        PdfPTable table = new PdfPTable(1);
        table.addCell(Cells.get(dateFrom.format(Utils.dateFormat)).add(" - ").add(dateTo.format(Utils.dateFormat)));
        document.add(table);

        return Response.pdf(document);
    }

    @SneakyThrows
    public ResponseEntity<byte[]> printPerson(String regNo) {
        StatefulDocument document = Defaults.getDocument(regNo, PageTemplate.A4rotate);

        PdfPTable table = new PdfPTable(1);
        table.addCell(Cells.get(regNo));
        document.add(table);

        return Response.pdf(document);
    }
}
