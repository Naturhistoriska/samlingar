package se.nrm.samlingar.data.process.logic.zoo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder; 
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.data.process.logic.coordinates.CoordinatesBuilder;
import se.nrm.samlingar.data.process.logic.json.JsonHelper;

/**
 *
 * @author idali
 */
@Slf4j
public class ZooJsonConverter implements Serializable {

    private final int batchSize = 2000;
    private final String birdCollection = "Bird";

    private String catalogedDate;
    private String catalogNumber;
    private String collectionName;
    private String synonym;
    private String synonymAuthor;

    private JsonObject mappingJson;
    private JsonObject synonymJson;
    private JsonObject classificationJson;
    private JsonObject eventDateJson;
    private JsonObject coordinatesJson;

    private final String synonymKey = "synonyms";
    private final String synonymAuthorKey = "synonymAuthor";
 
    private String csvEventDateKey;
    private String csvCatalogedDate;
    private String csvCoordinatesKey;
    private String csvTypeStatusKey;
    private String csvCatalogNumberKey;

    private String latitude;
    private String longitude;

    private List<JsonArray> list;
    private List<String> classificationKeys;

    private JsonObjectBuilder attBuilder;
    private JsonArrayBuilder arrayBuilder;

    private JsonArrayBuilder synomysArrayBuilder;
    private JsonArrayBuilder synomyAuthorsArrayBuilder;

    private boolean isStringCoordinates;
    private boolean hasCatalogedDate;
    
        
    @Inject
    private CoordinatesBuilder coordinatesBuilder;

    public ZooJsonConverter() {

    }

    public List<JsonArray> convert(List<CSVRecord> records, JsonObject json,
            String collectionId, Map<String, List<String>> imageMap) {
        list = new ArrayList();

        arrayBuilder = Json.createArrayBuilder();
        attBuilder = Json.createObjectBuilder();

        collectionName = JsonHelper.getInstance().getCollectionName(json);
        mappingJson = JsonHelper.getInstance().getMappingJson(json);
        log.info("mappingJosn : {}", mappingJson);

        classificationJson = JsonHelper.getInstance().getClassificationJson(json);
        classificationKeys = new ArrayList();
        if (classificationJson != null) {
            classificationJson.keySet()
                    .stream().forEach(key -> {
                        classificationKeys.add(classificationJson.getString(key));
                    });
        }

        isStringCoordinates = JsonHelper.getInstance().isStringCoordinates(json);
        if (isStringCoordinates) {
            csvCoordinatesKey = JsonHelper.getInstance().getCoordinatesCsvKey(json);
        } else {
            coordinatesJson = JsonHelper.getInstance().getCoordinatesJson(json);
        } 
        csvCatalogNumberKey = JsonHelper.getInstance().getCatalogNumberCsvKey(json);
        
        csvCatalogedDate = JsonHelper.getInstance().getCatalogedDateCsvKey(json); 
        csvTypeStatusKey = JsonHelper.getInstance().getTypeStatusCsvKey(json);
        
        AtomicInteger counter = new AtomicInteger(0);

        records.stream()
                .filter(isValid)
                .forEach(record -> {
                    attBuilder = Json.createObjectBuilder();
                    try {
                        counter.getAndIncrement();
                        catalogNumber = record.get(csvCatalogNumberKey).trim();
                        log.info("catalogNumber : {}", catalogNumber);

                        if (isBirdCollection()) {
                            JsonHelper.getInstance().addIdForBird(attBuilder, record.getRecordNumber(), collectionId);
                        } else {
                            JsonHelper.getInstance().addId(attBuilder, catalogNumber, collectionId);
                        }
//                        log.info("addCatalognumber" );
 
                        JsonHelper.getInstance().addCollectionName(attBuilder, collectionName);
////                        log.info("add CollectionName : {}",  collectionName);
                        JsonHelper.getInstance().addCollectionCode(attBuilder, collectionId);
////                        log.info("add CollectionId : {}",  collectionId);
                        JsonHelper.getInstance().addCatalogNumber(attBuilder, catalogNumber);
//
                        JsonHelper.getInstance().addMappingValue(attBuilder, mappingJson, record);
//                        log.info("add mappingJson  " );
                        
                        if (imageMap != null && !imageMap.isEmpty()) { 
                            JsonHelper.getInstance().addImages(attBuilder,
                                    imageMap.get(catalogNumber));
                        } 
                        if (!classificationKeys.isEmpty()) {
                            JsonHelper.getInstance().addClassification(attBuilder, classificationKeys, record);
                        }
//                        log.info("add classification: {}", classificationKeys);
                        
//                        log.info("coordinates json: {}", coordinatesJson);
                        if (isStringCoordinates) {
//                            convert.convertZooCoordinates(attBuilder, record.get(csvCoordinatesKey));
                        } else {
                            latitude = record.get(JsonHelper.getInstance()
                                    .getLatitudeCsvKey(coordinatesJson)).trim();
                            longitude = record.get(JsonHelper
                                    .getInstance().getLongitudeCsvKey(coordinatesJson)).trim();
                            coordinatesBuilder.build(attBuilder, latitude, longitude);
                        }
//                        log.info("add coordinates: {}", coordinatesJson);

                        csvEventDateKey = JsonHelper.getInstance().getEventDateCsvKey(json);
                        if (csvEventDateKey != null) {
                            JsonHelper.getInstance().addEventDate(attBuilder, record.get(csvEventDateKey));
                        }
//                        log.info("add eventDate " );
                        
                        if (csvCatalogedDate != null) {
                            JsonHelper.getInstance().addCatalogDate(attBuilder, record.get(csvCatalogedDate));
                        }
//                        log.info("add cataloged date " );
//                        if (hasDeterminedDate) {
//                            JsonHelper.getInstance().addDeterminedDate(attBuilder,
//                                    record.get(csvDeterminedDateKey));
//                        }
// 
                        if(csvTypeStatusKey != null) { 
                            JsonHelper.getInstance().addTypeStatus(attBuilder, 
                                    record.get(csvTypeStatusKey)); 
                        } 
                        log.info("add typestatus " );
                        JsonHelper.getInstance().addCountry(attBuilder, json, record); 
//                        log.info("add coutry " );
                        arrayBuilder.add(attBuilder);

                        if (counter.get() % batchSize == 0) {
                            list.add(arrayBuilder.build());
                            arrayBuilder = Json.createArrayBuilder();
                        }
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                });
        list.add(arrayBuilder.build());

        return list;
    }

    
//    private final Predicate<CSVRecord> isValid = record
//            -> isBirdCollection()
//            || !StringUtils.isBlank(record.get(csvCatalogNumberKey));
    
    
    private final Predicate<CSVRecord> isValid = record
            -> isBirdCollection()
            || !StringUtils.isBlank(record.get(csvCatalogNumberKey));

    private boolean isBirdCollection() {
        return collectionName.equals(birdCollection);
    }

//            s != null && !s.trim().isEmpty();
//    private final Predicate<CSVRecord> isValid = record
//            -> isBirdCollection()
//            || !StringUtils.isBlank(record.get(csvCatalogNumberKey));
}
