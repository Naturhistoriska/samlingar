package se.nrm.samlingar.api.utils;
 
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList; 
import java.util.List; 
import lombok.extern.slf4j.Slf4j; 
import org.apache.commons.lang3.StringUtils;  

/**
 *
 * @author idali
 */
@Slf4j
public class SolrSearchHelper {

    
    private final String regex = "([+\\-!(){}\\[\\]^\"~*?:\\\\|&])"; 
    private final String regexReplecement = "\\\\$1";
    private final String colon = ":";
    private final String spaceSeparate = "\\s+";
    
     
    
    

    private final String plusSign = "+";
    private final String colonEscap = "\\:";

    private final String leftBracket = "(";
    private final String rightBracket = ")";

    private final String emptySpace = " ";

    private final String quotationMark = "\"";

    private StringBuilder collectionCodeSearchSb;

    private LocalDate firstDayOfYear;
    private LocalDate tomorrow;

//    private final String basisOfRecordKey = "basisOfRecord";
    private final String catalogNumberKey = "catalogNumber";
    private final String collectionNameKey = "collectionName";
    private final String expeditionNameKey = "expeditionName";
    private final String preservationKey = "preservation";
    private final String preparationsKey = "preparations";
    private final String recordedByKey = "recordedBy";
    private final String lifeStageKey = "lifeStage";
//    private final String licenseKey = "license";
//    private final String individualCountKey = "individualCount";
    private final String occurrenceRemarksKey = "occurrenceRemarks";
    private final String sexKey = "sex";

    private final String countryKey = "country";
    private final String stateProvinceKey = "stateProvince";
    private final String continentKey = "continent";
    private final String decimalLongitudeKey = "decimalLongitude";
    private final String decimalLatitudeKey = "decimalLatitude";
    private final String localityKey = "locality";
    private final String minimumElevationInMetersKey = "minimumElevationInMeters";
    private final String maximumElevationInMetersKey = "maximumElevationInMeters";
    private final String minimumDepthInMetersKey = "minimumDepthInMeters";
    private final String maximumDepthInMetersKey = "maximumDepthInMeters";
    private final String waterBodyKey = "waterBody";
    private final String eventDateKey = "eventDate";
    private final String yearKey = "year";
    private final String monthKey = "month";
    private final String dayKey = "day";
    private final String fieldNumberKey = "fieldNumber";
    private final String habitatKey = "habitat";
    private final String typeStatusKey = "typeStatus";

    private final String kingdomKey = "kingdom";
    private final String phylumKey = "phylum";
    private final String classKey = "clazz";
    private final String orderKey = "order";
    private final String familyKey = "family";
    private final String genusKey = "genus";
    private final String subgenusKey = "subgenus";
    private final String speciesKey = "species";

    private final String scientificNameAuthorshipKey = "scientificNameAuthorship";
    private final String specificEpithetKey = "specificEpithet";
    private final String synonymsKey = "copy_synonyms";
    private final String vernacularNameKey = "vernacularName";

    private final String synonymsFieldKey = "synonyms:";

//    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm zzzz");
    private final String startsWith = "startsWith";
    private final String contains = "contains";
    private final String equals = "equals";
    
    

//    private LocalDateTime startDate;
    private String[] searchText;
    private List<String> fields;

    private static SolrSearchHelper instance = null;
    
    private StringBuilder seachTextSb;

//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
    
    
         
    private final String leftBlacket = "[";
    private final String rightBlacket = "]";
    
    private final String to = " TO ";
    private final String toWithStar = " TO *]";
    
    private final String catchallWithStar = "catchall:*";
    private final String catchallFieldKey = "catchall:";
    private final String catchallKey = "catchall";
    
    
    
    
    private final String scientificNameKey = "scientificName";

    private StringBuilder fuzzySeachTextSb;
    private StringBuilder freeTextSearchSb;
    private StringBuilder dateRangeSb;
    
    
    private StringBuilder synonymsSb;

    private final String star = "*";

    private final String scientificNameWithStar = "copy_scientificName:*";
    private final String and = " AND ";

    public static SolrSearchHelper getInstance() {
        synchronized (SolrSearchHelper.class) {
            if (instance == null) {
                instance = new SolrSearchHelper();
            }
        }
        return instance;
    }
    
    public String buildFreeTextSearch(String text, String mode) {
        if(text == null || text.isEmpty() || text.equals(star)) {
            return catchallWithStar;
        } 
        
        text = text.replaceAll(regex, regexReplecement);
        if(mode == null) {
             return buildCaseInsensitiveContainsSearchText(text, catchallKey);
        }
        
        switch (mode) {
            case contains:
                return buildCaseInsensitiveContainsSearchText(text, catchallKey);
            case equals:
                return buildCaseInsensitivEqualsSearchText(text, catchallKey);
            case startsWith:
                return buildCaseInsensitiveStartsWithSearchText(text, catchallKey);
            default:
                return buildCaseInsensitiveContainsSearchText(text, catchallKey); 
        } 
    }
    
    
    public String buildContainsQuery(String text) {
        seachTextSb = new StringBuilder();
        if (text == null || text.isEmpty() || text.equals(star)) {
            seachTextSb.append(catchallWithStar); 
        } else { 
            text = text.replaceAll(regex, regexReplecement);
            
            String[] terms = text.split(spaceSeparate); 
            
            for (int i = 0; i < terms.length; i++) {
                seachTextSb.append(catchallWithStar).append(terms[i]).append(star);
                if (i < terms.length - 1) {
                    seachTextSb.append(and);
                }
            } 
        } 
        return seachTextSb.toString();
    }
     
    public String buildDateRange(String startDate, String endDate) {
        
        dateRangeSb = new StringBuilder();
        if(!StringUtils.isBlank(startDate)) {
            dateRangeSb.append(eventDateKey);
            dateRangeSb.append(colon);
            dateRangeSb.append(leftBlacket);
            dateRangeSb.append(startDate);
            
            if(!StringUtils.isBlank(endDate)) {
                dateRangeSb.append(to);
                dateRangeSb.append(endDate);
                dateRangeSb.append(rightBlacket);
            } else {
                dateRangeSb.append(toWithStar);
            }
            return dateRangeSb.toString().trim();
        } else {
            return null;
        } 
    }
    
    
    public String buildScientificName(String text, String key, String searchMode ) {

        searchMode = searchMode == null ? contains : searchMode; 
         
        text = text.replaceAll(regex, regexReplecement);
        switch (searchMode) {
            case equals:
                return buildCaseInsensitivEqualsSearchText(text, key);
            case startsWith:
                return buildCaseInsensitiveStartsWithSearchText(text, key);
            default:
                return buildScientificNameContainsQuery(text); 
//                return buildCaseInsensitiveContainsSearchText(text, key); 
        }
    }
     
    public String buildScientificNameContainsQuery(String text) {
        seachTextSb = new StringBuilder();
 
        String[] terms = text.split(spaceSeparate);
        for (int i = 0; i < terms.length; i++) { 
            seachTextSb.append(scientificNameWithStar).append(terms[i]).append(star);
            if (i < terms.length - 1) {
                seachTextSb.append(and);
            }
        } 
        return seachTextSb.toString();
    }
        
    public String buildSynonyms(String synonyms) {
        synonymsSb = new StringBuilder(); 
        synonyms = synonyms.replaceAll(regex, regexReplecement);
         
        return buildCaseInsensitiveStartsWithSearchText(synonyms, synonymsKey);
    }
    
        
    public String buildAutoCompleteSearchText(String text, String key, 
            boolean isCaseSensitive) {
        
        log.info("isCaseSensitive : {}", isCaseSensitive);
        log.info("buildAutoCompleteSearchText: {}--{}", text, key);
        
        seachTextSb = new StringBuilder();
        if(isCaseSensitive) { 
            return buildContainsSearchText(text, key);
        } else {
            return buildCaseInsensitiveContainsSearchText(text, key);
        } 
    }
    
    
    private String buildStartsWithSearchText(String text, String key) {
        fuzzySeachTextSb = new StringBuilder();
        if (text.contains(emptySpace)) {
            searchText = text.split(emptySpace);

            fuzzySeachTextSb.append(plusSign);
            fuzzySeachTextSb.append(key);
            fuzzySeachTextSb.append(colon);
            fuzzySeachTextSb.append(StringUtils.capitalize(searchText[0]));
            fuzzySeachTextSb.append(star);

            for (int i = 1; i < searchText.length; i++) {
                fuzzySeachTextSb.append(emptySpace);
                fuzzySeachTextSb.append(plusSign);
                fuzzySeachTextSb.append(key);
                fuzzySeachTextSb.append(colon);
                fuzzySeachTextSb.append(star);
                fuzzySeachTextSb.append(searchText[i]);
                fuzzySeachTextSb.append(star);
            }
        } else {
            fuzzySeachTextSb.append(key);
            fuzzySeachTextSb.append(colon);
            fuzzySeachTextSb.append(StringUtils.capitalize(text));
            fuzzySeachTextSb.append(star);
        }

        return fuzzySeachTextSb.toString().trim();
    }
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
    
    
    
    
    
    
    
    
    
    


//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    

     
    
    private String buildCaseInsensitivEqualsSearchText(String text, String key) {
        seachTextSb = new StringBuilder();
        seachTextSb.append(key);
        seachTextSb.append(colon);
        seachTextSb.append(quotationMark);
        seachTextSb.append(text);
        seachTextSb.append(quotationMark);
        return seachTextSb.toString().trim();
    }
    
    private String buildCaseInsensitiveStartsWithSearchText(String text, String key) {
        seachTextSb = new StringBuilder();
        if (text.contains(emptySpace)) {
            searchText = text.split(emptySpace);

            seachTextSb.append(plusSign);
            seachTextSb.append(key);
            seachTextSb.append(colon);
            seachTextSb.append(searchText[0]);
            seachTextSb.append(star);

            for (int i = 1; i < searchText.length; i++) {
                seachTextSb.append(emptySpace);
                seachTextSb.append(plusSign);
                seachTextSb.append(key);
                seachTextSb.append(colon);
                seachTextSb.append(star);
                seachTextSb.append(searchText[i]);
                seachTextSb.append(star);
            }
        } else {
            seachTextSb.append(key);
            seachTextSb.append(colon);
            seachTextSb.append(text);
            seachTextSb.append(star);
        }

        return seachTextSb.toString().trim();
    }
 
    
    private String buildCaseInsensitiveContainsSearchText(String text, String key) {
        seachTextSb = new StringBuilder();
        if (text.contains(emptySpace)) {
            
            searchText = text.split(emptySpace); 
            for (String value : searchText) {
                seachTextSb.append(plusSign);
                seachTextSb.append(key);
                seachTextSb.append(colon);
                seachTextSb.append(star);
                seachTextSb.append(value);
                seachTextSb.append(star);
                seachTextSb.append(emptySpace);
            } 
        } else {
            seachTextSb.append(key);
            seachTextSb.append(colon); 
            seachTextSb.append(star);
            seachTextSb.append(text);
            seachTextSb.append(star);  
        }
        return seachTextSb.toString().trim();
    }
    
//    public String buildFreesearchText(String text) { 
//        return buildCaseInsensitiveContainsSearchText(text, catchallKey); 
//    }
    
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

    
    
     private String buildContainsSearchText(String text, String key) {
         log.info("buildContainsSearchText : {} -- {}", text, key);
        seachTextSb = new StringBuilder();
        boolean isUpperCase = Character.isUpperCase(text.charAt(0));

        if (text.contains(emptySpace)) {
            searchText = text.split(emptySpace);

            if (isUpperCase) {
                for (String value : searchText) {
                    seachTextSb.append(plusSign);
                    seachTextSb.append(key);
                    seachTextSb.append(colon);
                    seachTextSb.append(star);
                    seachTextSb.append(value);
                    seachTextSb.append(star);
                    seachTextSb.append(emptySpace);
                }
            } else {
                seachTextSb.append(leftBracket);
                seachTextSb.append(plusSign);
                seachTextSb.append(key);
                seachTextSb.append(colon);
                seachTextSb.append(star);
                seachTextSb.append(StringUtils.capitalize(searchText[0]));
                seachTextSb.append(star);

                for (int i = 1; i < searchText.length; i++) {
                    seachTextSb.append(emptySpace);
                    seachTextSb.append(plusSign);
                    seachTextSb.append(key);
                    seachTextSb.append(colon);
                    seachTextSb.append(star);
                    seachTextSb.append(searchText[i]);
                    seachTextSb.append(star);
                }
                seachTextSb.append(rightBracket);

                seachTextSb.append(emptySpace);
                seachTextSb.append(leftBracket);
                for (String value : searchText) {
                    seachTextSb.append(plusSign);
                    seachTextSb.append(key);
                    seachTextSb.append(colon);
                    seachTextSb.append(star);
                    seachTextSb.append(value);
                    seachTextSb.append(star);
                    seachTextSb.append(emptySpace);
                }
                seachTextSb.append(rightBracket);
            }
        } else {
            seachTextSb.append(key);
            seachTextSb.append(colon);
            seachTextSb.append(leftBracket);
            seachTextSb.append(star);
            seachTextSb.append(text);
            seachTextSb.append(star);
            seachTextSb.append(emptySpace);

            seachTextSb.append(star);
            seachTextSb.append(StringUtils.capitalize(text));
            seachTextSb.append(star);
            seachTextSb.append(rightBracket);
        }
        return seachTextSb.toString().trim();
    }


    
    
    
    
    
    
    
    
    
    
    
    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


    public String buildFreeTextSearch(String text) {

        freeTextSearchSb = new StringBuilder();

        if (text == null || text.isEmpty() || text.equals(star)) {
            freeTextSearchSb.append(catchallFieldKey);
            freeTextSearchSb.append(star);
            return freeTextSearchSb.toString().trim();
        }

        if (text.contains(emptySpace)) {
            searchText = text.split(emptySpace);
            for (String value : searchText) {
                freeTextSearchSb.append(catchallFieldKey);
                freeTextSearchSb.append(star);
                freeTextSearchSb.append(value);
                freeTextSearchSb.append(star);
                freeTextSearchSb.append(emptySpace);
            }
        } else {
            freeTextSearchSb.append(catchallFieldKey);
            freeTextSearchSb.append(star);
            freeTextSearchSb.append(text);
            freeTextSearchSb.append(star);
        }
        return freeTextSearchSb.toString();
    }

    public String buildScientificName(String text, boolean fuzzySearch) {
        fuzzySeachTextSb = new StringBuilder();
        if (fuzzySearch) {
            if (text.contains(emptySpace)) {
                searchText = text.split(emptySpace);
                for (String value : searchText) {
                    fuzzySeachTextSb.append(scientificNameKey);
                    fuzzySeachTextSb.append(colon);
                    fuzzySeachTextSb.append(star);
                    fuzzySeachTextSb.append(value);
                    fuzzySeachTextSb.append(star);
                    fuzzySeachTextSb.append(emptySpace);
                }
            } else {
                fuzzySeachTextSb.append(scientificNameKey);
                fuzzySeachTextSb.append(colon);
                fuzzySeachTextSb.append(text);
                fuzzySeachTextSb.append(emptySpace);

                fuzzySeachTextSb.append(scientificNameKey);
                fuzzySeachTextSb.append(colon);
                fuzzySeachTextSb.append(star);
                fuzzySeachTextSb.append(text);
                fuzzySeachTextSb.append(star);
                fuzzySeachTextSb.append(emptySpace);

                fuzzySeachTextSb.append(scientificNameKey);
                fuzzySeachTextSb.append(colon);
                fuzzySeachTextSb.append(StringUtils.capitalize(text));
                fuzzySeachTextSb.append(star);
            }
        } else {
            fuzzySeachTextSb.append(scientificNameKey);
            fuzzySeachTextSb.append(colon);
            fuzzySeachTextSb.append(quotationMark);
            fuzzySeachTextSb.append(text);
            fuzzySeachTextSb.append(quotationMark);
        }

        return fuzzySeachTextSb.toString().trim();
    }

//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
    public List<String> buildDataExportFields() {
        fields = new ArrayList(); 
        fields.add(catalogNumberKey);
        fields.add(collectionNameKey);
        fields.add(expeditionNameKey);
        fields.add(preservationKey);
        fields.add(preparationsKey);
        fields.add(recordedByKey);
        fields.add(lifeStageKey);  
        fields.add(occurrenceRemarksKey);
        fields.add(sexKey); 
        fields.add(countryKey);
        fields.add(stateProvinceKey);
        fields.add(continentKey);
        fields.add(decimalLongitudeKey);
        fields.add(decimalLatitudeKey);
        fields.add(localityKey);
        fields.add(minimumElevationInMetersKey);
        fields.add(maximumElevationInMetersKey);
        fields.add(minimumDepthInMetersKey);
        fields.add(maximumDepthInMetersKey);

        fields.add(waterBodyKey);
        fields.add(eventDateKey);
        fields.add(yearKey);
        fields.add(monthKey);
        fields.add(dayKey);
        fields.add(fieldNumberKey);
        fields.add(habitatKey);
        fields.add(typeStatusKey);
        fields.add(kingdomKey);
        fields.add(phylumKey);
        fields.add(classKey);
        fields.add(orderKey);
        fields.add(familyKey);

        fields.add(genusKey);
        fields.add(subgenusKey);
        fields.add(speciesKey);
        fields.add(scientificNameKey);
        fields.add(scientificNameAuthorshipKey);

        fields.add(specificEpithetKey);
//        fields.add(synonymsKey);
        fields.add(vernacularNameKey);

        return fields;
    }




  
    public String buildSearchCollectionCode(String collectionCodeKey, String collectionCodeValue) {

        collectionCodeValue = collectionCodeValue == null ? star : collectionCodeValue;
        collectionCodeSearchSb = new StringBuilder();
        collectionCodeSearchSb.append(collectionCodeKey);
        collectionCodeSearchSb.append(colon);
        collectionCodeSearchSb.append(collectionCodeValue);
        return collectionCodeSearchSb.toString().trim();
    }

    

   

    private String buildEqualsSearchText(String text, String key) {
        fuzzySeachTextSb.append(key);
        fuzzySeachTextSb.append(colon);
        fuzzySeachTextSb.append(quotationMark);
        fuzzySeachTextSb.append(StringUtils.capitalize(text));
        fuzzySeachTextSb.append(quotationMark);
        return fuzzySeachTextSb.toString().trim();
    }



    
    public String buildSearchText(String text, String key, String searchMode ) {

        searchMode = searchMode == null ? equals : searchMode;
        fuzzySeachTextSb = new StringBuilder();

        if (text == null || text.isEmpty() || text.equals(star)) {
            fuzzySeachTextSb.append(key);
            fuzzySeachTextSb.append(colon);
            fuzzySeachTextSb.append(star);
            return fuzzySeachTextSb.toString().trim();
        }

        text = text.replaceAll("([+\\-!(){}\\[\\]^\"~*?:\\\\|&])", "\\\\$1");

        if (searchMode.equals(startsWith)) {
            return buildStartsWithSearchText(text, key);
        }

        if (searchMode.equals(contains)) {
            return buildContainsSearchText(text, key);
        }

        if (searchMode.equals(equals)) {
            return buildEqualsSearchText(text, key);
        }
        return fuzzySeachTextSb.toString().trim();

    }

//    private String buildSearchText(String text, String key, boolean fuzzySearch) {
//
//        fuzzySeachTextSb = new StringBuilder();
//
//        if (text == null || text.isEmpty() || text.equals(star)) {
//            fuzzySeachTextSb.append(key);
//            fuzzySeachTextSb.append(colon);
//            fuzzySeachTextSb.append(star);
//            return fuzzySeachTextSb.toString().trim();
//        }
//
//        if (fuzzySearch) {
//            if (text.contains(emptySpace)) {
//                searchText = text.split(emptySpace);
//                for (String value : searchText) {
//                    fuzzySeachTextSb.append(key);
//                    fuzzySeachTextSb.append(colon);
//                    fuzzySeachTextSb.append(star);
//                    fuzzySeachTextSb.append(value);
//                    fuzzySeachTextSb.append(star);
//                    fuzzySeachTextSb.append(emptySpace);
//                }
//            } else {
//                fuzzySeachTextSb.append(key);
//                fuzzySeachTextSb.append(colon);
//                fuzzySeachTextSb.append(text);
//                fuzzySeachTextSb.append(emptySpace);
//
//                fuzzySeachTextSb.append(key);
//                fuzzySeachTextSb.append(colon);
//                fuzzySeachTextSb.append(star);
//                fuzzySeachTextSb.append(text);
//                fuzzySeachTextSb.append(star);
//                fuzzySeachTextSb.append(emptySpace);
//
//                fuzzySeachTextSb.append(key);
//                fuzzySeachTextSb.append(colon);
//                fuzzySeachTextSb.append(StringUtils.capitalize(text));
//                fuzzySeachTextSb.append(star);
//            }
//        } else {
//            fuzzySeachTextSb.append(key);
//            fuzzySeachTextSb.append(colon);
//            fuzzySeachTextSb.append(quotationMark);
//            fuzzySeachTextSb.append(text);
//            fuzzySeachTextSb.append(quotationMark);
//        }
//
//        return fuzzySeachTextSb.toString().trim();
//    } 
}
