package se.nrm.samlingar.data.process.logic.bot;

import java.io.Serializable;
import java.util.ArrayList; 
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.data.process.vo.Determination;

/**
 *
 * @author idali
 */
@Slf4j
public class DeterminationData implements Serializable {
    private final String determinationKey = "currentDetermination"; 
    private final String identifiedByKey = "identifiedBy"; 
    private final String dateIdentifiedKey = "dateIdentified"; 
    
    private Map<String, List<Determination>> determinationMap;
    private String currentDetermination;
    private String regNumber;
    private String identifiedBy; 
    private String dateIdentified;
    
    private String csvDeterminationKey;
    private String csvIdentifiedByKey; 
    private String csvDateIdentifiedKey; 
    
    private  List<Determination> determinationList;
    private Determination determination; 
    
    public DeterminationData() {
        
    }
    
    public Map<String, List<Determination>> extractData(
                List<CSVRecord> determinationRecords, JsonObject json) {
        log.info("extractDeterminationData : {}", json);
        
        determinationMap = new LinkedHashMap(); 
        currentDetermination = null;
        
        csvDeterminationKey = json.getString(determinationKey);
        csvIdentifiedByKey = json.getString(identifiedByKey); 
          
        if(json.containsKey(dateIdentifiedKey)) {
            csvDateIdentifiedKey = json.getString(dateIdentifiedKey);
        }  
        
     
        determinationRecords.stream()
                .forEach(detRecord -> {
                    currentDetermination = detRecord.get(csvDeterminationKey);
                    if (!StringUtils.isBlank(currentDetermination)) {
                        regNumber = detRecord.get(0);
                        identifiedBy = detRecord.get(csvIdentifiedByKey);
                          
                        if(csvDateIdentifiedKey != null) {
                            dateIdentified =  detRecord.get(csvDateIdentifiedKey);
                        }
                        
                        determination = new Determination(currentDetermination, 
                                identifiedBy,  dateIdentified);
                        
                        if (determinationMap.containsKey(regNumber)) {
                            determinationList = determinationMap.get(regNumber); 
                        } else {
                            determinationList = new ArrayList(); 
                        }
                        determinationList.add(determination);
                        determinationMap.put(regNumber, determinationList);
                    }
                });
        return determinationMap;
    } 
}
