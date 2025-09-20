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

    private final String colon = ":";

    private final String plusSign = "+";
    private final String colonEscap = "\\:";

    private final String leftBracket = "(";
    private final String rightBracket = ")";

    private final String emptySpace = " ";

    private final String quotationMark = "\"";

    private StringBuilder collectionCodeSearchSb;

    private LocalDate firstDayOfYear;
    private LocalDate tomorrow;

    private final String basisOfRecordKey = "basisOfRecord";
    private final String catalogNumberKey = "catalogNumber";
    private final String collectionNameKey = "collectionName";
    private final String expeditionNameKey = "dynamicProperties_expeditionName";
    private final String preservationKey = "dynamicProperties_preservation";
    private final String preparationsKey = "preparations";
    private final String recordedByKey = "recordedBy";
    private final String lifeStageKey = "lifeStage";
    private final String licenseKey = "license";
    private final String individualCountKey = "individualCount";
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
    private final String fieldNumberKey = "fieldNumber";
    private final String habitatKey = "habitat";
    private final String typeStatusKey = "typeStatus";

    private final String kingdomKey = "kingdom";
    private final String phylumKey = "phylum";
    private final String classKey = "class";
    private final String orderKey = "order";
    private final String familyKey = "family";
    private final String genusKey = "genus";
    private final String subgenusKey = "subgenus";
    private final String speciesKey = "species";

    private final String scientificNameAuthorshipKey = "scientificNameAuthorship";
    private final String specificEpithetKey = "specificEpithet";
    private final String synonymsKey = "synonyms";
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
    private final String catchallKey = "catchall:";
    private final String scientificNameKey = "scientificName";

    private StringBuilder fuzzySeachTextSb;
    private StringBuilder freeTextSearchSb;
    private StringBuilder synonymsSb;

    private final String star = "*";

    public static SolrSearchHelper getInstance() {
        synchronized (SolrSearchHelper.class) {
            if (instance == null) {
                instance = new SolrSearchHelper();
            }
        }
        return instance;
    }
    
    public String buildAutoCompleteSearchText(String text, String field, 
            boolean isCaseSensitive) {
        
        if(isCaseSensitive) {
            return buildContainsSearchText(text, text);
        } else {
            return buildCaseInsensitiveContainsSearchText(text, field);
        } 
    }
     
    public String buildScientificName(String text, String key, String searchMode ) {

        searchMode = searchMode == null ? equals : searchMode;
        text = text.replaceAll("([+\\-!(){}\\[\\]^\"~*?:\\\\|&])", "\\\\$1");

        if (searchMode.equals(equals)) {
            return buildCaseInsensitivEqualsSearchText(text, key);
        } else {
            return buildCaseInsensitiveContainsSearchText(text, key);
        } 
    }
    
    public String buildFreesearchText(String text) {
        fuzzySeachTextSb = new StringBuilder();
        text = buildCaseInsensitiveContainsSearchText(text, catchallKey);
       
        
        return text;
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


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

    
    private String buildCaseInsensitiveContainsSearchText(String text, String key) {

        if (text.contains(emptySpace)) {
            
            searchText = text.split(emptySpace); 
            for (String value : searchText) {
                fuzzySeachTextSb.append(plusSign);
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
            fuzzySeachTextSb.append(leftBracket);
            fuzzySeachTextSb.append(star);
            fuzzySeachTextSb.append(text);
            fuzzySeachTextSb.append(star); 
            fuzzySeachTextSb.append(rightBracket);
        }
        return fuzzySeachTextSb.toString().trim();
    }


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String buildSynonyms(String synonyms) {
        synonymsSb = new StringBuilder();

        return buildStartsWithSearchText(synonyms, synonymsKey);
    }

    public String buildFreeTextSearch(String text) {

        freeTextSearchSb = new StringBuilder();

        if (text == null || text.isEmpty() || text.equals(star)) {
            freeTextSearchSb.append(catchallKey);
            freeTextSearchSb.append(star);
            return freeTextSearchSb.toString().trim();
        }

        if (text.contains(emptySpace)) {
            searchText = text.split(emptySpace);
            for (String value : searchText) {
                freeTextSearchSb.append(catchallKey);
                freeTextSearchSb.append(star);
                freeTextSearchSb.append(value);
                freeTextSearchSb.append(star);
                freeTextSearchSb.append(emptySpace);
            }
        } else {
            freeTextSearchSb.append(catchallKey);
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
        fields.add(basisOfRecordKey);
        fields.add(catalogNumberKey);
        fields.add(collectionNameKey);
        fields.add(expeditionNameKey);
        fields.add(preservationKey);
        fields.add(preparationsKey);
        fields.add(recordedByKey);
        fields.add(lifeStageKey);
        fields.add(licenseKey);
        fields.add(individualCountKey);
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
        fields.add(synonymsKey);
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

    private String buildContainsSearchText(String text, String key) {

        boolean isUpperCase = Character.isUpperCase(text.charAt(0));

        if (text.contains(emptySpace)) {
            searchText = text.split(emptySpace);

            if (isUpperCase) {
                for (String value : searchText) {
                    fuzzySeachTextSb.append(plusSign);
                    fuzzySeachTextSb.append(key);
                    fuzzySeachTextSb.append(colon);
                    fuzzySeachTextSb.append(star);
                    fuzzySeachTextSb.append(value);
                    fuzzySeachTextSb.append(star);
                    fuzzySeachTextSb.append(emptySpace);
                }
            } else {
                fuzzySeachTextSb.append(leftBracket);
                fuzzySeachTextSb.append(plusSign);
                fuzzySeachTextSb.append(key);
                fuzzySeachTextSb.append(colon);
                fuzzySeachTextSb.append(star);
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
                fuzzySeachTextSb.append(rightBracket);

                fuzzySeachTextSb.append(emptySpace);
                fuzzySeachTextSb.append(leftBracket);
                for (String value : searchText) {
                    fuzzySeachTextSb.append(plusSign);
                    fuzzySeachTextSb.append(key);
                    fuzzySeachTextSb.append(colon);
                    fuzzySeachTextSb.append(star);
                    fuzzySeachTextSb.append(value);
                    fuzzySeachTextSb.append(star);
                    fuzzySeachTextSb.append(emptySpace);
                }
                fuzzySeachTextSb.append(rightBracket);
            }
        } else {
            fuzzySeachTextSb.append(key);
            fuzzySeachTextSb.append(colon);
            fuzzySeachTextSb.append(leftBracket);
            fuzzySeachTextSb.append(star);
            fuzzySeachTextSb.append(text);
            fuzzySeachTextSb.append(star);
            fuzzySeachTextSb.append(emptySpace);

            fuzzySeachTextSb.append(star);
            fuzzySeachTextSb.append(StringUtils.capitalize(text));
            fuzzySeachTextSb.append(star);
            fuzzySeachTextSb.append(rightBracket);
        }
        return fuzzySeachTextSb.toString().trim();
    }

    private String buildEqualsSearchText(String text, String key) {
        fuzzySeachTextSb.append(key);
        fuzzySeachTextSb.append(colon);
        fuzzySeachTextSb.append(quotationMark);
        fuzzySeachTextSb.append(StringUtils.capitalize(text));
        fuzzySeachTextSb.append(quotationMark);
        return fuzzySeachTextSb.toString().trim();
    }

    private String buildCaseInsensitivEqualsSearchText(String text, String key) {
        fuzzySeachTextSb.append(key);
        fuzzySeachTextSb.append(colon);
        fuzzySeachTextSb.append(quotationMark);
        fuzzySeachTextSb.append(text);
        fuzzySeachTextSb.append(quotationMark);
        return fuzzySeachTextSb.toString().trim();
    }

    
    public String buildSearchText(String text, String key, String searchMode, boolean fuzzySearch) {

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

    public String buildSearchText(String text, String key, boolean fuzzySearch) {

        fuzzySeachTextSb = new StringBuilder();

        if (text == null || text.isEmpty() || text.equals(star)) {
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
}
