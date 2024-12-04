package se.nrm.samlingar.data.process.logic.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author idali
 */
@Slf4j
public class Util {

    private StringBuilder csvFileSb;

    private final String dash = "-";
    private final String slash = "/";
    private final String colon = ":";
    private final String emptySpace = " ";
    


    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final DateTimeFormatter ymDtf = DateTimeFormatter.ofPattern("uuuu-MM");
    private final DateTimeFormatter yDtf = DateTimeFormatter.ofPattern("uuuu");
     
    private int intMonth;
    private int intDay;

    private static Util instance = null;

    public static Util getInstance() {
        synchronized (Util.class) {
            if (instance == null) {
                instance = new Util();
            }
        }
        return instance;
    }

    public String buildCsvFilePath(String filePath, String fileName) {
        log.info("buildCsvFilePath : {} -- {}", filePath, fileName);
        csvFileSb = new StringBuilder();
        csvFileSb.append(filePath);
        csvFileSb.append(fileName);

        return csvFileSb.toString().trim();
    }

    public LocalDate stringToLocalDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        try {
            if (strDate.contains(slash)) {
                return LocalDate.parse(strDate, formatter);
            } else if (strDate.contains(colon)) {
                return LocalDate.parse(StringUtils.substringBefore(strDate, emptySpace));
            }
            return LocalDate.parse(strDate);
        } catch (DateTimeException ex) {
            log.error("invalid date: {}", strDate);
            return fixInvalidDate(strDate);
        }
    }

    public LocalDate fixInvalidDate(String strDate) {
        String[] array = strDate.split(dash);

        if (array.length == 1) {
            Year year = Year.parse(strDate, yDtf);
            return year.atDay(1);
        } else if (array.length == 2) {
            YearMonth yearAndMonth = YearMonth.parse(strDate, ymDtf);
            return yearAndMonth.atDay(1);
        }
        return null;
    }

    public LocalDate fixDate(String year, String month, String day) { 
        
        if (StringUtils.isBlank(year)) {
            return null;
        }
  
        if (StringUtils.isBlank(month)) {
            return Year.parse(year, yDtf).atDay(1);
        }
 
        intMonth = stringToInt(month);
        intDay = StringUtils.isBlank(day) ? 1 : stringToInt(day); 
        
        try { 
            return Year.parse(year, yDtf).atMonth(intMonth).atDay(intDay);   
        } catch(DateTimeParseException e) {
            log.error("DateTimeParseException: {}", e.getMessage());
        } catch(Exception e) {
            log.error("Exception: {}", e.getMessage());
        }
        return null; 
    }

    public int stringToInt(String value) { 
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) { 
            throw new NumberFormatException(ex.getMessage());
        }
    }
    
    public double stringToDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }
}
