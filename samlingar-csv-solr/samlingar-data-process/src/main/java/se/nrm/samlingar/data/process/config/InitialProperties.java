package se.nrm.samlingar.data.process.config;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Slf4j
public class InitialProperties implements Serializable {

    private final static String CONFIG_INITIALLISING_ERROR = "Property not initialized";

    private final String botCollection = "bot";
    private final String paleoCollection = "paleo";
    private final String zooCollection = "zoo";

    private String paleoCsvFilePath;
    private String botCsvFilePath;
    private String zooCsvFilePath;

    private String fungiCsvFilePath;
    private String fungiMainCsvPath;
    private String fungiBestCsvPath;
    private String fungiNameCsvPath;
    private String fungiCatalogCsvPath;
    private String fungiExsickatCsvPath;
    private String fungiTaxonCsvPath;

    private String mossesCsvFilePath;
    private String mossesMainCsvPath;
    private String mossesBestCsvPath;
    private String mossesNameCsvPath;
    private String mossesCatalogCsvPath;
    private String mossesExsickatCsvPath;

    private String algaeCsvFilePath;
    private String algaeMainCsvPath;
    private String algaeBestCsvPath;
    private String algaeNameCsvPath;
    private String algaeCatalogCsvPath;
    private String algaeExsickatCsvPath;

    private String vascularPlantsCsvFilePath;
    private String vascularPlantsMainCsvPath;
    private String vascularPlantsBestCsvPath;
    private String vascularPlantsNameCsvPath;
    private String vascularPlantsCatalogCsvPath;
    private String vascularPlantsExsickatCsvPath;

    private String imageFboCsvPath;
    private String imageKboCsvPath;
    private String imageZooCsvPath;

    private String paleoMappingfilePath;
    private String botMappingfilePath;
    private String zooMappingFilePath;
    private String imageMappingFilePath;

    private String solrPath;
    private String baseSolrUrl;

    private String collection;

    private boolean delectCollection;
    private String prefix;

    private String username;
    private String password;

//  private String fromDate;
//  private String toDate;
    public InitialProperties() {
    }

    @Inject
    public InitialProperties(@ConfigurationValue("swarm.solr.baseUrl") String baseSolrUrl,
            @ConfigurationValue("swarm.solr.path") String solrPath,
            @ConfigurationValue("swarm.solr.username") String username,
            @ConfigurationValue("swarm.solr.password") String password,
            @ConfigurationValue("swarm.csv.file.paths.paleobiology.csv") String paleoCsvFilePath,
            @ConfigurationValue("swarm.csv.file.paths.bot") String botCsvFilePath,
            @ConfigurationValue("swarm.csv.file.paths.zoo.csv") String zooCsvFilePath,
            @ConfigurationValue("swarm.csv.file.paths.bot.fungi.fungidir") String fungiCsvFilePath,
            @ConfigurationValue("swarm.csv.file.paths.bot.fungi.main") String fungiMainCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.fungi.best") String fungiBestCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.fungi.name") String fungiNameCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.fungi.exsickat") String fungiExsickatCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.fungi.catalog") String fungiCatalogCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.fungi.taxon") String fungiTaxonCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.mosses") String mossesCsvFilePath,
            @ConfigurationValue("swarm.csv.file.paths.bot.mosses.main") String mossesMainCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.mosses.best") String mossesBestCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.mosses.name") String mossesNameCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.mosses.exsickat") String mossesExsickatCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.mosses.catalog") String mossesCatalogCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.algae") String algaeCsvFilePath,
            @ConfigurationValue("swarm.csv.file.paths.bot.algae.main") String algaeMainCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.algae.best") String algaeBestCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.algae.name") String algaeNameCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.algae.exsickat") String algaeExsickatCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.algae.catalog") String algaeCatalogCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.vascular_plants.vpdir") String vascularPlantsCsvFilePath,
            @ConfigurationValue("swarm.csv.file.paths.bot.vascular_plants.main") String vascularPlantsMainCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.vascular_plants.best") String vascularPlantsBestCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.vascular_plants.name") String vascularPlantsNameCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.vascular_plants.exsickat") String vascularPlantsExsickatCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.vascular_plants.catalog") String vascularPlantsCatalogCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.image.fbo") String imageFboCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.bot.image.kbo") String imageKboCsvPath,
            @ConfigurationValue("swarm.csv.file.paths.zoo.image") String imageZooCsvPath,
            @ConfigurationValue("swarm.mapping.files.paleo") String paleoMappingfilePath,
            @ConfigurationValue("swarm.mapping.files.bot") String botMappingfilePath,
            @ConfigurationValue("swarm.mapping.files.zoo") String zooMappingFilePath,
            @ConfigurationValue("swarm.mapping.files.image") String imageMappingFilePath,
            @ConfigurationValue("swarm.prefix") String prefix,
            @ConfigurationValue("swarm.collection") String collection,
            @ConfigurationValue("swarm.delect.collection") boolean deleteCollection
    //          @ConfigurationValue("swarm.date.toDate") String toDate) {
    ) {
        this.baseSolrUrl = baseSolrUrl;
        this.solrPath = solrPath;
        this.username = username;
        this.password = password;
        this.paleoCsvFilePath = paleoCsvFilePath;
        this.botCsvFilePath = botCsvFilePath;
        this.zooCsvFilePath = zooCsvFilePath;
        this.fungiCsvFilePath = fungiCsvFilePath;
        this.fungiBestCsvPath = fungiBestCsvPath;
        this.fungiCatalogCsvPath = fungiCatalogCsvPath;
        this.fungiExsickatCsvPath = fungiExsickatCsvPath;
        this.fungiMainCsvPath = fungiMainCsvPath;
        this.fungiNameCsvPath = fungiNameCsvPath;
        this.fungiTaxonCsvPath = fungiTaxonCsvPath;
        this.mossesBestCsvPath = mossesBestCsvPath;
        this.mossesCatalogCsvPath = mossesCatalogCsvPath;
        this.mossesCsvFilePath = mossesCsvFilePath;
        this.mossesExsickatCsvPath = mossesExsickatCsvPath;
        this.mossesMainCsvPath = mossesMainCsvPath;
        this.mossesNameCsvPath = mossesNameCsvPath;
        this.algaeBestCsvPath = algaeBestCsvPath;
        this.algaeCatalogCsvPath = algaeCatalogCsvPath;
        this.algaeCsvFilePath = algaeCsvFilePath;
        this.algaeExsickatCsvPath = algaeExsickatCsvPath;
        this.algaeMainCsvPath = algaeMainCsvPath;
        this.algaeNameCsvPath = algaeNameCsvPath;
        this.vascularPlantsCsvFilePath = vascularPlantsCsvFilePath;
        this.vascularPlantsMainCsvPath = vascularPlantsMainCsvPath;
        this.vascularPlantsBestCsvPath = vascularPlantsBestCsvPath;
        this.vascularPlantsNameCsvPath = vascularPlantsNameCsvPath;
        this.vascularPlantsExsickatCsvPath = vascularPlantsExsickatCsvPath;
        this.vascularPlantsCatalogCsvPath = vascularPlantsCatalogCsvPath;
        this.imageFboCsvPath = imageFboCsvPath;
        this.imageKboCsvPath = imageKboCsvPath;
        this.imageZooCsvPath = imageZooCsvPath;
        this.paleoMappingfilePath = paleoMappingfilePath;
        this.botMappingfilePath = botMappingfilePath;
        this.zooMappingFilePath = zooMappingFilePath;
        this.imageMappingFilePath = imageMappingFilePath;
        this.prefix = prefix;
        this.collection = collection;
        this.delectCollection = deleteCollection;
        log.info("InitialProperties : {} -- {}", collection, delectCollection);
    }

    public String getPaleoCsvFilePath() {
        if (paleoCsvFilePath == null) {
            log.error("paleoCsvFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return paleoCsvFilePath;
    }

    public String getBotCsvFilePath() {
        if (botCsvFilePath == null) {
            log.error("botCsvFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return botCsvFilePath;
    }

    public String getFungiCsvFilePath() {
        if (fungiCsvFilePath == null) {
            log.error("fungiCsvFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return fungiCsvFilePath;
    }

    public String getFungiBestCsvFilePath() {
        if (fungiBestCsvPath == null) {
            log.error("fungiCsvFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return fungiBestCsvPath;
    }

    public String getFungiCatalogCsvFilePath() {
        if (fungiCatalogCsvPath == null) {
            log.error("error: fungiCatalogCsvPath file not found ");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return fungiCatalogCsvPath;
    }

    public String getFungiExsickatCsvFilePath() {
        if (fungiExsickatCsvPath == null) {
            log.error("fungiExsickatCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return fungiExsickatCsvPath;
    }

    public String getFungiMainCsvFilePath() {
        if (fungiMainCsvPath == null) {
            log.error("fungiMainCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return fungiMainCsvPath;
    }

    public String getFungiNameCsvFilePath() {
        if (fungiNameCsvPath == null) {
            log.error("fungiNameCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return fungiNameCsvPath;
    }
    
    public String getFungiTaxonCsvFilePath() {
        if (fungiTaxonCsvPath == null) {
            log.error("fungiTaxonCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return fungiTaxonCsvPath;
    }

    public String getMossesCsvFilePath() {
        if (mossesCsvFilePath == null) {
            log.error("mossesCsvFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return mossesCsvFilePath;
    }

    public String getMossesBestCsvFilePath() {
        if (mossesBestCsvPath == null) {
            log.error("mossesBestCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return mossesBestCsvPath;
    }

    public String getMossesCatalogCsvFilePath() {
        if (mossesCatalogCsvPath == null) {
            log.error("error: fungiCatalogCsvPath file not found ");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return mossesCatalogCsvPath;
    }

    public String getMossesExsickatCsvFilePath() {
        if (mossesExsickatCsvPath == null) {
            log.error("mossesExsickatCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return mossesExsickatCsvPath;
    }

    public String getMossesMainCsvFilePath() {
        if (mossesMainCsvPath == null) {
            log.error("mossesMainCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return mossesMainCsvPath;
    }

    public String getMossesNameCsvFilePath() {
        if (mossesNameCsvPath == null) {
            log.error("mossesNameCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return mossesNameCsvPath;
    }

    public String getAlgaeCsvFilePath() {
        if (algaeCsvFilePath == null) {
            log.error("algaeCsvFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return algaeCsvFilePath;
    }

    public String getAlgaeBestCsvFilePath() {
        if (algaeBestCsvPath == null) {
            log.error("algaeBestCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return algaeBestCsvPath;
    }

    public String getAlgaeCatalogCsvFilePath() {
        if (algaeCatalogCsvPath == null) {
            log.error("algaeCatalogCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return algaeCatalogCsvPath;
    }

    public String getAlgaeExsickatCsvFilePath() {
        if (algaeExsickatCsvPath == null) {
            log.error("algaeExsickatCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return algaeExsickatCsvPath;
    }

    public String getAlgaeMainCsvFilePath() {
        if (algaeMainCsvPath == null) {
            log.error("algaeMainCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return algaeMainCsvPath;
    }

    public String getAlgaeNameCsvFilePath() {
        if (algaeNameCsvPath == null) {
            log.error("algaeNameCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return algaeNameCsvPath;
    }

    public String getVascularPlantsCsvFilePath() {
        if (vascularPlantsCsvFilePath == null) {
            log.error("vascularPlantsCsvFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return vascularPlantsCsvFilePath;
    }

    public String getVascularPlantsBestCsvFilePath() {
        if (vascularPlantsBestCsvPath == null) {
            log.error("vascularPlantsBestCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return vascularPlantsBestCsvPath;
    }

    public String getVascularPlantsCatalogCsvFilePath() {
        if (vascularPlantsCatalogCsvPath == null) {
            log.error("error: vascularPlantsCatalogCsvPath file not found ");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return vascularPlantsCatalogCsvPath;
    }

    public String getVascularPlantsExsickatCsvFilePath() {
        if (vascularPlantsExsickatCsvPath == null) {
            log.error("vascularPlantsExsickatCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return vascularPlantsExsickatCsvPath;
    }

    public String getVascularPlantsMainCsvFilePath() {
        if (vascularPlantsMainCsvPath == null) {
            log.error("vascularPlantsMainCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return vascularPlantsMainCsvPath;
    }

    public String getVascularPlantsNameCsvFilePath() {
        if (vascularPlantsNameCsvPath == null) {
            log.error("vascularPlantsNameCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return vascularPlantsNameCsvPath;
    }

    public String getImageFboCsvPath() {
        if (imageFboCsvPath == null) {
            log.error("imageFboCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return imageFboCsvPath;
    }

    public String getImageKboCsvPath() {
        if (imageKboCsvPath == null) {
            log.error("imageKboCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return imageKboCsvPath;
    }

    public String getImageZooCsvPath() {
        if (imageZooCsvPath == null) {
            log.error("imageZooCsvPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return imageZooCsvPath;
    }

    public String getZooCsvFilePath() {
        if (zooCsvFilePath == null) {
            log.error("zooCsvFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return zooCsvFilePath;
    }

    public String getBaseSolrPath() {
        if (baseSolrUrl == null) {
            log.error("baseSolrUrl is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return baseSolrUrl;
    }

    public String getSolrPath() {
        if (solrPath == null) {
            log.error("solrPath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return solrPath;
    }

    public String getPaleoMappingFilePath() {
        if (paleoMappingfilePath == null) {
            log.error("paleoMappingfilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return paleoMappingfilePath;
    }

    public String getBotMappingFilePath() {
        if (botMappingfilePath == null) {
            log.error("botMappingfilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return botMappingfilePath;
    }

    public String getZooMappingFilePath() {
        if (zooMappingFilePath == null) {
            log.error("zooMappingFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return zooMappingFilePath;
    }

    public String getImageMappingFilePath() {
        if (imageMappingFilePath == null) {
            log.error("imageMappingFilePath is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return imageMappingFilePath;
    }

    public String getCollection() {
        if (collection == null) {
            log.error("collection is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return collection;
    }

    public String getCsvFile(String collection) {
        if (collection.equals(botCollection)) {
            return null;
        } else {
            return collection.equals(zooCollection) ? getZooCsvFilePath()
                    : getPaleoCsvFilePath();
        } 
    }
    
    public String getMapingFilePath(String collection) {
        return collection.equals(botCollection) ? getBotMappingFilePath() :
                collection.equals(paleoCollection) ? getPaleoMappingFilePath() :
                getZooMappingFilePath(); 
    }

    public String getUsername() {
        if (username == null) {
            log.error("username is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return username;
    }

    public String getPassword() {
        if (password == null) {
            log.error("password is null");
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return password;
    }

    public boolean getDeleteCollection() {
        return delectCollection;
    }
    
    public String getPrefix() {
        return prefix;
    }
}
