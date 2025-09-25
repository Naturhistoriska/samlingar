package se.nrm.samlingar.data.process.logic;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.json.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.Swarm;
import se.nrm.samlingar.data.process.config.InitialProperties;
import se.nrm.samlingar.data.process.logic.bot.BotDataProcessor;
import se.nrm.samlingar.data.process.logic.bot.VascularPlantsDataProcess;
import se.nrm.samlingar.data.process.logic.files.JsonFileHandler;
import se.nrm.samlingar.data.process.logic.paleo.PaleoDataProcessor;
import se.nrm.samlingar.data.process.logic.zoo.ZooDataProcessor;

/**
 *
 * @author idali
 */
@Slf4j
public class DataProcessor implements Serializable {

    private final String botCollection = "bot";
    private final String paleoCollection = "paleo";
    private final String zooCollection = "zoo";
    private final String fboCollection = "fbo";

    private JsonArray array;
    private String collection;
    private String csvFilePath;
    private boolean deleteData;
//    private String imageMappingFile;
    private JsonArray imageArray;

    @Inject
    private InitialProperties propeties;
    @Inject
    private JsonFileHandler fileHander;

    @Inject
    private BotDataProcessor bot;
    @Inject
    private PaleoDataProcessor paleo;
    @Inject
    private ZooDataProcessor zoo;
    @Inject
    private VascularPlantsDataProcess fbo;
    @Inject
    private JsonFileHandler jsonFileHander;

    public DataProcessor() {

    }

    public void run() {
        log.info("run ");
        try {
            collection = propeties.getCollection();
            log.info("collection : {}", collection);
            
            imageArray = jsonFileHander.readMappingJsonFile(getImageMappingFilePath());

            
            if (collection.equals(fboCollection)) {
                array = fileHander.readMappingJsonFile(getMappingFilePath(botCollection));
  
                deleteData = delete(); 
                fbo.process(propeties, array, imageArray, deleteData);
            } else {
                array = fileHander.readMappingJsonFile(getMappingFilePath(collection));

                csvFilePath = getCsvFilePath(collection);
                deleteData = delete();
  
                switch (collection) {
                    case zooCollection:
                        zoo.process(csvFilePath, array,
                                imageArray, getZooImageFilePath(), deleteData);
                        break;
                    case paleoCollection:
                        paleo.process(csvFilePath, array, deleteData);
                        break;
                    case botCollection:
                        bot.process(propeties, array, imageArray, deleteData);
                        break; 
                    default:
                        break;
                }

            }
        } catch (RuntimeException e) {
            log.error("error... {}", e.getMessage());
        }

        stopServer();
    }

    /**
     *
     * @return boolean delete data from solr
     */
    private boolean delete() {
        return propeties.getDeleteCollection();
    }

    /**
     *
     * @param collection
     * @return getCsvFile
     */
    private String getCsvFilePath(String collection) {
        return propeties.getCsvFile(collection);
    }

    private String getImageMappingFilePath() {
        return propeties.getImageMappingFilePath();
    }

    /**
     *
     * @param collection
     * @return mappingFilePath
     */
    private String getMappingFilePath(String collection) {
        return propeties.getMapingFilePath(collection);
    }

    private String getZooImageFilePath() {
        return propeties.getImageZooCsvPath();
    }

    private void stopServer() {
        log.info("stopServer");
        try {
            TimeUnit.SECONDS.sleep(20);
            Thread.currentThread().interrupt();
            Swarm.stopMain();
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

}
