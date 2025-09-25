package se.nrm.samlingar.data.process.logic.bot;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import se.nrm.samlingar.data.process.config.InitialProperties;
import se.nrm.samlingar.data.process.logic.files.CSVFileProcessor;
import se.nrm.samlingar.data.process.logic.json.JsonHelper;
import se.nrm.samlingar.data.process.logic.solr.Solr;
import se.nrm.samlingar.data.process.logic.util.CommonString;
import se.nrm.samlingar.data.process.vo.Determination;
import se.nrm.samlingar.data.process.vo.NameSynonyms;

/**
 *
 * @author idali
 */
@Slf4j
public class VascularPlantsDataProcess implements Serializable {

    private final String synonymKey = "synonym";

    private final String prefix = "file_";
//    private final String prefix = "naturarvkollekt_del";

    private List<CSVRecord> records;
    private List<JsonArray> list;
    private List<CSVRecord> imageRecords;
    private List<CSVRecord> nameRecords;
    private List<CSVRecord> determinationRecords;

    private Map<String, List<Determination>> determinationMap;
    private Map<String, List<String>> imageMap;  
  
    private final String determinationkey = "determination";

    private char delimiter;
    private String encoding;
    private JsonObject imageJson;


    private final String datasetKey = "dataset";

    private final String fboDateset = "fbo";

    private String mainCsvFilePath;
    private String catalogCsvFilePath;
    private String nameCsvFilePath;
    private String determinationCsvFilePath;
    private String exsiccateCsvFilePath;
    private String collectionName;
    private String idPrefix;
    
    private String vascularPlantsDirPath;

    @Inject
    private CSVFileProcessor fileProcessor;
    @Inject
    private CsvToJsonConverter convert;
    @Inject
    private SynonymDataExtraction synonymData;
    @Inject
    private Solr solr;

    @Inject
    private ImageData imageData;

    @Inject
    private DeterminationData datermination;

    public VascularPlantsDataProcess() {

    }

    public void process(InitialProperties properties, JsonArray array,
            JsonArray imageArray, boolean delete) {

        log.info("VascularPlantsDataProcess.process");

        imageRecords = getImageCsvRecord(imageArray,
                properties.getImageFboCsvPath(), fboDateset);
 

        imageMap = imageData.fboExtract(imageRecords,
                JsonHelper.getInstance().getMappingJson(imageJson));

        log.info("imagemap : {}", imageMap.size());

        mainCsvFilePath = properties.getVascularPlantsCsvFilePath();
        catalogCsvFilePath = properties.getVascularPlantsCatalogCsvFilePath();
        nameCsvFilePath = properties.getVascularPlantsNameCsvFilePath();
        determinationCsvFilePath = properties.getVascularPlantsBestCsvFilePath();
        exsiccateCsvFilePath = properties.getVascularPlantsExsickatCsvFilePath();
 
        vascularPlantsDirPath = properties.getVascularPlantsCsvFilePath(); 

        array.getValuesAs(JsonObject.class)
                .stream()
                .filter(isProcess)
                .forEach(json -> {
                    delimiter = JsonHelper.getInstance().getDelimiter(
                            json, CommonString.getInstance().getSeparatorKey());
                    encoding = JsonHelper.getInstance().getJsonValue(
                            json, CommonString.getInstance().getEncodingKey());

                    collectionName = JsonHelper.getInstance().getCollectionName(json);
                    idPrefix = JsonHelper.getInstance().getIdPrefix(json);

                    nameRecords = fileProcessor.read(nameCsvFilePath, delimiter, encoding);
                    determinationRecords = fileProcessor.read(determinationCsvFilePath, delimiter, encoding);
                    log.info("files : {} -{}  ", nameRecords.size(), determinationRecords.size());

                    determinationMap = datermination.extractData(
                            determinationRecords, json.getJsonObject(determinationkey));
                    log.info("determinationMap : {}", determinationMap.size());

                    if (delete) {
                        solr.deleteCollection(idPrefix);
                    }
 
                    Map<String, List<String>> synonymsMap = synonymData.extract(nameRecords,
                            json.getJsonObject(synonymKey));
                    log.info("synonymsMap : {}", synonymsMap.size());

                    
                    try {
                        Files.list(Paths.get(vascularPlantsDirPath))
                                .map(Path::toFile)
                                .filter(File::isFile)
                                .filter(f -> f.getName().startsWith(prefix))
                                .sorted()
                                .forEach(file -> { 
                                    records = fileProcessor.read(file, delimiter, encoding);
                                    log.info("records size : {} -- {}", file.getName(), records.size());

                                    list = convert.vascularPlantsConvert(records,
                                            synonymsMap, determinationMap, imageMap,
                                            idPrefix, collectionName, json);

                                    list.stream()
                                            .forEach(l -> {
                                                int status = solr.postToSolr(l.toString().trim());
                                                log.info("status : {}", status);
                                            });
                                    records = null;
                                });
                    } catch (IOException ex) {
                        log.error("error...{}", ex.getMessage());
                    }

                });

    }

    public void process(String vascularPlantsDirPath, JsonObject mappingJson,
            JsonObject submappingJson, char delimiter, String encoding,
            String idPrefix, String collectionName, List<CSVRecord> nameRecords,
            Map<String, List<Determination>> determinationMap,
            Map<String, List<String>> imageMap) {
        log.info("vascularPlantsDirectioy : {}", vascularPlantsDirPath);

        Map<String, List<String>> synonymsMap = synonymData.extract(nameRecords,
                submappingJson.getJsonObject(synonymKey));
        log.info("synonymsMap : {}", synonymsMap.size());

        try {
            Files.list(Paths.get(vascularPlantsDirPath))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .filter(f -> f.getName().startsWith(prefix))
                    .sorted()
                    .forEach(file -> {
                        log.info("file name : {}", file.getName());
                        records = fileProcessor.read(file, delimiter, encoding);
                        log.info("records size : {} -- {}", file.getName(), records.size());

                        list = convert.vascularPlantsConvert(records,
                                synonymsMap, determinationMap, imageMap,
                                idPrefix, collectionName, mappingJson);

                        list.stream()
                                .forEach(l -> {
                                    int status = solr.postToSolr(l.toString().trim());
                                    log.info("status : {}", status);
                                });
                        records = null;
                    });
        } catch (IOException ex) {
            log.error("error...{}", ex.getMessage());
        }
    }

    private List<CSVRecord> getImageCsvRecord(JsonArray imageArray, String imageFilePath, String dateset) {
        log.info("getImageCsvRecord : {}  ", imageFilePath);

        imageJson = getImageMappingJson(imageArray, dateset);

        delimiter = JsonHelper.getInstance().getDelimiter(imageJson,
                CommonString.getInstance().getSeparatorKey());

        return fileProcessor.read(imageFilePath, delimiter, null);
    }

    private JsonObject getImageMappingJson(JsonArray imageArray, String dateset) {
        log.info("getImageMappingJson : {} ", dateset);

        return imageArray.getValuesAs(JsonObject.class)
                .stream()
                .filter(a -> a.getString(datasetKey).equals(dateset))
                .findFirst()
                .get();
    }

//    public void process(String vascularPlantsDirPath, JsonObject mappingJson, 
//            JsonObject submappingJson, char delimiter, String encoding, 
//            String idPrefix, String collectionName, List<CSVRecord> nameRecords, 
//            Map<String, List<DeterminationVo>> determinationMap,
//            Map<String, List<String>> imageMap) {
//        log.info("vascularPlantsDirectioy : {}", vascularPlantsDirPath); 
//         
//        Map<String, List<String>> synonymsMap = synonymData.extract(nameRecords, 
//                submappingJson.getJsonObject(synonymKey));
//        log.info("synonymsMap : {}", synonymsMap.size());
//         
//        try {
//            Files.list(Paths.get(vascularPlantsDirPath))
//                    .map(Path::toFile)
//                    .filter(File::isFile)
//                    .filter(f -> f.getName().startsWith(prefix))
//                    .sorted()
//                    .forEach(file -> {
//                        log.info("file name : {}", file.getName());
//                        records =  fileProcessor.read(file, delimiter, encoding);
//                        log.info("records size : {} -- {}", file.getName(), records.size()); 
//                        
//                        list = convert.vascularPlantsConvert(records, 
//                                synonymsMap, determinationMap, imageMap,
//                                idPrefix, collectionName, mappingJson, submappingJson);
//                     
//                        list.stream()
//                            .forEach(l -> {
//                                int status = solr.postToSolr(l.toString().trim());
//                                log.info("status : {}", status);
//                            });
//                        records = null;
//                    }); 
//        } catch (IOException ex) {
//            log.error("error...{}", ex.getMessage());
//        } 
//    } 
    private final Predicate<JsonObject> isProcess = json -> json.getBoolean(
            CommonString.getInstance().getProcessKey(), true);
}
