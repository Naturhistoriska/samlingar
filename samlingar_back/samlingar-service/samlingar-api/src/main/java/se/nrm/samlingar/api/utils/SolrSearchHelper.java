package se.nrm.samlingar.api.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.YearMonth; 
import java.time.ZoneOffset; 
import java.time.format.DateTimeFormatter; 
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author idali
 */
@Slf4j
public class SolrSearchHelper {

    private final String colon = ":";
    private final String wildCard = "*";
    private final String fromZoom = ":00Z";
//    private final String toZoom = "Z";
    private final String toDate = " TO *]";
//    private final String squareEnd = "]";

    private final String emptySpace = " ";
    private final String star = "*";
    private final String quotationMark = "\"";

    private StringBuilder dateRangeSb;
    private YearMonth yearMonth;
    private int yearOfToday;
    private int lastTenYear;
    private int nextYear;

    private StringBuilder fuzzySeachTextSb;
    private StringBuilder collectionCodeSearchSb;
    
    private LocalDate firstDayOfYear;
    private LocalDate tomorrow;
 
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm zzzz");
 
    

    private LocalDateTime startDate;
    private String[] searchText;

    private static SolrSearchHelper instance = null;

    public static SolrSearchHelper getInstance() {
        synchronized (SolrSearchHelper.class) {
            if (instance == null) {
                instance = new SolrSearchHelper();
            }
        }
        return instance;
    }
    
    public String getStartDate(boolean isYearChart) {
        return isYearChart ? getFirstDayOfLastTenYears() : getFirstDayOfLastTwelveMohth();
    }
     
    public String getEndDate() {  
        tomorrow = LocalDate.now()
                .plusDays(1); 
        return convertLocalDateToString(tomorrow); 
    }
     
    private String getFirstDayOfLastTenYears() {
        firstDayOfYear = LocalDate.now()
                .minusYears(10)
                .withDayOfYear(1);
        
        return convertLocalDateToString(firstDayOfYear);
    }
    
    private String getFirstDayOfLastTwelveMohth() {
        firstDayOfYear = LocalDate.now()
                .minusMonths(11)
                .withDayOfMonth(1);  
        return convertLocalDateToString(firstDayOfYear);
    }
    
    private String convertLocalDateToString(LocalDate date) {
        OffsetDateTime utcDateTime = date
                .atTime(LocalTime.MIDNIGHT)
                .atOffset(ZoneOffset.UTC); 
        return utcDateTime.format(DateTimeFormatter.ISO_INSTANT);  
    }
    
  
 
//    public String getFirstDayOfCurrentYear() {  
//        firstDayOfYear = LocalDate.now().withDayOfYear(1);  
//        return convertLocalDateToString(firstDayOfYear); 
//    }

    
    public String buildSearchCollectionCode(String collectionCodeKey, String collectionCodeValue) {
        
        collectionCodeValue = collectionCodeValue == null ? wildCard : collectionCodeValue;
        collectionCodeSearchSb = new StringBuilder(); 
        collectionCodeSearchSb.append(collectionCodeKey);
        collectionCodeSearchSb.append(colon); 
        collectionCodeSearchSb.append(collectionCodeValue); 
        return collectionCodeSearchSb.toString().trim();
    }

    public String buildSearchText(String text, String key, boolean fuzzySearch) {

        fuzzySeachTextSb = new StringBuilder(); 
           
        if(text == null || text.isEmpty() || text.equals(star)) {
            fuzzySeachTextSb.append(key);
            fuzzySeachTextSb.append(colon);
            fuzzySeachTextSb.append(star);
            return fuzzySeachTextSb.toString().trim(); 
        }
         
        if (fuzzySearch) {
            if (text.contains(emptySpace)) {
                searchText = text.split(emptySpace);
                for (String value : searchText) {  
                    fuzzySeachTextSb.append(key);
                    fuzzySeachTextSb.append(colon);
                    fuzzySeachTextSb.append(star);
                    fuzzySeachTextSb.append(value);
                    fuzzySeachTextSb.append(star);
                    fuzzySeachTextSb.append(emptySpace);
                } 
            } else {
                fuzzySeachTextSb.append(key);
                fuzzySeachTextSb.append(colon);
                fuzzySeachTextSb.append(text); 
                fuzzySeachTextSb.append(emptySpace);

                fuzzySeachTextSb.append(key);
                fuzzySeachTextSb.append(colon);
                fuzzySeachTextSb.append(star);
                fuzzySeachTextSb.append(text);
                fuzzySeachTextSb.append(star);
                fuzzySeachTextSb.append(emptySpace);

                fuzzySeachTextSb.append(key);
                fuzzySeachTextSb.append(colon);
                fuzzySeachTextSb.append(StringUtils.capitalize(text));
                fuzzySeachTextSb.append(star);
            }
        } else {
            fuzzySeachTextSb.append(key);
            fuzzySeachTextSb.append(colon);
            fuzzySeachTextSb.append(quotationMark);
            fuzzySeachTextSb.append(text);
            fuzzySeachTextSb.append(quotationMark);
        }

        return fuzzySeachTextSb.toString().trim();
    }

    public String buildTwelveMonthDateRange() {

        yearMonth = YearMonth.from(LocalDate.now());
        yearOfToday = yearMonth.getYear();
        startDate = yearMonth.minusMonths(11).atDay(1).atStartOfDay();

        dateRangeSb = new StringBuilder();
        dateRangeSb.append("catalogedDate:[");
        dateRangeSb.append(startDate);
        dateRangeSb.append(fromZoom);
        dateRangeSb.append(toDate);
        log.info("date range : {}", dateRangeSb.toString());
        return dateRangeSb.toString().trim();
    }

}
