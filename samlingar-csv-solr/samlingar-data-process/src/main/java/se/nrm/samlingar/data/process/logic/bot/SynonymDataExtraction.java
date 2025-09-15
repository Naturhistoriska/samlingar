package se.nrm.samlingar.data.process.logic.bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author idali
 */
@Slf4j
public class SynonymDataExtraction implements Serializable {
    private final String catalogIdKey = "catalogId";
    private final String synonymKey = "synonym";
    
    private Map<String, List<String>> synomyMap; 
    private List<String> list;
    
    private String synonym; 
    private String catalogId;
    
    private String csvCatalogIdKey;
    private String csvSynonymKey;
    
        
    public SynonymDataExtraction() {
        
    }
    
    public Map<String, List<String>> extract(List<CSVRecord> nameRecords, JsonObject json) {
        log.info("extractSynonyms : {}", json);
          
        synomyMap = new LinkedHashMap<>(); 
        synonym = null; 
        catalogId = null; 

        csvCatalogIdKey = json.getString(catalogIdKey);
        csvSynonymKey = json.getString(synonymKey);
        nameRecords.stream()
                .forEach(nameRecord -> {
                    catalogId = nameRecord.get(csvCatalogIdKey);  
                    synonym = nameRecord.get(csvSynonymKey);    
                    if (synomyMap.containsKey(catalogId)) { 
                        list = synomyMap.get(catalogId); 
                    } else {
                        list = new ArrayList<>(); 
                    } 
                    list.add(synonym);
                    synomyMap.put(catalogId, list);
                });
        return synomyMap; 
    }
}
