package sn.ucad.office.pjobac.modules.pdf;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sn.ucad.office.pjobac.config.AppConstants;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import java.awt.*;
import java.io.*;

import java.io.FileOutputStream;

@Service
@Slf4j
//@RequiredArgsConstructor
public class PdfGeneratorImp implements PdfGenerator{
    @Override
    public Document test1() throws BusinessResourceException  {
        log.info("Generating hello world from PdfGenerator::test1");

        // Step 1:
        Document document = new Document();
        try{
            // Step 2:
            PdfWriter.getInstance(document, new FileOutputStream("HelloWorld.pdf"));

            // Step 3:
            document.open();

            // Step 4:
            document.add(new Paragraph("Hello World"));

            // Step 5:
            document.close();

            return document;
        } catch (DocumentException de) {
            log.error("Generating hello world from PdfGenerator::test1-error: {}",de.getMessage());
            throw new BusinessResourceException("document-exception", "Erreur sur le fichier ", HttpStatus.BAD_REQUEST);
        } catch (FileNotFoundException fne) {
            log.error("Generating hello world from PdfGenerator::test1-error: {}",fne.getMessage());
            throw new BusinessResourceException("file-not-found-exception", "Fichier non trouvé ", HttpStatus.NOT_FOUND);
        } catch (IOException ioe) {
            log.error("Generating hello world from PdfGenerator::test1-error: {}",ioe.getMessage());
            throw new BusinessResourceException("io-exception", "Erreur inattendue ", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @Override
    public void helloworld() throws BusinessResourceException {
        log.info("Generating hello world from PdfGenerator::test1");

        // Step 1:
        Document document = new Document();
        try{
            // Step 2:
            PdfWriter.getInstance(document, new FileOutputStream("HelloWorld.pdf"));
            // Step 3:
            document.open();
            // Step 4:
            document.add(new Paragraph("Hello World"));
            // Step 5:
            document.close();
        } catch (DocumentException de) {
            log.error("Generating hello world from PdfGenerator::test1-error: {}",de.getMessage());
            throw new BusinessResourceException("document-exception", "Erreur sur le fichier ", HttpStatus.BAD_REQUEST);
        } catch (FileNotFoundException fne) {
            log.error("Generating hello world from PdfGenerator::test1-error: {}",fne.getMessage());
            throw new BusinessResourceException("file-not-found-exception", "Fichier non trouvé ", HttpStatus.NOT_FOUND);
        } catch (IOException ioe) {
            log.error("Generating hello world from PdfGenerator::test1-error: {}",ioe.getMessage());
            throw new BusinessResourceException("io-exception", "Erreur inattendue ", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @Override
    public void generate(HttpServletResponse response) throws DocumentException, IOException, BusinessResourceException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to modify it
        document.open();
        // Creating font, setting font style and size
        Font fontTitleUcadTimesRoman = new Font(Font.TIMES_ROMAN, 20, Font.BOLD, Color.BLACK);
        // Font fontTitleUcadTimesRoman =  FontFactory.getFont(FontFactory.TIMES_ROMAN);
        //fontTitleUcadTimesRoman.setSize(20);

        Font fontTitleUcadHelvetica = new Font(Font.HELVETICA, 16, Font.BOLD, Color.BLACK);

        // Adding Ucad Logo
        Image logoUcad = Image.getInstance("/home/oem/Documents/wspace/projets-dev/disi/ipms/api/demande-service/src/main/resources/logo_ucad.png");
        //logoUcad.scaleAbsolute(150f,150f);
        // Creating paragraph
        Paragraph titleUcad = new Paragraph(AppConstants.TITLE_UCAD, fontTitleUcadTimesRoman);
        Paragraph titleRectorat = new Paragraph(AppConstants.TITLE_RECTORAT, fontTitleUcadHelvetica);
        Paragraph titleBp = new Paragraph(AppConstants.TITLE_BP, fontTitleUcadHelvetica);
        Paragraph titleTeleFax = new Paragraph(AppConstants.TITLE_TELE_FAX, fontTitleUcadHelvetica);
        Paragraph titleImput = new Paragraph(AppConstants.TITLE_IMPUTATION, fontTitleUcadTimesRoman);

        // Aligning the paragraph in document
        titleUcad.setAlignment(Paragraph.ALIGN_CENTER);
        titleRectorat.setAlignment(Paragraph.ALIGN_CENTER);
        titleBp.setAlignment(Paragraph.ALIGN_CENTER);
        titleTeleFax.setAlignment(Paragraph.ALIGN_CENTER);
        titleImput.setAlignment(Paragraph.ALIGN_CENTER);

        // Adding the created paragraph in document
        document.add(titleUcad);
        document.add(titleRectorat);
        document.add(titleBp);
        document.add(titleTeleFax);
        document.add(new Paragraph("\n"));
        document.add(titleImput);

        //  Table
        Table eneteTable = new Table(2);

        //Cell cellEntete = new Cell();
        //cellEntete.add(logoUcad);
        //cellEntete.add(pTitleUcad);


        //eneteTable.addCell(new Cell(logoUcad));
        //eneteTable.addCell(new Cell(pTitleUcad));
        //document.add(eneteTable);

        // Adding another content
        //  ***
        Paragraph p1 = new Paragraph(new Chunk(
                titleUcad.getContent(),
                fontTitleUcadTimesRoman)
        );
        p1.add(new Paragraph(AppConstants.TITLE_RECTORAT, fontTitleUcadHelvetica));
        p1.add(new Paragraph(AppConstants.TITLE_BP, fontTitleUcadHelvetica));
        p1.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p1);
        /*Paragraph p1 = new Paragraph(new Chunk(
                "This is my first paragraph. ",
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
        p1.add("The leading of this paragraph is calculated automagically. ");
        p1.add("The default leading is 1.5 times the fontsize. ");
        p1.add(new Chunk("You can add chunks "));
        p1.add(new Phrase("or you can add phrases. "));
        p1.add(new Phrase(
                "Unless you change the leading with the method setLeading, the leading doesn't change if you add text with another leading. This can lead to some problems.",
                FontFactory.getFont(FontFactory.HELVETICA, 18)));
        document.add(p1);
        Paragraph p2 = new Paragraph(new Phrase(
                "This is my second paragraph. ", FontFactory.getFont(
                FontFactory.HELVETICA, 12)));
        p2.add("As you can see, it started on a new line.");
        document.add(p2);
        Paragraph p3 = new Paragraph("This is my third paragraph.",
                FontFactory.getFont(FontFactory.HELVETICA, 12));
        document.add(p3);*/
        //  ***
        // Closing the document
        document.close();
    }
}
/*
* Source: https://techblogstation.com/spring-boot/export-data-to-pdf-in-spring-boot/
* */
