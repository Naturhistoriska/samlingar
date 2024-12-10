package se.nrm.samlingar.data.process.logic.zoo.images;

import com.sun.javafx.scene.control.skin.VirtualFlow;
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
public class ImageDataProcessor implements Serializable {
    
    private final String datasetKey = "dataset";
    private final String catalogNumberKey = "catalogNumber";
    private final String associatedMediaKey = "associatedMedia"; 
    private final String thumbKey = "thumb";
    private final String largeKey = "large"; 
    
    private final String fullSize = "Full_size/";
    private final String thumbnails = "Thumbnails/"; 
     
    private String csvCatalogNumberKey; 
    private String csvThumbKey;
    private String csvLargeImageKey;

    private String catalogNumber;

    private String thumbImage;
    private String largeImage;
 
    private List<String> imageList;
    private Map<String, List<String>> map;
 
    private JsonArray imageArray;
  
    private JsonObject associatedMediaJson; 

    @Inject
    private JsonFileHandler fileHander;

    public ImageDataProcessor() {

    }

    public Map<String, List<String>> run(JsonObject imageJson, List<CSVRecord> records) {  
        csvCatalogNumberKey = imageJson.getString(catalogNumberKey);

        associatedMediaJson = imageJson.getJsonObject(associatedMediaKey);  
        
        csvThumbKey = associatedMediaJson.getString(thumbKey);
        csvLargeImageKey = associatedMediaJson.getString(largeKey); 
        
        imageList = new ArrayList<>();
        map = new HashMap();
        records.stream()
                .filter(isValid)
                .forEach(record -> {
                    catalogNumber = record.get(csvCatalogNumberKey); 

                    if (map.containsKey(catalogNumber)) {
                        imageList = map.get(catalogNumber);
                    } else {
                        imageList = new ArrayList();
                    }
                    thumbImage = record.get(csvThumbKey);
                    largeImage = record.get(csvLargeImageKey);
                    
                    thumbImage = thumbnails + thumbImage;
                    largeImage = fullSize + largeImage;
               
                    imageList.add(thumbImage);
                    imageList.add(largeImage);

                    map.put(catalogNumber, imageList);
                }); 
        return map;
    }


    public JsonObject getImageMappingJson(String imageFilePath, String dataset) {
        log.info("getImageMappingJson : {}", imageFilePath);
        imageArray = fileHander.readMappingJsonFile(imageFilePath);

        return imageArray.getValuesAs(JsonObject.class)
                .stream()
                .filter(a -> a.getString(datasetKey).equals(dataset))
                .findFirst()
                .get();
    }

    private final Predicate<CSVRecord> isValid = record
            -> !StringUtils.isBlank(record.get(csvCatalogNumberKey));
}
