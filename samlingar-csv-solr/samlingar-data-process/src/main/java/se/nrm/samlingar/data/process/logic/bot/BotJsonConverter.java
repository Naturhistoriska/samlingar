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
import se.nrm.samlingar.data.process.logic.coordinates.CoordinatesBuilder;
import se.nrm.samlingar.data.process.logic.json.JsonHelper;
import se.nrm.samlingar.data.process.vo.Determination;
import se.nrm.samlingar.data.process.vo.NameSynonyms;

/**
 *
 * @author idali
 */
@Slf4j
public class BotJsonConverter implements Serializable {
    
    private List<JsonArray> list;
    
    private JsonObjectBuilder attBuilder;
    private JsonArrayBuilder arrayBuilder;
    private JsonArrayBuilder imageArrayBuilder;
    
    private final int batchSize = 6000;
    
    private JsonObject eventDateJson;
    private JsonObject mappingJson;
    
    private String catalogNumber;
    private String exsiccat;
    
    private String csvExsiccatKey;
    private String csvCatalogIdKey;
    
    private NameSynonyms nameSynonyms;
    
    @Inject
    private CoordinatesBuilder coordinatesBuilder;
    @Inject
    private BotConverter convert;
    
    public BotJsonConverter() {
        
    }
    
    public List<JsonArray> convert(List<CSVRecord> records, Map<String, NameSynonyms> nameMap,
            Map<String, String> exisccateMap, Map<String, List<Determination>> determinationMap,
            Map<String, List<String>> imageMap, String collectionName,
            String idPrefix, JsonObject json) {
        log.info("convert");

        list = new ArrayList();
        arrayBuilder = Json.createArrayBuilder();
        attBuilder = Json.createObjectBuilder();

        eventDateJson = JsonHelper.getInstance().getEventDateJson(json); 
        mappingJson = JsonHelper.getInstance().getMappingJson(json);
        
        csvExsiccatKey = JsonHelper.getInstance().getExsicaatCsvKey(json);
        csvCatalogIdKey = JsonHelper.getInstance().getCatalogIdCsvKey(json);
 
        AtomicInteger counter = new AtomicInteger(0);
        records.stream()
                .filter(isValid)
                .forEach(record -> {
                    attBuilder = Json.createObjectBuilder();
                    try {
                        counter.getAndIncrement();
                        catalogNumber = record.get(0);
                        log.info("catalogueId : {}", catalogNumber); 
                        JsonHelper.getInstance().addId(attBuilder, catalogNumber, idPrefix);
                        JsonHelper.getInstance().addCollectionName(attBuilder, collectionName);
                        JsonHelper.getInstance().addCollectionCode(attBuilder, idPrefix);
                        JsonHelper.getInstance().addCatalogNumber(attBuilder, catalogNumber);
                         
                        JsonHelper.getInstance().addMappingValue(attBuilder, mappingJson, record);
                        
                        JsonHelper.getInstance().addEventDate(attBuilder, eventDateJson, record);
                          
                        JsonHelper.getInstance().addTypeStatus(attBuilder, 
                            record.get(JsonHelper.getInstance().getTypeStatusCsvKey(json)));
                         
                        JsonHelper.getInstance().addCatalogDate(attBuilder, 
                                record.get(JsonHelper.getInstance().getCatalogedDateCsvKey(json)));
//                        log.info("catalogDate added...");
                         
                        coordinatesBuilder.buildBotCoordinates(attBuilder, 
                                record.get(JsonHelper.getInstance().getCoordinatesCsvKey(json)).trim());
      
                        
                        convert.addImage(attBuilder, imageMap, catalogNumber);   
                        
                        convert.convertDetermination(attBuilder, determinationMap, catalogNumber);  
                         
                        exsiccat = exisccateMap.get(record.get(csvExsiccatKey));
                        JsonHelper.getInstance().addExsiccat(attBuilder, exsiccat); 
                                
                        nameSynonyms = nameMap.get(record.get(csvCatalogIdKey));
                        convert.addSynonyms(attBuilder, nameSynonyms);  
                        
                        arrayBuilder.add(attBuilder);
                        if (counter.get() % batchSize == 0) {
                            list.add(arrayBuilder.build());
                            arrayBuilder = Json.createArrayBuilder();
                        } 
                    } catch (Exception ex) {
                        log.error("Exception : {}", ex.getMessage());
                    }
                });
        list.add(arrayBuilder.build());
        
        log.info("counter : {}", counter.get());
        arrayBuilder = null;
        
        return list;
    }
    
    private final Predicate<CSVRecord> isValid = record -> !StringUtils.isBlank(record.get(0));
}
