package se.nrm.samlingar.data.process.logic.json;

import java.time.LocalDate; 
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.data.process.logic.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class JsonHelper {

    private final String mappingKey = "mapping";

    private final String idKey = "id";
    private final String idPrefixKey = "idPrefix";
    private final String collectionNameKey = "collectionName";
    private final String collectionCodeKey = "collectionCode";
    private final String catalogNumberKey = "catalogNumber";
    private final String catalogedDateKey = "catalogedDate";
    private final String catalogedMonthKey = "catalogedMonth";
    private final String catalogedYearKey = "catalogedYear";
    private final String catalogIDKey = "catalogID";
    
    private final String modifiedDateKey = "modified";
    
    private final String associatedMediaKey = "associatedMedia";
    private final String synonymsKey = "synonyms";
    private final String synonymAuthorKey = "synonymAuthor";
    private final String classificationKay = "classification";
    private final String scientificNameKey = "scientificName";
    private final String hasImagesKey = "hasImage";
    private final String higherClassificationKey = "higherClassification";
    private final String taxonRankKey = "taxonRank";
    private final String eventDateKey = "eventDate";
    private final String eventEndDateKey = "eventEndDate";
    private final String startDayOfYearKey = "startDayOfYear";
    private final String endDayOfYearKey = "endDayOfYear";
    private final String typeStatusKey = "typeStatus";
    private final String dayKey = "day";
    private final String monthKey = "month";
    private final String yearKey = "year";
    private final String endYearKey = "endYear";
    private final String endMonthKey = "endMonth";
    private final String endDayKey = "endDay";
    
    private final String coordinatesKey = "coordinates"; 
    private final String latitudeKey = "decimalLatitude";
    private final String longitudeKey = "decimalLongitude";
    private final String verbatimEventDateKey = "verbatimEventDate";
//    private final String geohashKey = "geohash";
//    private final String geopointKey = "geopoint";
    
    private final String geoKey = "geo";
    private final String verbatimCoordinatesKey = "verbatimCoordinates";
    private final String latLongKey = "lat_long";
    
    private final String countryKey = "country";
    private final String exsiccatKey = "exsiccate";
    private final String dateIdentifiedKey = "dateIdentified";
     

    private final String fileNameKey = "fileName";

//    private final String doubleFormat1 = "%.1f";
//    private final String doubleFormat2 = "%.2f";
//    private final String doubleFormat3 = "%.3f";

    private final String zeroOne = "01";
    private final String zero = "0";
    private final String sweden = "Sweden";
    private final String sverige = "Sverige";
    private final String seKey = "se";
    private final String descriptionKey = "description";
    private final String speciesRank = "species";

    private final String currentDetermination = "CurrentDetermination";
    
    

    private String csvKey;

    private StringBuilder synonymAuthorSb;

    private List<String> classificationList;
    private String synonyms;
    private List<String> images;
    private LocalDate eventDate;
//    private String typeStatus;
    private String year;
    private String month;
    private String day;
    
    private String endYear;
    private String endMonth;
    private String endDay;
            
    
    
    
    private YearMonth yearMonth;
    private String country;

    private String formattedLat;
    private String formattedLong;
    private int intLat;
    private int intLong;
    private String format;

    private String taxon;

    private final char comma = ',';
    private final String slash = "/";
    private final String dash = "-";
    private final String underScore = "_";
    private final String emptySpace = " ";
    private final String pipe = "\\|";
    

    private final DateTimeFormatter yDtf = DateTimeFormatter.ofPattern("uuuu");

    private StringBuilder sb;
    private LocalDate catalogedDate;
    private StringBuilder coordinatesSb;
    
    private LocalDate modifiedDate;

    private LocalDate eventEndDate;
    
    private LocalDate dateIdentified;
    private String identifiedYear;

    private boolean isScientificNameSet;

    private JsonArrayBuilder imageArrayBuilder;
    private JsonArrayBuilder georHashArrayBuilder;
    private JsonArrayBuilder pointArrayBuilder;
    private StringBuilder geoHashSb;

    private StringBuilder pointSb;
    
    private JsonArrayBuilder synomysBuilder;

    private StringBuilder dateSb;
    private String[] dateArray;
    private List<String> synonymsList;
    
    
    private JsonArrayBuilder synonymsArrayBuilder;
    private JsonArrayBuilder synonymAuthorsArrayBuilder;

    private static JsonHelper instance = null;

    public static JsonHelper getInstance() {
        synchronized (JsonHelper.class) {
            if (instance == null) {
                instance = new JsonHelper();
            }
        }
        return instance;
    }

    public JsonObject getMappingJson(JsonObject json) {
        return json.getJsonObject(mappingKey);
    }

    public String getIdPrefix(JsonObject json) {
        return json.getString(idPrefixKey);
    }

    public char getDelimiter(JsonObject json, String key) {
        return getJsonValue(json, key) != null ? getJsonValue(json, key).charAt(0) : comma;
    }

    public String getFileName(JsonObject json) {
        return json.containsKey(fileNameKey) ? json.getString(fileNameKey) : null;
    }

    public String getJsonValue(JsonObject json, String key) {
        return json.containsKey(key) ? json.getString(key) : null;
    }

    public JsonObject getJsonObject(JsonObject json, String key) {
        return json.containsKey(key) ? json.getJsonObject(key) : null;
    }

    public void addIdForBird(JsonObjectBuilder attBuilder, long recordNumber, String idPrefix) {
        sb = new StringBuilder();
        sb.append(idPrefix);
        sb.append(underScore);
        sb.append(recordNumber);
        attBuilder.add(idKey, sb.toString().trim());
    }
     
    public String getCatalogNumberCsvKey(JsonObject json) {
        return json.getString(catalogNumberKey);
    }

    public String getAssociatedMediaCsvKey(JsonObject json) {
        return json.getString(associatedMediaKey);
    }
    
    public String getExsicaatCsvKey(JsonObject json) {
        return json.containsKey(exsiccatKey) ? json.getString(exsiccatKey) : null; 
    }

    public String getCatalogedDateCsvKey(JsonObject json) {
        return json.containsKey(catalogedDateKey) ? json.getString(catalogedDateKey) : null;
    }
    
    public String getModifiedDateCsvKey(JsonObject json) { 
        return json.containsKey(modifiedDateKey) ? json.getString(modifiedDateKey) : null;
    }

    public String getLatitudeCsvKey(JsonObject coordinatesJson) {
        return coordinatesJson.getString(latitudeKey);
    }

    public String getLongitudeCsvKey(JsonObject coordinatesJson) {
        return coordinatesJson.getString(longitudeKey);
    }

    public String getSynonymCsvKey(JsonObject json) {
        return json.getString(synonymsKey);
    }
    
    public boolean hasSynonyms(JsonObject json) {
        return json.containsKey(synonymsKey);
    }

    public String getSynonymAuthorCsvKey(JsonObject json) {
        return json.getString(synonymAuthorKey);
    }

    public String getDescriptionCsvKey(JsonObject json) {
        return json.getString(descriptionKey);
    }
    
    public String getCatalogIdCsvKey(JsonObject json) {
        return json.getString(catalogIDKey);
    }

    public boolean isStringCoordinates(JsonObject json) {
        return JsonValue.ValueType.STRING.equals(json.get(coordinatesKey).getValueType());
    }
    
    public boolean isStringEventDate(JsonObject json) { 
        return JsonValue.ValueType.STRING.equals(json.get(eventDateKey).getValueType());
    }
    
    public JsonObject getEventEndDateJson(JsonObject json) {
        return json.containsKey(eventEndDateKey) ? json.getJsonObject(eventEndDateKey) : null;
    }


    public JsonObject getSynonymJson(JsonObject json) {
        return json.getJsonObject(synonymsKey);
    }

    public JsonObject getClassificationJson(JsonObject json) {
        return json.containsKey(classificationKay)
                ? json.getJsonObject(classificationKay) : null;
    }

    public JsonObject getEventDateJson(JsonObject json) {
        return json.getJsonObject(eventDateKey);
    }

    public String getEventDateCsvKey(JsonObject json) {
        return json.containsKey(eventDateKey) ? json.getString(eventDateKey) : null;
    }
  
    public JsonObject getCoordinatesJson(JsonObject json) {
        return json.getJsonObject(coordinatesKey);
    }

    public String getCoordinatesCsvKey(JsonObject json) {
        return json.getString(coordinatesKey);
    }

    public String getTypeStatusCsvKey(JsonObject json) {
        return json.containsKey(typeStatusKey) ? json.getString(typeStatusKey) : null;
    }
    
    public String getDateIdentifiedCsvKey(JsonObject json) {
        return json.containsKey(dateIdentifiedKey) ? json.getString(dateIdentifiedKey) : null;
    }
 
    public void addClassification(JsonObjectBuilder attBuilder,
            JsonObject classificationJson, CSVRecord record) { 
//        log.info("addClassification " );
        
        isScientificNameSet = false;
//        classificationList = new ArrayList();
        classificationJson.keySet()
                .stream()
                .forEach(key -> {
                    taxon = record.get(classificationJson.getString(key)).trim(); 
                    if (!StringUtils.isBlank(taxon)) {           
                        attBuilder.add(key, taxon);
                        if (!isScientificNameSet) {
                            addAttValue(attBuilder, scientificNameKey, taxon);
                            addAttValue(attBuilder, taxonRankKey, StringUtils.capitalize(key).trim()); 
                            isScientificNameSet = true; 
                        } 
                    } 
                }); 
    }

    public void addZooClassification(JsonObjectBuilder attBuilder,
            JsonObject classificationJson, CSVRecord record) { 
//        log.info("addClassification " );
        
        isScientificNameSet = false;
//        classificationList = new ArrayList();
        classificationJson.keySet()
                .stream()
                .forEach(key -> {
                    taxon = record.get(classificationJson.getString(key)).trim(); 
                    if (!StringUtils.isBlank(taxon)) {           
                        attBuilder.add(key, taxon);
                        if (!isScientificNameSet) { 
                            addAttValue(attBuilder, taxonRankKey, StringUtils.capitalize(key).trim()); 
                            isScientificNameSet = true; 
                        } 
                    } 
                }); 
    }
        
    public void addClassificationForPaleoCollection(JsonObjectBuilder attBuilder,
            JsonObject classificationJson, CSVRecord record) {
        log.info("addClassificationForPaleoCollection : {}", classificationJson);
        isScientificNameSet = false;
        classificationList = new ArrayList();
        classificationJson.keySet()
                .stream()
                .forEach(key -> {
                    taxon = record.get(classificationJson.getString(key)).trim();
                    if (!StringUtils.isBlank(taxon)) {
                        
                        if (isScientificNameSet) {
                            classificationList.add(taxon);
                        } else {
                            addAttValue(attBuilder, scientificNameKey, taxon);
                            addAttValue(attBuilder, taxonRankKey, key);
                            isScientificNameSet = true;
                        }
                    }
                    addAttValue(attBuilder, key, taxon);
                });
        Collections.reverse(classificationList);
        addAttValue(attBuilder, higherClassificationKey,
                classificationList.stream().collect(Collectors.joining(slash)));
    }

//    public void addClassification1(JsonObjectBuilder attBuilder,
//            List<String> classificationKeys, CSVRecord record) {
//
//        isScientificNameSet = false;
//        classificationList = new ArrayList();
//        classificationKeys.stream()
//                .forEach(key -> {
//                    taxon = record.get(key).trim();
//                    if (!StringUtils.isBlank(taxon)) {
//                        if (isScientificNameSet) {
//                            classificationList.add(taxon);
//                            addAttValue(attBuilder, key, taxon);
//                        } else {
//                            addAttValue(attBuilder, scientificNameKey, taxon);
//                            if (key.equals(scientificNameKey) || key.equals(currentDetermination)) {
//                                addAttValue(attBuilder, taxonRankKey, speciesRank);
//                            } else {
//                                addAttValue(attBuilder, taxonRankKey, key);
//                            }
//                            isScientificNameSet = true;
//                        }
//                    }
//                });
//        Collections.reverse(classificationList);
//        addAttValue(attBuilder, higherClassificationKey,
//                classificationList.stream().collect(Collectors.joining(slash)));
//    }
    
    public void addVerbatimCoordinates(JsonObjectBuilder attBuilder, String verbatimCoordinates) {
         addAttValue(attBuilder, verbatimCoordinatesKey,verbatimCoordinates); 
    }

    public String getCollectionName(JsonObject json) {
        return json.getString(collectionNameKey);
    }
    
    public String getCollectionCode(JsonObject json) { 
        return json.getString(collectionCodeKey);
    }

    public void addId(JsonObjectBuilder attBuilder, String catalogNumber, String idPrefix) {
        sb = new StringBuilder();
        sb.append(idPrefix);
        sb.append(underScore);
        sb.append(catalogNumber);
        attBuilder.add(idKey, sb.toString().trim());
    }

    public void addCollectionName(JsonObjectBuilder attBuilder, String collectionName) {
        addAttValue(attBuilder, collectionNameKey, collectionName);
    }

    public void addCatalogNumber(JsonObjectBuilder attBuilder, String catalogNumber) {
        addAttValue(attBuilder, catalogNumberKey, catalogNumber);
    }

    public void addCatalogDate(JsonObjectBuilder attBuilder, String date) {
//        log.info("addCatalogDate : {}", date);
        catalogedDate = Util.getInstance().stringToLocalDate(date);
//        log.info("catalogedDate : {}", catalogedDate);
        if (catalogedDate != null) {
            attBuilder.add(catalogedDateKey, catalogedDate.toString());
            attBuilder.add(catalogedMonthKey, catalogedDate.getMonth().name());
            attBuilder.add(catalogedYearKey, catalogedDate.getYear());
        }
    }
    
    public void addModifedDate(JsonObjectBuilder attBuilder, String date) {
//        log.info("addModifedDate : {}", date);   
        modifiedDate = Util.getInstance().stringToLocalDate(date);
//        log.info("modifiedDate : {}", modifiedDate);
        if (modifiedDate != null) {
            attBuilder.add(modifiedDateKey, modifiedDate.toString()); 
        }
    }
        
    public void addDateIdentified(JsonObjectBuilder attBuilder, String date) {
//        log.info("addDateIdentified : {}", date);
        dateIdentified = Util.getInstance().stringToLocalDate(date);
        log.info("dateIdentified : {}", catalogedDate);
        if (catalogedDate != null) {
            attBuilder.add(dateIdentifiedKey, catalogedDate.toString()); 
        }  
    }

    public void addCollectionCode(JsonObjectBuilder attBuilder, String idPrefix) {
        addAttValue(attBuilder, collectionCodeKey, idPrefix);
    }

    public void addMappingValue(JsonObjectBuilder attBuilder,
            JsonObject mappingJson, CSVRecord record) {
//        log.info("addMappingValue : {} - {}", record, mappingJson);
        mappingJson.keySet().stream()
                .forEach(key -> {
                    csvKey = mappingJson.getString(key); 
                    addAttValue(attBuilder, key, record.get(csvKey));
                });
    }

    public void addImages(JsonObjectBuilder attBuilder, List<String> imageList) {
        if (imageList != null && !imageList.isEmpty()) {
            imageArrayBuilder = Json.createArrayBuilder();
            imageList.stream()
                    .forEach(s -> {
                        imageArrayBuilder.add(s);
                    });
            attBuilder.add(hasImagesKey, true);
            attBuilder.add(associatedMediaKey, imageArrayBuilder);
        }
    }

    public void addImages(JsonObjectBuilder attBuilder, String associatedMedia) {
        images = Arrays.asList(StringUtils.split(associatedMedia, slash));
        addImages(attBuilder, images);
    }

    // used for pal
    public void addSynonymAndAuthor(JsonArrayBuilder synomysArrayBuilder,
            JsonArrayBuilder synomyAuthorsArrayBuilder,
            String synonym, String synonymAuthor) {
        if (!StringUtils.isBlank(synonym)) {
            synonymAuthorSb = new StringBuilder();
            synonymAuthorSb.append(synonym);
            synonymAuthorSb.append(emptySpace);
            synonymAuthorSb.append(synonymAuthor);
            
            synomysArrayBuilder.add(synonym);
            synomyAuthorsArrayBuilder.add(synonymAuthorSb.toString().trim());
        }
    }
    
    // used by zoo
    public void addSynonyms(JsonObjectBuilder attBuilder,  String synonyms) {  
//        synonyms = record.get(json.getString(synonymsKey));
        log.info("addSynonyms : {}", synonyms);
        if(!StringUtils.isBlank(synonyms)) {
            synonymsList = new ArrayList();
            
            synomysBuilder = Json.createArrayBuilder();
            synonymsList = Arrays.asList(synonyms.split(pipe));
            synonymsList.stream()
                    .forEach(synonym -> {
                        synomysBuilder.add(synonym.trim());
                    });
            attBuilder.add(synonymsKey, synomysBuilder);
        }  
    } 
    
    // Used by bot
    public void addSynonyms(JsonObjectBuilder attBuilder, List<String> synonyms) {

        if (synonyms != null && !synonyms.isEmpty()) {
//            log.info("addSynonyms : {}", synonyms.size());
            synonymsArrayBuilder = Json.createArrayBuilder();
            synonymAuthorsArrayBuilder = Json.createArrayBuilder();
            synonyms.stream()
                    .forEach(synomy -> {
                        synonymsArrayBuilder.add(synomy.trim());
                        synonymAuthorsArrayBuilder.add(synomy.trim());
                    });
            attBuilder.add(synonymsKey, synonymsArrayBuilder);
            attBuilder.add(synonymAuthorKey, synonymAuthorsArrayBuilder);
        }
    }

    private String buildDate(String year, String month, String day) {
//        log.info("buildDate : {} -- {}", year, month + " -- " + day);
        dateSb = new StringBuilder();
        dateSb.append(year);
        dateSb.append(dash);
        if (month.length() == 1) {
            dateSb.append(zero);
        }
        dateSb.append(month);
        dateSb.append(dash);
        if (day.length() == 1) {
            dateSb.append(zero);
        }
        dateSb.append(day);
        return dateSb.toString().trim();
    }

    private String verbatimEventDate(String year, String month, String day) { 
        dateSb = new StringBuilder();
        dateSb.append(year);
        dateSb.append(dash); 
        dateSb.append(month); 
        if(!StringUtils.isBlank(day)) {
            dateSb.append(dash); 
            dateSb.append(day); 
        }
        return dateSb.toString().trim();
    }

        
    public void addEventDate(JsonObjectBuilder attBuilder, String date) {
        log.info("addEventDate : {}", date);

        if (!StringUtils.isBlank(date)) {
            eventDate = Util.getInstance().stringToLocalDate(date);
            if (eventDate != null) {
                attBuilder.add(verbatimEventDateKey, eventDate.toString());
            } else {
                if (date.contains(slash)) {
                    dateArray = date.split(slash);
                } else {
                    dateArray = date.split(dash);
                }

                if (dateArray[0].length() == 4) {
                    switch (dateArray.length) {
                        case 1:
                            eventDate = Util.getInstance().stringToLocalDate(buildDate(dateArray[0], zeroOne, zeroOne));
                            break;
                        case 2:
                            eventDate = Util.getInstance().stringToLocalDate(buildDate(dateArray[0], dateArray[1], zeroOne));
                            break;
                        case 3:
                            eventDate = Util.getInstance().stringToLocalDate(buildDate(dateArray[0], dateArray[1], dateArray[2]));
                            break;
                        default:
                            eventDate = null;
                            break;
                    }
                }
            }
            attBuilder.add(verbatimEventDateKey, date);
            if (eventDate != null) {
                attBuilder.add(eventDateKey, eventDate.toString());
                attBuilder.add(startDayOfYearKey, eventDate.getDayOfYear());
            } 
        }
    }


    private void addEventDate(JsonObjectBuilder attBuilder, String year, String month, String day) {
//        log.info("addEventDate : {} -- {}", year, month + " -- " + day);
        if (!StringUtils.isBlank(year) && year.length() == 4) {
            if (StringUtils.isAllBlank(day, month)) {
                attBuilder.add(verbatimEventDateKey, year);
                eventDate = Util.getInstance().stringToLocalDate(buildDate(year, zeroOne, zeroOne));
            } else if (StringUtils.isBlank(day)) {
                attBuilder.add(verbatimEventDateKey, verbatimEventDate(year, month, null));
                eventDate = Util.getInstance().stringToLocalDate(buildDate(year, month, zeroOne));
            } else {
                attBuilder.add(verbatimEventDateKey, verbatimEventDate(year, month, day));
                eventDate = Util.getInstance().stringToLocalDate(buildDate(year, month, day));
            }
//            log.info("eventdata : {}", eventDate);
            addAttValue(attBuilder, yearKey, year);
            addAttValue(attBuilder, monthKey, month);
            addAttValue(attBuilder, dayKey, day);
        }
        
        if (eventDate != null) { 
            attBuilder.add(eventDateKey, eventDate.toString());
            attBuilder.add(startDayOfYearKey, eventDate.getDayOfYear());
        } 
    }

    public void addEventDate(JsonObjectBuilder attBuilder, JsonObject eventDateJson, CSVRecord record) {
//        log.info("addEventDate ");
        
        year = record.get(eventDateJson.getString(yearKey));  
        month = record.get(eventDateJson.getString(monthKey));  
        day = record.get(eventDateJson.getString(dayKey)); 
           
//        log.info("addEventDate : {} -- {}", year, month + " -- " + day);
        if (!StringUtils.isBlank(year)) {
            addEventDate(attBuilder, year, month, day);
        }
    }
     
    public void addEventEndDate(JsonObjectBuilder attBuilder, JsonObject eventEndDateJson, CSVRecord record) {
        log.info("addEventEndDate 1111 : {} -- {}", eventEndDateJson, record);
        
        endYear = record.get(eventEndDateJson.getString(endYearKey));   
        endMonth = record.get(eventEndDateJson.getString(endMonthKey)); 
        endDay = record.get(eventEndDateJson.getString(endDayKey));   
        if (!StringUtils.isBlank(endYear)) {
            addEventEndDate(attBuilder, endYear, endMonth, endDay);
        }
    }
    
    
    private void addEventEndDate(JsonObjectBuilder attBuilder, String year, String month, String day) {

        if (!StringUtils.isBlank(year) && year.length() == 4) {
            attBuilder.add(endYearKey, year);
            addAttValue(attBuilder, endMonthKey, month);
            addAttValue(attBuilder, endDayKey, day);
            eventEndDate = Util.getInstance().fixDate(year, month, day);
            
            attBuilder.add(endDayOfYearKey, eventEndDate.getDayOfYear());
        } 
    }
 
    public void addCountry(JsonObjectBuilder attBuilder, JsonObject json, CSVRecord record) {
        country = record.get(json.getString(countryKey));
        if (!StringUtils.isBlank(country)) {
            addAttValue(attBuilder, countryKey, StringUtils.capitalize(country).trim());
            if (country.equalsIgnoreCase(sweden)) {
                addAttValue(attBuilder, seKey, sverige);
            }
        }
    }

    public void addTypeStatus(JsonObjectBuilder attBuilder, String typeStatus) {
//        log.info("addTypeStatus : {}", typeStatus);
        if (!StringUtils.isBlank(typeStatus)) {
            addAttValue(attBuilder, typeStatusKey, 
                    StringUtils.capitalize(typeStatus).trim());
        }
    }

    public void addLatAndLong(JsonObjectBuilder attBuilder, double latitude, double longitude) {
        log.info("addLatAndLong : {} -- {}", latitude, longitude);
         
        attBuilder.add(latitudeKey, latitude);
        attBuilder.add(longitudeKey, longitude);
    }

    public void addCoordinates(JsonObjectBuilder attBuilder, double latitude, double longitude) {
        log.info("addCoordinates : {} -- {}", latitude, longitude);
        coordinatesSb = new StringBuilder();
        coordinatesSb.append(latitude);
        coordinatesSb.append(comma);
        coordinatesSb.append(longitude);
        attBuilder.add(latLongKey, coordinatesSb.toString().trim());
        attBuilder.add(geoKey, coordinatesSb.toString().trim());
//        attBuilder.add(geopointKey, coordinatesSb.toString().trim());
    }

    
 

    public void addExsiccat(JsonObjectBuilder attBuilder, String exsiccat) {
        addAttValue(attBuilder, exsiccatKey, exsiccat);
    }
    
    
 

    public void addAttValue(JsonObjectBuilder attBuilder, String key, String value) {
        if (!StringUtils.isBlank(value)) {
            attBuilder.add(key, value);
        }
    }
    
    public void addAttValue(JsonObjectBuilder attBuilder, String key, int value) {
        if (value != 0) {
            attBuilder.add(key, value);
        }
    }
}
