package sn.ucad.office.pjobac.modules.pdf.v1;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.ucad.office.pjobac.modules.pdf.PdfGenerator;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/ipms/api/v1/pdf-generator")
public class PdfGeneratorResource {
    private final PdfGenerator service;

    @GetMapping(value = "/hello-word")
    public ResponseEntity<Void> del() {
        service.helloworld();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping(value = "/hello1", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Document> hello1() throws IOException {
        Document document = service.test1();
        HttpHeaders headers = new HttpHeaders();
        String fileName = "example.pdf";
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(document);
    }
    @GetMapping(value = "/previewPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> previewPDF() throws IOException {
        byte[] pdf = null;
        HttpHeaders headers = new HttpHeaders();
        String fileName = "example.pdf";
        headers.setContentDispositionFormData(fileName, fileName);
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
    @GetMapping(value = "/imputation")
    public void generatePdf(HttpServletResponse response) throws DocumentException, IOException{
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        service.generate(response);
    }
}
