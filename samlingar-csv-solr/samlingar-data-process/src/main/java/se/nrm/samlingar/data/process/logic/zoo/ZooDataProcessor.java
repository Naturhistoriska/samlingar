package se.nrm.samlingar.data.process.logic.zoo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject; 
import lombok.extern.slf4j.Slf4j; 
import org.apache.commons.csv.CSVRecord;
import se.nrm.samlingar.data.process.logic.files.CSVFileProcessor; 
import se.nrm.samlingar.data.process.logic.json.JsonHelper;
import se.nrm.samlingar.data.process.logic.solr.Solr;
import se.nrm.samlingar.data.process.logic.util.CommonString;
import se.nrm.samlingar.data.process.logic.zoo.images.ImageDataProcessor;

/**
 *
 * @author idali
 */
@Slf4j
public class ZooDataProcessor implements Serializable {
    
    private final String datasetKey = "dataset";
    
    private String fileName;
    private char delimiter;
    private String encoding;
    private String collectionId;

    private List<CSVRecord> records;
    private List<CSVRecord> imageRecords;
    private List<JsonArray> list;
    private Map<String, List<String>> imageMap;
    
    private int status;  
    private JsonObject imageJson; 
    private String imageFileName;
    
    private final String av = "av";
    
    
    @Inject
    private CSVFileProcessor fileProcessor;
    @Inject
    private ZooJsonConverter jsonConverter;
    @Inject
    private Solr solr;
    @Inject
    private ImageDataProcessor imageProcessor;
    
    public ZooDataProcessor() {
        
    }
    
    public void process(String filePath, JsonArray array, JsonArray imageArray, 
            String imageFilePath, boolean delete) {
        try {
            if (array != null) {
                array.getValuesAs(JsonObject.class).stream()
                        .filter(isProcess)
                        .forEach(json -> {
                            fileName = JsonHelper.getInstance().getJsonValue(
                                    json, CommonString.getInstance().getFileNameKey());
                            
                            delimiter = JsonHelper.getInstance().getDelimiter(
                                    json, CommonString.getInstance().getSeparatorKey());
                            encoding = JsonHelper.getInstance().getJsonValue(
                                    json, CommonString.getInstance().getEncodingKey());
                            log.info("encoding... {} -- {}", encoding, fileName);
                             
                            records = fileProcessor.read(filePath, fileName, delimiter, encoding);
                             
                            collectionId = JsonHelper.getInstance().getIdPrefix(json);
             
                            if (records != null) {
                                log.info("records size : {}", records.size());
                                if (delete) {
                                    solr.deleteCollection(collectionId);
                                }
                                 
                                imageMap = null;                 
                                if(json.getBoolean(CommonString.getInstance().getAddImagesKey())) { 
                                    imageRecords = getImageCsvRecord(imageArray, imageFilePath);
                                    imageMap = imageProcessor
                                            .run(JsonHelper.getInstance().getMappingJson(imageJson),
                                                    imageRecords); 
                                    log.info("imageMap: {}", imageMap.size());
                                }   
                                
                                list = jsonConverter.convert(records,  json, collectionId, imageMap);
  
                                list.stream()
                                        .forEach(l -> {
                                            status = solr.postToSolr(l.toString().trim());
                                            log.info("status : {}", status);
                                        });
                            }
                             
                        });

            }
        } catch (RuntimeException e) {
            log.error("error... {}", e.getMessage());
        } 
    }  
//    
//    private boolean isBirdCollection(String id) {
//        return id.equals(av);
//    }
//    
    private JsonObject getImageMappingJson(JsonArray imageArray) {
        log.info("getImageMappingJson" ); 

        return imageArray.getValuesAs(JsonObject.class)
                .stream()
                .filter(a -> a.getString(datasetKey).equals(collectionId))
                .findFirst()
                .get();
    }
    
    private List<CSVRecord> getImageCsvRecord(JsonArray imageArray, String imageFilePath) {
  
        imageJson = getImageMappingJson(imageArray);
        delimiter = JsonHelper.getInstance().getDelimiter(imageJson, 
                CommonString.getInstance().getSeparatorKey());
         
        imageFileName = JsonHelper.getInstance().getFileName(imageJson); 
         
        return fileProcessor.read(imageFilePath, imageFileName, delimiter, null);
    }

    
    private final Predicate<JsonObject> isProcess = json -> json.getBoolean(
            CommonString.getInstance().getProcessKey(), true); 
}
