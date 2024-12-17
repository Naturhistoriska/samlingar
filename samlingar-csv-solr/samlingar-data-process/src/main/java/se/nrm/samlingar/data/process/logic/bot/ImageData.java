package se.nrm.samlingar.data.process.logic.bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.data.process.logic.files.JsonFileHandler;

/**
 *
 * @author idali
 */
@Slf4j
public class ImageData implements Serializable {
    private final String catalogNumberKey = "catalogNumber";
    private final String photographerKey = "photographer";
    private final String associatedMediaKey = "associatedMedia";
    private final String datasetKey = "dataset";
    private final String separatorKey = "separator";
    private final String smallKey = "small";
    private final String largeKey = "large";
    private final String miniKey = "mini";
    private final String mediumKey = "medium";
    
    
    
    private final String fboDataset = "fbo";
    private final String kboDataset = "kbo";
     
    private final String leftBracket = " [";
    private final String rightBracket = "]";
    
    private char delimiter;
    
    private String csvCatalogNumberKey;
    
    private String catalogNumber;
    private String photographer;
    private String associatedMedia;
    
    private String smallImage;
    private String miniImage;
    private String largeImage;
    private String meduimImage;
    
    private String imageFilePath;
     
    private List<String> imageList;
    private Map<String, List<String>> map;
    
    private List<CSVRecord> records;
    
    private StringBuilder imageSb;
    private JsonArray imageArray;  
    
    private JsonObject associatedMediaJson;
    
    @Inject
    private JsonFileHandler fileHander;
    
    public ImageData() {
        
    }

//    public List<CSVRecord> getImageCsvRecord(InitialProperties propeties,JsonObject imageJson, 
//            CSVFileProcessor fileProcessor, String dataset) {
//          
//        delimiter = JsonHelper.getInstance().getDelimiter(imageJson, separatorKey); 
//        imageFilePath = dataset.equals(fboDataset) ? 
//                propeties.getImageFboCsvPath() : propeties.getImageKboCsvPath();
//        
//        records = fileProcessor.read(imageFilePath, delimiter, null);
//        log.info("records size : {}", records.size());
//        return records;
//    }
    
    public Map<String, List<String>> fboExtract(List<CSVRecord> records, JsonObject mappingJson) {
        csvCatalogNumberKey = mappingJson.getString(catalogNumberKey);
        associatedMediaJson = mappingJson.getJsonObject(associatedMediaKey);
        map = new HashMap();
        records.stream()
                .filter(isValid)
                .forEach(record -> {
                    catalogNumber = record.get(csvCatalogNumberKey);
                    photographer = record.get(mappingJson.getString(photographerKey)); 
                    smallImage = record.get(associatedMediaJson.getString(smallKey));
                    miniImage = record.get(associatedMediaJson.getString(miniKey));
                    meduimImage = record.get(associatedMediaJson.getString(mediumKey));
                    largeImage = record.get(associatedMediaJson.getString(largeKey));
                    
                    if (map.containsKey(catalogNumber)) {
                        imageList = map.get(catalogNumber);
                    } else {
                        imageList = new ArrayList(); 
                    }
                    imageList.add(buildImageUrl(photographer, smallImage));
                    imageList.add(buildImageUrl(photographer, miniImage));
                    imageList.add(buildImageUrl(photographer, meduimImage));
                    imageList.add(buildImageUrl(photographer, largeImage));
                    map.put(catalogNumber, imageList);  
                });
        return map;
    }
     
    public Map<String, List<String>> kboExtract(List<CSVRecord> records, JsonObject mappingJson) { 
         
        csvCatalogNumberKey = mappingJson.getString(catalogNumberKey);
        
        map = new HashMap();
        records.stream()
                .filter(isValid)
                .forEach(record -> {
                    catalogNumber = record.get(csvCatalogNumberKey);
                    photographer = record.get(mappingJson.getString(photographerKey));
                    associatedMedia = record.get(mappingJson.getString(associatedMediaKey));
 
                    if (map.containsKey(catalogNumber)) {
                        imageList = map.get(catalogNumber);
                    } else {
                        imageList = new ArrayList(); 
                    }
                    imageList.add(buildImageUrl(photographer, associatedMedia));
                    map.put(catalogNumber, imageList);  
                });  
        return map; 
    }

    private String buildImageUrl(String photographer, String associatedMedia) {
        imageSb = new StringBuilder();
        imageSb.append(photographer);
        imageSb.append(leftBracket);
        imageSb.append(associatedMedia);
        imageSb.append(rightBracket); 
        return imageSb.toString().trim();
    }
 
    private final Predicate<CSVRecord> isValid = record
            -> !StringUtils.isBlank(record.get(csvCatalogNumberKey));
    
}
