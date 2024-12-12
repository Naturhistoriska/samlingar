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
    private final String signs = "-/";
    private final String colon = ":";
    private final String emptySpace = " ";

    private final String zeroOne = "01";
    private final String zero = "0";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final DateTimeFormatter ymDtf = DateTimeFormatter.ofPattern("uuuu-M");
    private final DateTimeFormatter yDtf = DateTimeFormatter.ofPattern("uuuu");

    private int intMonth;
    private int intDay;

    private String[] dateArray;
    private StringBuilder dateSb;

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
        if (StringUtils.isAllBlank(strDate)) {
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
//            log.error("invalid date: {}", strDate);
//            return fixInvalidDate(strDate);
            return null;
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
        } catch (DateTimeParseException e) {
            log.error("DateTimeParseException: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return null;
        }
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

    public boolean isStringContainsSign(String string) {
        return StringUtils.containsAny(string, signs);
    }

    public String buildDate(String date) {

        if (date.contains(slash)) {
            dateArray = date.split(slash);
        } else {
            dateArray = date.split(dash);
        }
        dateSb = new StringBuilder();

        if (dateArray.length <= 3 && dateArray[0].length() == 4) {
            if (dateArray.length == 1) {
                dateSb.append(date);
                dateSb.append(dash);
                dateSb.append(zeroOne);
                dateSb.append(dash);
                dateSb.append(zeroOne);
                return dateSb.toString().trim();
            }

            if (dateArray.length == 2) { 
                if(dateArray[1].length() > 2) {
                    return null;
                } else {
                    if (dateArray[1].length() == 1) {
                        dateSb.append(dateArray[0]);
                        dateSb.append(dash);
                        dateSb.append(zero);
                        dateSb.append(dateArray[1]);
                        dateSb.append(dash);
                        dateSb.append(zeroOne); 
                    } else {
                        dateSb.append(date);
                        dateSb.append(dash);
                        dateSb.append(zeroOne);  
                    } 
                    return dateSb.toString().trim();
                } 
            } else if (dateArray.length == 3) {
                return date;
            }
        } else {
            return null;
        } 
        return null;
    }

}
