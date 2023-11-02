package sn.ucad.office.pjobac.modules.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import java.io.IOException;

public interface PdfGenerator {
    public Document test1() throws BusinessResourceException;
    public void helloworld() throws BusinessResourceException;
    public void generate(HttpServletResponse response) throws DocumentException, IOException, BusinessResourceException;
}
