package com.truevocation.front.controller;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.truevocation.domain.User;
import com.truevocation.repository.PortfolioRepository;
import com.truevocation.repository.UserRepository;
import com.truevocation.security.SecurityUtils;
import com.truevocation.service.PortfolioService;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import javax.servlet.http.HttpServletResponse;

import javax.validation.Valid;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/front")
public class ExportController {

    private final PortfolioService portfolioService;

    private final PortfolioRepository portfolioRepository;

    private final UserRepository userRepository;

    public ExportController(PortfolioService portfolioService, PortfolioRepository portfolioRepository, UserRepository userRepository) {
        this.portfolioService = portfolioService;
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/portfolio/export-to-pdf")
    public void updatePortfolio(HttpServletResponse response) throws URISyntaxException, IOException, DocumentException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<User> listUsers = userRepository.findAll();


        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.WHITE);

        Paragraph p = new Paragraph("List of Users", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
        PdfPTable table = new PdfPTable(3);

        Stream.of("column header 1", "column header 2", "column header 3")
            .forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(Color.WHITE);
                header.setBorderWidth(0);
                header.setBorderColor(Color.WHITE);
                header.setPhrase(new Phrase(columnTitle));
                table.addCell(header);
            });
        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");


        PdfPCell imageCell = new PdfPCell(new Phrase("row 1, col 1"));
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
        document.open();
        Path path = Paths.get(ClassLoader.getSystemResource("static/logo.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        document.add(img);

        document.add(table);
        document.add(p);
        document.close();

    }
}
