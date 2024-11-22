package se.nrm.samlingar.api.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author idali
 */
@Slf4j
public class SolrSearchHelper {

    private final String querySeparator = ":";
    private final String wildCard = "*";
    private final String fromZoom = ":00Z";
    private final String toZoom = "Z";
    private final String toDate = " TO *]";
    private final String squareEnd = "]";

    private StringBuilder dateRangeSb;
    private YearMonth yearMonth;
    private int yearOfToday;
    private int lastTenYear;
    private int nextYear;

    private LocalDateTime startDate;

    private static SolrSearchHelper instance = null;

    public static SolrSearchHelper getInstance() {
        synchronized (SolrSearchHelper.class) {
            if (instance == null) {
                instance = new SolrSearchHelper();
            }
        }
        return instance;
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
