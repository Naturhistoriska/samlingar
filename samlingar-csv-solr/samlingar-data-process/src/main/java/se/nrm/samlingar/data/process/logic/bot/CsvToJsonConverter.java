package se.nrm.samlingar.data.process.logic.bot;

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
import se.nrm.samlingar.data.process.logic.coordinates.JsonCoordinatesConverter;
import se.nrm.samlingar.data.process.logic.json.JsonHelper;
import se.nrm.samlingar.data.process.vo.Determination;
 

/**
 *
 * @author idali
 */
@Slf4j
public class CsvToJsonConverter implements Serializable  {
    private final String collectionIdKey = "collectionId";
    private final String catalogNumberKey = "catalogNumber";
    private final String classificationKey = "classification";
    private final String startDateKey = "startDate";
    private final String coordinateKey = "coordinate";
    private final String synonymKey = "synonym";
    private final String catalogIdKey = "catalogId";
    private final String catalogedDateKey = "catalogedDate";
    private final String typeStatusKey = "typeStatus";
    private final String latitudeKey = "latitude";
    private final String longtitudeKey = "longitude";
    private final String degreeKey = "degree";
    private final String minuteKey = "minute";
    private final String secondKey = "second";
    private final String directionKey = "direction";
    private final String startDateYearKey = "collYear";
    private final String startDateMonthKey = "collMonth";
    private final String startDateDayKey = "collDay";
    
    private final String imageKey = "image";
    private final String associatedMediaKey = "associatedMedia";

    private String csvCatalogIdKey;
    private String csvCatalogedDateKey;
    private String csvTypeStatusKey;

    private String csvLatDegreeKey;
    private String csvLatMinuteKey;
    private String csvLatSecondKey;
    private String csvLatDirectionKey;

    private String csvLngDegreeKey;
    private String csvLngMinuteKey;
    private String csvLngSecondKey;
    private String csvLngDirectionKey;
 

    private final int batchSize = 5000;

    private String catalogNumber;
    private String catatlogId;

    private JsonObject classificationJson;
    private JsonObject startDateJson;
    private JsonObject coordinatesJson;
    private JsonObject latitudeJson;
    private JsonObject longitudeJson;

    private JsonObjectBuilder attBuilder;
    private JsonArrayBuilder arrayBuilder;
    private JsonArrayBuilder imageArrayBuilder;
    
    private List<JsonArray> list;
    private List<String> imageList;
    
    private JsonObject eventDateJson;
  
    @Inject
    private JsonCoordinatesConverter convert;
    
    @Inject
    private BotConverter botConvert;

    public CsvToJsonConverter() {

    }
    
    public List<JsonArray> vascularPlantsConvert(List<CSVRecord> records,
            Map<String, List<String>> synonysMap, Map<String, List<Determination>> determinationMap,
            Map<String, List<String>> imageMap, String idPrefix, String collectionName,
            JsonObject mappingJson ) {
        log.info("vascularPlantsConvert");

        list = new ArrayList();
        arrayBuilder = Json.createArrayBuilder();
        attBuilder = Json.createObjectBuilder();

        csvCatalogIdKey = mappingJson.getJsonObject(synonymKey).getString(catalogIdKey);

 
        classificationJson = mappingJson.getJsonObject(classificationKey);
        coordinatesJson = mappingJson.getJsonObject(coordinateKey);

        latitudeJson = coordinatesJson.getJsonObject(latitudeKey);
        longitudeJson = coordinatesJson.getJsonObject(longtitudeKey);

        csvLatDegreeKey = latitudeJson.getString(degreeKey);
        csvLatMinuteKey = latitudeJson.getString(minuteKey);
        csvLatSecondKey = latitudeJson.getString(secondKey);
        csvLatDirectionKey = latitudeJson.getString(directionKey);

        csvLngDegreeKey = longitudeJson.getString(degreeKey);
        csvLngMinuteKey = longitudeJson.getString(minuteKey);
        csvLngSecondKey = longitudeJson.getString(secondKey);
        csvLngDirectionKey = longitudeJson.getString(directionKey);

        csvCatalogedDateKey = mappingJson.getString(catalogedDateKey);
        csvTypeStatusKey = mappingJson.getString(typeStatusKey);
        
        eventDateJson = JsonHelper.getInstance().getEventDateJson(mappingJson); 

        AtomicInteger counter = new AtomicInteger(0);
        records.stream()
                .filter(isValid)
                .forEach(record -> {
                    attBuilder = Json.createObjectBuilder();
                    try {
                        counter.getAndIncrement();
                        catalogNumber = record.get(0);
                        log.info("catalogueId : {}", catalogNumber);

//                        if (!StringUtils.isBlank(catalogNumber)) {
                        JsonHelper.getInstance().addId(attBuilder, catalogNumber, idPrefix);
                        JsonHelper.getInstance().addCollectionName(attBuilder, collectionName);
                        JsonHelper.getInstance().addCollectionCode(attBuilder, idPrefix);
                        JsonHelper.getInstance().addCatalogNumber(attBuilder, catalogNumber);

                        JsonHelper.getInstance().addMappingValue(attBuilder, mappingJson, record);

                        JsonHelper.getInstance().addClassification(attBuilder, classificationJson, record);

//                            JsonHelper.getInstance().addStartDate(attBuilder, startDateJson, record);
                        JsonHelper.getInstance().addEventDate(attBuilder, eventDateJson, record);

                        JsonHelper.getInstance().addTypeStatus(attBuilder, 
                                StringUtils.capitalize(record.get(csvTypeStatusKey).trim())
                                );

                        JsonHelper.getInstance().addCatalogDate(attBuilder, record.get(csvCatalogedDateKey));
 
                        imageList = imageMap.get(catalogNumber);
                        if(imageList != null && !imageList.isEmpty()) {
                            imageArrayBuilder = Json.createArrayBuilder();
                            imageList.stream()
                                    .forEach(s -> {
                                        imageArrayBuilder.add(s);
                                    }); 
                            attBuilder.add(imageKey, true);
                            attBuilder.add(associatedMediaKey, imageArrayBuilder);   
                        }
                        
                        
                        convert.convertVascularPlantsCoordination(attBuilder,
                                csvLatDegreeKey, csvLatMinuteKey, csvLatSecondKey,
                                csvLatDirectionKey, csvLngDegreeKey, csvLngMinuteKey,
                                csvLngSecondKey, csvLngDirectionKey, record);
 
                        botConvert.convertDetermination(attBuilder, determinationMap, catalogNumber);

                        catatlogId = record.get(csvCatalogIdKey);
                        JsonHelper.getInstance().addSynonyms(attBuilder, synonysMap.get(catatlogId));
                        arrayBuilder.add(attBuilder);
                        if (counter.get() % batchSize == 0) {
                            list.add(arrayBuilder.build());
                            arrayBuilder = Json.createArrayBuilder();
                        }
//                        }
                    } catch (Exception ex) {
                        log.error("Exception : {}", ex.getMessage());
                    }
                });

        list.add(arrayBuilder.build());
        arrayBuilder = null;
        return list;
    }
    
    
    
    private final Predicate<CSVRecord> isValid = record -> !StringUtils.isBlank(record.get(0));

}
