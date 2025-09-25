package se.nrm.samlingar.data.process.logic.bot;

import java.io.Serializable;
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
public class BotDataProcessor implements Serializable {

    private final String mosses = "mosses";
    private final String vascularPlants = "vascularPlants";

    private final String fungi = "fungi";
    private final String kboDateset = "kbo"; 
    private final String datasetKey = "dataset";

    private final String determinationkey = "determination";

    private String fileName;
//    private String imageFileName;

    private String mainCsvFilePath;
    private String catalogCsvFilePath;
    private String nameCsvFilePath;
    private String determinationCsvFilePath;
    private String exsiccateCsvFilePath;

    private String taxonCsvFilePath;

    private char delimiter;
    private String encoding;
    private String collectionName;
    private String idPrefix;

    private Map<String, List<Determination>> determinationMap;
    private Map<String, List<String>> imageMap;
    private Map<String, String> exisccateMap;
    private Map<String, NameSynonyms> nameMap;

    private List<CSVRecord> records;
    private List<CSVRecord> catalogRecords;

    private List<CSVRecord> nameRecords;
    private List<CSVRecord> determinationRecords;
    private List<CSVRecord> exsiccateRecords;
    private List<CSVRecord> imageRecords;

    private List<CSVRecord> taxonRecords;

    private JsonObject imageJson;

    private JsonObject mappingJson;

    private List<JsonArray> list;
    private int status;

    @Inject
    private Solr solr;
    @Inject
    private CSVFileProcessor fileProcessor;

    @Inject
    private DeterminationData datermination;
    
    @Inject
    private CsvDataExtraction extractor;

    @Inject
    private ImageData imageData;

    @Inject
    private BotJsonConverter converter;

    public BotDataProcessor() {

    }

    public void process(InitialProperties properties, JsonArray array,
            JsonArray imageArray, boolean delete) {
        log.info("process");

        try {
            if (array != null) {
                array.getValuesAs(JsonObject.class)
                        .stream()
                        .filter(isProcess)
                        .forEach(json -> {

                            fileName = JsonHelper.getInstance().getJsonValue(
                                    json, CommonString.getInstance().getFileNameKey());
                            log.info("fileName : {}", fileName);

                            setupFilesPathe(fileName, properties);
                            delimiter = JsonHelper.getInstance().getDelimiter(
                                    json, CommonString.getInstance().getSeparatorKey());
                            encoding = JsonHelper.getInstance().getJsonValue(
                                    json, CommonString.getInstance().getEncodingKey());
                            collectionName = JsonHelper.getInstance().getCollectionName(json);
                            idPrefix = JsonHelper.getInstance().getIdPrefix(json);

                            if (delete) {
                                solr.deleteCollection(idPrefix);
                            }

                            mappingJson = JsonHelper.getInstance().getMappingJson(json);
                            log.info("mappingJosn : {}", mappingJson);

                            nameRecords = fileProcessor.read(nameCsvFilePath, delimiter, encoding);
                            determinationRecords = fileProcessor.read(determinationCsvFilePath, delimiter, encoding);
                            log.info("files : {} -{}  ", nameRecords.size(), determinationRecords.size());

                            determinationMap = datermination.extractData(
                                    determinationRecords, json.getJsonObject(determinationkey));
                            log.info("determinationMap : {}", determinationMap.size());

                            imageRecords = getImageCsvRecord(imageArray,
                                    properties.getImageKboCsvPath(), kboDateset);
                            log.info("image records : {}", imageRecords.size());

                            imageMap = imageData.kboExtract(imageRecords,
                                    JsonHelper.getInstance().getMappingJson(imageJson));

                            catalogRecords = fileProcessor.read(catalogCsvFilePath, delimiter, encoding);
                            nameMap = extractor.extractName(nameRecords, catalogRecords);

                            log.info("done.....");

                            exsiccateRecords = fileProcessor.read(exsiccateCsvFilePath, delimiter, encoding);
                            exisccateMap = extractor.extractExsiccate(exsiccateRecords);

                            records = fileProcessor.read(mainCsvFilePath, delimiter, encoding);
                            log.info("records size : {}", records.size());

                            list = converter.convert(records, nameMap, exisccateMap, determinationMap,
                                    imageMap, collectionName, idPrefix, json);

                            list.stream()
                                    .forEach(l -> {
                                        status = solr.postToSolr(l.toString().trim());
                                        log.info("status : {}", status);
                                    });
//                            if (fileName.equals(vascularPlants)) {
//                                imageMap = imageData.fboExtract(imageRecords,
//                                        JsonHelper.getInstance().getMappingJson(imageJson));
//                                vascularPlantsProcess.process(mainCsvFilePath,
//                                        mappingJson, submappingJson, delimiter, encoding, idPrefix,
//                                        collectionName, nameRecords, determinationMap, imageMap);
//                            } else {
//                                imageRecords = getImageCsvRecord(imageArray,
//                                        properties.getImageKboCsvPath(), kboDateset);
//                                log.info("image records : {}", imageRecords.size());
//
//                                imageMap = imageData.kboExtract(imageRecords,
//                                        JsonHelper.getInstance().getMappingJson(imageJson));
//
//                                catalogRecords = fileProcessor.read(catalogCsvFilePath, delimiter, encoding);
//                                nameMap = extractor.extractName(nameRecords, catalogRecords);
//
//                                log.info("done.....");
//
//                                exsiccateRecords = fileProcessor.read(exsiccateCsvFilePath, delimiter, encoding);
//                                exisccateMap = extractor.extractExsiccate(exsiccateRecords);
//
//                                records = fileProcessor.read(mainCsvFilePath, delimiter, encoding);
//                                log.info("records size : {}", records.size());
//
//                                list = converter.convert(records, nameMap, exisccateMap, determinationMap,
//                                        imageMap, collectionName, idPrefix, json);
//
//                                list.stream()
//                                        .forEach(l -> {
//                                            status = solr.postToSolr(l.toString().trim());
//                                            log.info("status : {}", status);
//                                        });
//
////                              
//                            }

//                            if (fileName.equals(vascularPlants)) {
//                                imageMap = imageExtractor.fboExtract(imageRecords,
//                                        JsonHelper.getInstance().getMappingJson(imageJson));
//                                vascularPlantsProcess.process(mainCsvFilePath,
//                                        mappingJson, submappingJson, delimiter, encoding, idPrefix,
//                                        collectionName, nameRecords, determinationMap, imageMap);
//                            } else {
//                                catalogRecords = fileProcessor.read(catalogCsvFilePath, delimiter, encoding);
//                                nameMap = extractor.extractName(nameRecords, catalogRecords);
//
//                                exsiccateRecords = fileProcessor.read(exsiccateCsvFilePath,
//                                        delimiter, encoding);
//                                exisccateMap = extractor.extractExsiccate(exsiccateRecords);
//                                log.info("nameMap and exisccateMap: {} -- {}", nameMap.size(), exisccateMap.size());
//                                log.info("files 1: {}", catalogRecords.size());
//
//                                imageMap = imageExtractor.kboExtract(imageRecords,
//                                        JsonHelper.getInstance().getMappingJson(imageJson));
//
//                                records = fileProcessor.read(mainCsvFilePath, delimiter, encoding);
//                                log.info("records size : {}", records.size());
//
//                                if (records != null) {
//                                    convertAndPost(records);
//                                }
//                                if (fileName.equals(fungi)) {
//
//                                    try {
//                                        log.info("fungiDirectioy : {}", mainCsvFilePath);
//
//                                        Files.list(Paths.get(mainCsvFilePath))
//                                                .map(Path::toFile)
//                                                .filter(File::isFile)
//                                                .filter(f -> f.getName().startsWith(prefix))
//                                                .sorted()
//                                                .forEach(file -> {
//                                                    log.info("file name : {}", file.getName());
//                                                    records = fileProcessor.read(file, delimiter, encoding);
//                                                    log.info("records  : {} -- {}", file.getName(), records.size());
//                                                    if (records != null) {
//                                                        convertAndPost(records);
////                                                        list = convert.convert(records, nameMap, exisccateMap, determinationMap,
////                                                                imageMap, collectionName, idPrefix, mappingJson, submappingJson);
////
////                                                        list.stream()
////                                                                .forEach(l -> {
////                                                                    int status = solrWithAuth.postToSolr(l.toString().trim());
////                                                                    log.info("status : {}", status);
////                                                                });
//                                                    }
//                                                });
//                                    } catch (IOException ex) {
//                                        log.error(ex.getMessage());
//                                    } 
//                                } else {
//                                    records = fileProcessor.read(mainCsvFilePath, delimiter, encoding);
//                                    log.info("records size : {}", records.size());
//
//                                    if (records != null) {
//                                        convertAndPost(records);
//                                    }
//                                }
//                            }
//
                        });
            }
        } catch (RuntimeException e) {
            log.error("error... {}", e.getMessage());
        }
    }

    private JsonObject getImageMappingJson(JsonArray imageArray, String dateset) {
        log.info("getImageMappingJson : {}");

        return imageArray.getValuesAs(JsonObject.class)
                .stream()
                .filter(a -> a.getString(datasetKey).equals(dateset))
                .findFirst()
                .get();
    }

    private List<CSVRecord> getImageCsvRecord(JsonArray imageArray, String imageFilePath, String dateset) {

        imageJson = getImageMappingJson(imageArray, dateset);
        delimiter = JsonHelper.getInstance().getDelimiter(imageJson,
                CommonString.getInstance().getSeparatorKey());

//        imageFileName = JsonHelper.getInstance().getFileName(imageJson);

        return fileProcessor.read(imageFilePath, delimiter, null);
    }

    private void setupFilesPathe(String fileName, InitialProperties properties) {
        switch (fileName) {
            case fungi:
                mainCsvFilePath = properties.getFungiCsvFilePath();
                mainCsvFilePath = properties.getFungiMainCsvFilePath();
                catalogCsvFilePath = properties.getFungiCatalogCsvFilePath();
                nameCsvFilePath = properties.getFungiNameCsvFilePath();
                determinationCsvFilePath = properties.getFungiBestCsvFilePath();
                exsiccateCsvFilePath = properties.getFungiExsickatCsvFilePath();
                taxonCsvFilePath = properties.getFungiTaxonCsvFilePath();
                break;
            case mosses:
                mainCsvFilePath = properties.getMossesMainCsvFilePath();
                catalogCsvFilePath = properties.getMossesCatalogCsvFilePath();
                nameCsvFilePath = properties.getMossesNameCsvFilePath();
                determinationCsvFilePath = properties.getMossesBestCsvFilePath();
                exsiccateCsvFilePath = properties.getMossesExsickatCsvFilePath();
                break;
            case vascularPlants:
                mainCsvFilePath = properties.getVascularPlantsCsvFilePath();
                catalogCsvFilePath = properties.getVascularPlantsCatalogCsvFilePath();
                nameCsvFilePath = properties.getVascularPlantsNameCsvFilePath();
                determinationCsvFilePath = properties.getVascularPlantsBestCsvFilePath();
                exsiccateCsvFilePath = properties.getVascularPlantsExsickatCsvFilePath();
                break;
            default:
                mainCsvFilePath = properties.getAlgaeMainCsvFilePath();
                catalogCsvFilePath = properties.getAlgaeCatalogCsvFilePath();
                nameCsvFilePath = properties.getAlgaeNameCsvFilePath();
                determinationCsvFilePath = properties.getAlgaeBestCsvFilePath();
                exsiccateCsvFilePath = properties.getAlgaeExsickatCsvFilePath();
                break;
        }

    }

    private final Predicate<JsonObject> isProcess = json -> json.getBoolean(
            CommonString.getInstance().getProcessKey(), true);

}
