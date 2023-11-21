package sn.ucad.office.pjobac.utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import sn.ucad.office.pjobac.exception.BusinessResourceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class AppDateFormatter {
    public Date dateFormat(String dateToFomrat, String format) throws ParseException, BusinessResourceException {
        try {
            SimpleDateFormat formattter = new SimpleDateFormat(format);
            Date date;
            date = formattter.parse(dateToFomrat);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessResourceException("IncorrectValue", "Une date incorrecte est passée en paramétre. <dateFormat>.", HttpStatus.BAD_REQUEST);
        }
    }

}
