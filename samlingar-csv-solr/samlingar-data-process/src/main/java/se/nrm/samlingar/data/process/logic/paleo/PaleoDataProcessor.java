package se.nrm.samlingar.data.process.logic.paleo;

import java.io.Serializable;
import java.util.List;
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

/**
 *
 * @author idali
 */
@Slf4j
public class PaleoDataProcessor implements Serializable {
    
    private String fileName;
    private char delimiter;
    private String encoding;
    private String collectionId;

    private List<CSVRecord> records;
    private List<JsonArray> list;
    
    private int status;
    
    
    
    @Inject
    private CSVFileProcessor fileProcessor;
    @Inject
    private PaleoJsonConverter jsonConverter;
    @Inject
    private Solr solr;
    
    public PaleoDataProcessor() {

    }

    public void process(String filePath, JsonArray array, boolean delete) {
        log.info("process : {} -- {}", filePath, delete);
        try {
            if (array != null) {
                array.getValuesAs(JsonObject.class).stream()
                        .filter(isProcess)
                        .forEach(collectionJson -> {
                            fileName = JsonHelper.getInstance().getJsonValue(
                                    collectionJson, CommonString.getInstance().getFileNameKey());
                            
                            collectionId = JsonHelper.getInstance().getIdPrefix(collectionJson);
                            
                            delimiter = JsonHelper.getInstance().getDelimiter(
                                    collectionJson, CommonString.getInstance().getSepartorKey());
                            encoding = JsonHelper.getInstance().getJsonValue(
                                    collectionJson, CommonString.getInstance().getEncodingKey());
                            log.info("encoding... {}", encoding);
                            
                            
                            records = fileProcessor.read(filePath, fileName, delimiter, encoding);
                            log.info("records size : {}", records.size());
                            if (records != null) {
                                if (delete) {
                                    solr.deleteCollection(collectionId);
                                }

                                list = jsonConverter.convertCsvToJson(records, collectionJson, collectionId);
  
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

    private final Predicate<JsonObject> isProcess = json -> json.getBoolean(
            CommonString.getInstance().getProcessKey(), true);
}
