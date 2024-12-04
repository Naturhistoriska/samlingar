package se.nrm.samlingar.data.process.logic.json;

import java.time.LocalDate;
import java.time.Year;
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

    private final String associatedMediaKey = "associatedMedia";
    private final String synonymKey = "synonym";
    private final String synonymAuthorKey = "synonymAuthor";
    private final String classificationKay = "classification";
    private final String scientificNameKey = "scientificName";
    private final String higherClassificationKey = "higherClassification";
    private final String taxonRankKey = "taxonRank";
    private final String eventDateKey = "eventDate";
    private final String startDayOfYearKey = "startDayOfYear";
    private final String typeStatusKey = "typeStatus";
    private final String dayKey = "day";
    private final String monthKey = "month";
    private final String yearKey = "year";
    private final String coordinatesKey = "coordinates";
    private final String latitudeKey = "decimalLatitude";
    private final String longitudeKey = "decimalLongitude";
    private final String verbatimEventDateKey = "verbatimEventDate";
    private final String geohashKey = "geohash";
    private final String geopointKey = "geopoint";
    private final String verbatimCoordinatesKey = "verbatimCoordinates";
    private final String pointKey = "point";
    private final String countryKey = "country";

    private final String doubleFormat1 = "%.1f";
    private final String doubleFormat2 = "%.2f";
    private final String doubleFormat3 = "%.3f";

    private final String sweden = "Sweden";
    private final String sverige = "Sverige";
    private final String seKey = "se";

    private String csvKey;

    private StringBuilder synonymAuthorSb;

    private List<String> classificationList;
    private List<String> images;
    private LocalDate eventDate;
    private String typeStatus;
    private String year;
    private String month;
    private String day;
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
    private final String underScore = "_";
    private final String emptySpace = " ";

    private final DateTimeFormatter yDtf = DateTimeFormatter.ofPattern("uuuu");

    private StringBuilder sb;
    private LocalDate catalogedDate;
    private StringBuilder coordinatesSb;

    private boolean isScientificNameSet;

    private JsonArrayBuilder imageArrayBuilder;
    private JsonArrayBuilder georHashArrayBuilder;
    private JsonArrayBuilder pointArrayBuilder;
    private StringBuilder geoHashSb;
    private StringBuilder geopointSb;

    private StringBuilder pointSb;

    private static JsonHelper instance = null;

    public static JsonHelper getInstance() {
        synchronized (JsonHelper.class) {
            if (instance == null) {
                instance = new JsonHelper();
            }
        }
        return instance;
    }

    public JsonObject getMappingJson(JsonObject collectionJson) {
        return collectionJson.getJsonObject(mappingKey);
    }

    public String getIdPrefix(JsonObject json) {
        return json.getString(idPrefixKey);
    }

    public char getDelimiter(JsonObject json, String key) {
        return getJsonValue(json, key) != null ? getJsonValue(json, key).charAt(0) : comma;
    }

    public String getJsonValue(JsonObject json, String key) {
        return json.containsKey(key) ? json.getString(key) : null;
    }

    public String getCatalogNumberCsvKey(JsonObject json) {
        return json.getString(catalogNumberKey);
    }

    public String getAssociatedMediaCsvKey(JsonObject json) {
        return json.getString(associatedMediaKey);
    }

    public String getCatalogedDateCsvKey(JsonObject json) {
        return json.getString(catalogedDateKey);
    }

    public String getLatitudeCsvKey(JsonObject coordinatesJson) {
        return coordinatesJson.getString(latitudeKey);
    }

    public String getLongitudeCsvKey(JsonObject coordinatesJson) {
        return coordinatesJson.getString(longitudeKey);
    }

    public String getSynonymCsvKey(JsonObject json) {
        return json.getString(synonymKey);
    }

    public String getSynonymAuthorCsvKey(JsonObject json) {
        return json.getString(synonymAuthorKey);
    }

    public JsonObject getSynonymJson(JsonObject json) {
        return json.getJsonObject(synonymKey);
    }

    public JsonObject getClassificationJson(JsonObject json) {
        return json.getJsonObject(classificationKay);
    }

    public JsonObject getEventDateJson(JsonObject json) {
        return json.getJsonObject(eventDateKey);
    }

    public JsonObject getCoordinatesJson(JsonObject json) {
        return json.getJsonObject(coordinatesKey);
    }

    public void addClassification(JsonObjectBuilder attBuilder,
            List<String> classificationKeys, CSVRecord record) {

        isScientificNameSet = false;
        classificationList = new ArrayList();
        classificationKeys.stream()
                .forEach(key -> { 
                    taxon = record.get(key).trim();
                    
                    addAttValue(attBuilder, key, taxon);
                    if (!StringUtils.isBlank(taxon)) {
                        if (isScientificNameSet) {
                            classificationList.add(taxon);
                        } else {
                            addAttValue(attBuilder, scientificNameKey, taxon);
                            addAttValue(attBuilder, taxonRankKey, key);
                            isScientificNameSet = true;
                        }
                    } 
                });
        Collections.reverse(classificationList);
        addAttValue(attBuilder, higherClassificationKey,
                classificationList.stream().collect(Collectors.joining(slash)));
    }

    public String getCollectionName(JsonObject json) {
        return json.getString(collectionNameKey);
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
        catalogedDate = Util.getInstance().stringToLocalDate(date);
        if (catalogedDate != null) {
            attBuilder.add(catalogedDateKey, catalogedDate.toString());
            attBuilder.add(catalogedMonthKey, catalogedDate.getMonth().name());
            attBuilder.add(catalogedYearKey, catalogedDate.getYear());
        }
    }

    public void addCollectionCode(JsonObjectBuilder attBuilder, String idPrefix) {
        addAttValue(attBuilder, collectionCodeKey, idPrefix);
    }

    public void addMappingValue(JsonObjectBuilder attBuilder,
            JsonObject mappingJson, CSVRecord record)  {

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
            attBuilder.add(associatedMediaKey, imageArrayBuilder);
        }
    }

    public void addImages(JsonObjectBuilder attBuilder, String associatedMedia) {
        images = Arrays.asList(StringUtils.split(associatedMedia, slash));
        addImages(attBuilder, images);
    }

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

    public void addEventDate(JsonObjectBuilder attBuilder, JsonObject eventDateJson, CSVRecord record) {

        year = record.get(eventDateJson.getString(yearKey));
        month = record.get(eventDateJson.getString(monthKey));
        day = record.get(eventDateJson.getString(dayKey));

        if (!StringUtils.isBlank(year)) {

            eventDate = Util.getInstance().fixDate(year, month, day);
            log.info("event date: {}", eventDate);

            if (eventDate != null) {
                attBuilder.add(eventDateKey, eventDate.toString());
                attBuilder.add(startDayOfYearKey, eventDate.getDayOfYear());

                if (StringUtils.isBlank(month)) {
                    attBuilder.add(verbatimEventDateKey, year);
                } else {
                    if (StringUtils.isBlank(day)) {
                        yearMonth = Year.parse(year, yDtf)
                                .atMonth(Util.getInstance().stringToInt(month));
                        attBuilder.add(verbatimEventDateKey, yearMonth.toString());
                    } else {
                        attBuilder.add(verbatimEventDateKey, eventDate.toString());
                    }
                }
            }
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

    public void addTypeStatus(JsonObjectBuilder attBuilder, JsonObject json, CSVRecord record) {
        typeStatus = record.get(json.getString(typeStatusKey));
        addAttValue(attBuilder, typeStatusKey, StringUtils.capitalize(typeStatus).trim());
    }

    public void addLatAndLong(JsonObjectBuilder attBuilder, double latitude, double longitude) {  
        attBuilder.add(latitudeKey, latitude);
        attBuilder.add(longitudeKey, longitude);
    }

    public void addCoordinates(JsonObjectBuilder attBuilder, double latitude, double longitude) {

        coordinatesSb = new StringBuilder();
        coordinatesSb.append(latitude);
        coordinatesSb.append(comma);
        coordinatesSb.append(longitude);
        attBuilder.add(verbatimCoordinatesKey, coordinatesSb.toString().trim());
        attBuilder.add(geopointKey, coordinatesSb.toString().trim());
    }

    public void addGeoHash(JsonObjectBuilder attBuilder, String geoHash) {
        georHashArrayBuilder = Json.createArrayBuilder();
        for (int i = 2; i <= 5; i++) {
            geoHashSb = new StringBuilder();
            geoHashSb.append(i);
            geoHashSb.append(underScore);
            geoHashSb.append(geoHash.substring(0, i));
            georHashArrayBuilder.add(geoHashSb.toString().trim());
        }
        attBuilder.add(geohashKey, georHashArrayBuilder);
    }

    public void addPoint(JsonObjectBuilder attBuilder, double latitude, double longtude) {
        log.info("addPoint : {} -- {}", latitude, longtude);

        pointArrayBuilder = Json.createArrayBuilder();

        intLat = (int) latitude;
        intLong = (int) longtude;

        pointSb = new StringBuilder();
        pointSb.append(0);
        pointSb.append(underScore);
        pointSb.append(intLat);
        pointSb.append(comma);
        pointSb.append(intLong);
        pointArrayBuilder.add(pointSb.toString().trim());

        for (int i = 1; i < 4; i++) {
            format = getDoubleFormat(i);
            formattedLat = String.format(format, latitude);
            formattedLong = String.format(format, latitude);

            pointSb = new StringBuilder();
            pointSb.append(i);
            pointSb.append(underScore);
            pointSb.append(formattedLat);
            pointSb.append(comma);
            pointSb.append(formattedLong);
            pointArrayBuilder.add(pointSb.toString().trim());
        }
        attBuilder.add(pointKey, pointArrayBuilder);
//        log.info("what... : {}", attBuilder.build().toString());
    }

    private String getDoubleFormat(int i) {
        switch (i) {
            case 1:
                return doubleFormat1;
            case 2:
                return doubleFormat2;
            default:
                return doubleFormat3;
        }
    }
 
    private void addAttValue(JsonObjectBuilder attBuilder, String key, String value) {
        if (!StringUtils.isBlank(value)) {
            attBuilder.add(key, value);
        }
    }
}
