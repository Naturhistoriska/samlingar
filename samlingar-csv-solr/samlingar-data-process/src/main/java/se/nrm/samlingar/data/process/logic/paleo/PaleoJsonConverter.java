package se.nrm.samlingar.data.process.logic.paleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
import se.nrm.samlingar.data.process.logic.util.CommonString;

/**
 *
 * @author idali
 */
@Slf4j
public class PaleoJsonConverter implements Serializable {

    private final int batchSize = 6000;

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

    private String latitude;
    private String longitude;

    private List<JsonArray> list;
//    private List<String> classificationKeys;

    private JsonObjectBuilder attBuilder;
    private JsonArrayBuilder arrayBuilder;

    private JsonArrayBuilder synomysArrayBuilder;
    private JsonArrayBuilder synomyAuthorsArrayBuilder;

    private boolean isAddSynomys;
    private boolean addImages;

    @Inject
    private CoordinatesBuilder coordinatesBuilder;

    public List<JsonArray> convertCsvToJson(List<CSVRecord> records,
            JsonObject collectionJson, String collectionId) {

        list = new ArrayList();
        arrayBuilder = Json.createArrayBuilder();
        attBuilder = Json.createObjectBuilder();

        collectionName = JsonHelper.getInstance().getCollectionName(collectionJson);
        mappingJson = JsonHelper.getInstance().getMappingJson(collectionJson);
        synonymJson = JsonHelper.getInstance().getSynonymJson(collectionJson);
        classificationJson = JsonHelper.getInstance().getClassificationJson(collectionJson);
 
        eventDateJson = JsonHelper.getInstance().getEventDateJson(collectionJson);
        coordinatesJson = JsonHelper.getInstance().getCoordinatesJson(collectionJson);

        isAddSynomys = false;
        addImages = collectionJson.getBoolean(CommonString.getInstance().getAddImagesKey());
  
        AtomicInteger counter = new AtomicInteger(0);
        records.stream().forEach(record -> {
            try {
                catalogNumber = record.get(JsonHelper.getInstance()
                        .getCatalogNumberCsvKey(collectionJson)).trim();
                catalogedDate = record.get(JsonHelper.getInstance()
                        .getCatalogedDateCsvKey(collectionJson)).trim();
                log.info("catalogNumber : {}", catalogNumber);
                if (hasCatalogNumber(catalogNumber)) {
                    if (isAddSynomys) {
                        attBuilder.add(synonymKey, synomysArrayBuilder);
                        attBuilder.add(synonymAuthorKey, synomyAuthorsArrayBuilder);
                    }
                    arrayBuilder.add(attBuilder);

                    if (counter.get() % batchSize == 0) {
                        list.add(arrayBuilder.build());
//                    log.info("list : {}", list);
                        arrayBuilder = Json.createArrayBuilder();
                    }

                    counter.getAndIncrement();
                    attBuilder = Json.createObjectBuilder();
                    synomysArrayBuilder = Json.createArrayBuilder();
                    synomyAuthorsArrayBuilder = Json.createArrayBuilder();
                    isAddSynomys = false;

                    JsonHelper.getInstance().addId(attBuilder, catalogNumber, collectionId);

                    JsonHelper.getInstance().addCollectionName(attBuilder, collectionName);

                    JsonHelper.getInstance().addCollectionCode(attBuilder, collectionId);

                    JsonHelper.getInstance().addCatalogNumber(attBuilder, catalogNumber);

                    JsonHelper.getInstance().addCatalogDate(attBuilder, catalogedDate);

                    JsonHelper.getInstance().addClassificationForPaleoCollection(attBuilder, classificationJson, record);

                    if(addImages) {
                        JsonHelper.getInstance().addImages(attBuilder,
                            record.get(JsonHelper.getInstance()
                                    .getAssociatedMediaCsvKey(collectionJson)));
                    } 

                    JsonHelper.getInstance().addEventDate(attBuilder, eventDateJson, record);
                    JsonHelper.getInstance().addTypeStatus(attBuilder, 
                            record.get(JsonHelper.getInstance().getTypeStatusCsvKey(collectionJson)));

                    JsonHelper.getInstance().addCountry(attBuilder, collectionJson, record);

                    JsonHelper.getInstance().addMappingValue(attBuilder, mappingJson, record);

                    latitude = record.get(JsonHelper.getInstance()
                            .getLatitudeCsvKey(coordinatesJson)).trim();
                    longitude = record.get(JsonHelper
                            .getInstance().getLongitudeCsvKey(coordinatesJson)).trim();

                    coordinatesBuilder.build(attBuilder, latitude, longitude);
                    addSynonyms(record, synonymJson);
                } else {
                    if (isAddSynomys) {
                        addSynonyms(record, synonymJson);
                    }
                }

            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        });
        if (isAddSynomys) {
            attBuilder.add(synonymKey, synomysArrayBuilder);
            attBuilder.add(synonymAuthorKey, synomyAuthorsArrayBuilder);
        }

        arrayBuilder.add(attBuilder);

        list.add(arrayBuilder.build());
        log.info("count : {}", counter.get());
        return list;
    }

    private void addSynonyms(CSVRecord record, JsonObject synonymJson) {

        synonym = record.get(JsonHelper.getInstance().getSynonymCsvKey(synonymJson));

        if (!StringUtils.isBlank(synonym)) {
//            log.info("addSynonyms : {}", synonym);
            synonymAuthor = record.get(JsonHelper.getInstance().getSynonymAuthorCsvKey(synonymJson));

            JsonHelper.getInstance().addSynonymAndAuthor(synomysArrayBuilder, synomyAuthorsArrayBuilder, synonym, synonymAuthor);
            isAddSynomys = true;
        }

    }

    private boolean hasCatalogNumber(String catalogNumber) {
        return !StringUtils.isAllBlank(catalogNumber);
    }
}
