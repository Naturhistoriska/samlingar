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

    private final int batchSize = 6000;
    private final String birdCollection = "Bird";

    private final String eventDateKey = "eventDate";
    private final String eventEndDateKey = "eventEndDate";

    private String catalogNumber;
    private String collectionName;

    private JsonObject mappingJson;
    private JsonObject classificationJson;
    private JsonObject coordinatesJson;
    private JsonObject eventDateJson;
    private JsonObject eventEndDateJson;

    private String csvCatalogedDate;
    private String csvTypeStatusKey;
    private String csvCatalogNumberKey;
    private String csvDateIdentifiedKey;

    private String latitude;
    private String longitude;
 
    
    private List<JsonArray> list;
//    private List<String> classificationKeys;

    private JsonObjectBuilder attBuilder;
    private JsonArrayBuilder arrayBuilder;

    private boolean hasSynonyms;

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

        if (json.containsKey(eventDateKey)) {
            eventDateJson = JsonHelper.getInstance().getEventDateJson(json);
            log.info("eventDateJson : {}", eventDateJson);
        }

        if (json.containsKey(eventEndDateKey)) {
            eventEndDateJson = JsonHelper.getInstance().getEventEndDateJson(json);
        }

        coordinatesJson = JsonHelper.getInstance().getCoordinatesJson(json);

        csvCatalogNumberKey = JsonHelper.getInstance().getCatalogNumberCsvKey(json);

        csvCatalogedDate = JsonHelper.getInstance().getCatalogedDateCsvKey(json);
        csvTypeStatusKey = JsonHelper.getInstance().getTypeStatusCsvKey(json);

        csvDateIdentifiedKey = JsonHelper.getInstance().getDateIdentifiedCsvKey(json);

        hasSynonyms = JsonHelper.getInstance().hasSynonyms(json);

        AtomicInteger counter = new AtomicInteger(0); 

        records.stream()
                .filter(isValid)
                .forEach(record -> {
                    attBuilder = Json.createObjectBuilder();
                    try {
                        counter.getAndIncrement();
                        catalogNumber = record.get(csvCatalogNumberKey).trim();
//                        log.info("catalogNumber : {}  ", catalogNumber  );

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

                        if (classificationJson != null) {
                            JsonHelper.getInstance().addClassification(attBuilder, classificationJson, record);
                        }

                        latitude = record.get(JsonHelper.getInstance()
                                .getLatitudeCsvKey(coordinatesJson)).trim();
                        longitude = record.get(JsonHelper
                                .getInstance().getLongitudeCsvKey(coordinatesJson)).trim();
                        coordinatesBuilder.build(attBuilder, latitude, longitude);

                        log.info("coordinates added...");

                        JsonHelper.getInstance().addEventDate(attBuilder, eventDateJson, record);
                        log.info("eventDate added...");

                        if (eventEndDateJson != null) {
                            JsonHelper.getInstance().addEventEndDate(attBuilder, eventEndDateJson, record);
                        }

                        if (csvCatalogedDate != null) {
                            JsonHelper.getInstance().addCatalogDate(attBuilder, record.get(csvCatalogedDate));
                        }

                        if (csvDateIdentifiedKey != null) {
                            JsonHelper.getInstance().addDateIdentified(attBuilder, record.get(csvDateIdentifiedKey));
                        }
//        
                        if (csvTypeStatusKey != null) {
                            JsonHelper.getInstance().addTypeStatus(attBuilder,
                                    record.get(csvTypeStatusKey));
                        }
//                        log.info("typestatus added...");

                        if (hasSynonyms) {
                            JsonHelper.getInstance().addSynonyms(attBuilder, json, record);
                        }

                        JsonHelper.getInstance().addCountry(attBuilder, json, record);
                        log.info("add coutry ");

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
        log.info("count : {}", counter.get());
        return list;
    }

//    private final Predicate<CSVRecord> isValid1 = record
//            ->  StringUtils.isBlank(record.get(csvCatalogNumberKey));
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
