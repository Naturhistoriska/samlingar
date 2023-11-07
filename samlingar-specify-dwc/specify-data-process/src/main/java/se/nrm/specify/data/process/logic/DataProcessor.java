package se.nrm.specify.data.process.logic;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.wildfly.swarm.Swarm;
import se.nrm.specify.data.model.EntityBean;
import se.nrm.specify.data.model.impl.Collectionobject;
import se.nrm.specify.data.process.config.InitialProperties;
import se.nrm.specify.data.process.logic.jpa.DataCrud;
import se.nrm.specify.data.process.logic.json.JsonConverter;
import se.nrm.specify.data.process.logic.solr.SolrService;
//import se.nrm.specify.data.process.logic.json.JsonConverter;
//import se.nrm.specify.data.process.logic.solr.SolrClient;

/**
 *
 * @author idali
 */
@Slf4j
public class DataProcessor implements Serializable {

//    private final List<Integer> allCollectionCodes;
    private final int max = 2000;
//    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final String filterKey = "filters";

    private int end;
 
    private List<Integer> ids;
    private List<Integer> fetchIds;

    private JsonArray entityJsonArray;
    private int statusCode;

    @Inject
    private DataCrud crud;

    @Inject
    private MappingFileReader mappingFile;

    @Inject
    private JsonConverter converter;
    
    @Inject
    private InitialProperties propeties;

    @Inject
    private SolrService solr;

    private List<String> collections;

    private boolean isUpdate;

    private final String slash = "/";
    private StringBuilder sb;
    private String jpql;
    private String updateJpql;

    private LocalDate tomorrow;
    private LocalDate yesterday;
    private LocalDate today;
    private Date fromDate;
    private Date toDate;

    public DataProcessor() {
    }

    public void run() {
        log.info("run");

        collections = Arrays.asList(propeties.getCollections().split(slash));
        isUpdate = propeties.isUpdate();

        if (isUpdate) {
            updateJpql = buildGetUpdateIdsQuery();

            today = LocalDate.now();
            yesterday = today.minusDays(3);
            tomorrow = today.plusDays(1);

            fromDate = java.sql.Date.valueOf(yesterday);
            toDate = java.sql.Date.valueOf(tomorrow);

            log.info("dates : {} -- {}", fromDate, toDate);
        }
 
        try {
            collections.stream()
                    .map(Integer::parseInt)
                    .forEach(collectionCode -> {
                        try {
                            if (isUpdate) {
                                ids = crud.findUpdateIdsByCollectionCode(collectionCode,
                                        updateJpql, fromDate, toDate);
                            } else {
                                statusCode = solr.deleteCollection(collectionCode);
                                ids = crud.findIdsByCollectionCode(collectionCode);
                                log.info("delete status : {}", statusCode); 
                            }

                            log.info("code : {} -- {}", collectionCode, ids.size());

                            Map<String, String> filterMap = new HashMap();
                            JsonObject mappingJson = mappingFile.read(collectionCode.toString());

                            if (mappingJson.containsKey(filterKey)) {
                                JsonObject filterJson = mappingJson.getJsonObject(filterKey);
                                filterJson.keySet()
                                        .stream()
                                        .forEach(key -> {
                                            filterMap.put(key, filterJson.getString(key));
                                        });
                                jpql = buildJpqlWithFilters(filterMap);
                            } else {
                                jpql = buildJpql();
                            }

                            int count = ids.size();
                            for (int i = 0; i < count; i += max) {
                                end = i + max <= count ? i + max : count; 

                                fetchIds = ids.size() >= max ? ids.subList(i, end) : ids;
                                log.info("start: {} --- end: {}", i, end);
                                List<EntityBean> entities = crud.fetchData(collectionCode, fetchIds,
                                        filterMap, jpql);
//                                log.info("entities size : {}", entities.size());
                                entityJsonArray = converter.convert(entities, mappingJson);
                                 
                                log.info("entity jaonArray : {}", entityJsonArray.size());
                                
                                statusCode = solr.postToSolr(entityJsonArray.toString().trim());
//                  
                                log.info("status : {}", statusCode);
                            }
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                            stopServer();
                        }
                    });
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        stopServer();
    }

    private String buildJpqlWithFilters(Map<String, String> filterMap) {
        buildBaseJpql();
        filterMap.keySet()
                .stream()
                .forEach(key -> {
                    sb.append("AND (");
                    sb.append(key);
                    sb.append(" <> :");
                    sb.append(StringUtils.substringAfterLast(key, "."));
                    sb.append(" OR ");
                    sb.append(key);
                    sb.append(" is Null) ");
                });
        sb.append("ORDER BY c.collectionObjectID");

        return sb.toString();
    }
    
    private void buildBaseJpql() {
        sb = new StringBuilder();
        sb.append("SELECT DISTINCT c FROM Collectionobject c ")
                .append("LEFT JOIN FETCH c.collectionObjectAttribute ")
                .append("LEFT JOIN FETCH c.determinations d ")
                .append("LEFT JOIN FETCH c.collection ct ")
                .append("LEFT JOIN FETCH ct.discipline dsc ")
                .append("LEFT JOIN FETCH dsc.division div ")
                .append("LEFT JOIN FETCH div.institution inst ")
                .append("LEFT JOIN FETCH c.collectingEvent ce ")
                .append("LEFT JOIN FETCH c.preparations p ")
                .append("LEFT JOIN FETCH ce.locality lc ")
                .append("LEFT JOIN FETCH ce.collectors clts ")
                .append("LEFT JOIN FETCH clts.agent ")
                .append("LEFT JOIN FETCH ce.collectingEventAttribute cea ")
                .append("LEFT JOIN FETCH lc.geography g ")
                .append("LEFT JOIN FETCH lc.localitydetails ld ")
                .append("LEFT JOIN FETCH lc.geocoorddetails gd ")
                .append("LEFT JOIN FETCH gd.agent gdagent ")
                .append("LEFT JOIN FETCH d.preferredTaxon pt ")
                .append("LEFT JOIN FETCH pt.commonnametxs cn ")
                .append("LEFT JOIN FETCH pt.taxonTreeDefItem tdi ")
                .append("LEFT JOIN FETCH d.determiner ")
                .append("LEFT JOIN FETCH p.prepType ")
                .append("LEFT JOIN FETCH p.storage s ")
                .append("WHERE c.collectionMemberID = :collectionMemberID ")
                .append("AND c.collectionObjectID in :ids "); 
    }

    private String buildJpql() {
        buildBaseJpql(); 
        sb.append("ORDER BY c.collectionObjectID"); 
        return sb.toString().trim();

    }

    private String buildGetUpdateIdsQuery() {
        log.info("buildGetUpdateIdsQuery");
        sb = new StringBuilder();
        sb.append("SELECT c.collectionObjectID FROM Collectionobject c ");
        sb.append("WHERE c.collectionMemberID = :collectionMemberID ");
        sb.append("AND c.timestampModified BETWEEN :fromDate AND :toDate ");
        sb.append("ORDER BY c.collectionObjectID");
        return sb.toString().trim();
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
