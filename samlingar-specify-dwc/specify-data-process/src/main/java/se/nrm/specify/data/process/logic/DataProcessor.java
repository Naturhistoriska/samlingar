package se.nrm.specify.data.process.logic;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;  
import java.util.List; 
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.json.JsonArray; 
import lombok.extern.slf4j.Slf4j; 
import org.wildfly.swarm.Swarm; 
import se.nrm.specify.data.model.impl.Collection;
import se.nrm.specify.data.model.impl.Collectionobject;
import se.nrm.specify.data.model.impl.Institution;
import se.nrm.specify.data.process.config.InitialProperties;
import se.nrm.specify.data.process.logic.jpa.DataCrud;
import se.nrm.specify.data.process.logic.json.JsonConverter;
import se.nrm.specify.data.process.logic.solr.SolrService;

/**
 *
 * @author idali
 */
@Slf4j
public class DataProcessor implements Serializable {
 
    private final int max = 2000;
    private final int daysToSubtract = 5;
    private final int daysToAdd = 1;
  
    private int count;
    private int end;
    private int statusCode;


    private List<Integer> ids;
    private List<Integer> fetchIds;
  
    private List<String> collections;

    private boolean isUpdate;

    private final String slash = "/"; 
    
    private StringBuilder sb;
    private String jpql;
//    private String updateJpql;
 
    private LocalDate today;
    private Date fromDate;
    private Date toDate;
    


    private List<Collectionobject> entities;
    private Institution institution;
    private Collection collection;

    private JsonArray jsonArray;
    
    @Inject
    private DataCrud crud;
  
    @Inject
    private JsonConverter converter;

    @Inject
    private InitialProperties propeties;

    @Inject
    private SolrService solr;

    public DataProcessor() {
    }

    public void run() {
        log.info("run....");

        collections = Arrays.asList(propeties.getCollections().split(slash));
        log.info("collections.... {}", collections);
        
        
        institution = crud.getInstitutionByCode(propeties.getInstitutionCode());
     
        isUpdate = propeties.isUpdate();
        if (isUpdate) {
//            updateJpql = buildGetUpdateIdsQuery(); 
            
            today = LocalDate.now();
            fromDate = Date.valueOf(today.minusDays(daysToSubtract));
            toDate = Date.valueOf(today.plusDays(daysToAdd));
              
            log.info("dates : {} -- {}", fromDate, toDate);
        }
        jpql = buildJpql();  
        try {
            collections.stream()
                    .forEach(collectionCode -> {
                        try {
                            collection = crud.getCollectionByCode(collectionCode);
                            
                            if (isUpdate) {
                                ids = crud.findUpdateIdsByCollectionCode(collectionCode,
                                      fromDate, toDate);
                            } else { 
                                solr.deleteCollection(collectionCode);
                                ids = crud.findIdsByCollectionCode(collectionCode); 
                            } 
                            log.info("ids size : {}", ids.size());

                            count = ids.size(); 
                            for (int i = 0; i < count; i += max) {
                                end = i + max <= count ? i + max : count;

                                fetchIds = count >= max ? ids.subList(i, end) : ids;
                                log.info("start: {} --- end: {}", i, end);
                                entities = crud.fetchDataByIds(collectionCode, fetchIds, jpql);
                     
                                jsonArray = converter.convert(entities, institution, collection);

                                statusCode = solr.postToSolr(jsonArray.toString().trim());
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
 
    private String buildBaseJpql() {
        sb = new StringBuilder();
        sb.append("SELECT DISTINCT c FROM Collectionobject c ")
                .append("LEFT JOIN FETCH c.accession ") 
                .append("LEFT JOIN FETCH c.collectionObjectAttribute ")
                .append("LEFT JOIN FETCH c.determinationList d ")
//                .append("LEFT JOIN FETCH c.collection ct ")
                .append("LEFT JOIN FETCH c.collectingEvent ce ")
                .append("LEFT JOIN FETCH c.preparationList p ")
                .append("LEFT JOIN FETCH c.collectionobjectattachmentList ca ")
                .append("LEFT JOIN FETCH ce.locality lc ") 
                .append("LEFT JOIN FETCH lc.localitydetail1Collection ld ") 
                .append("LEFT JOIN FETCH ce.collectorList clts ")
                .append("LEFT JOIN FETCH clts.agent ")
                .append("LEFT JOIN FETCH lc.geography g ")
                .append("LEFT JOIN FETCH d.preferredTaxon pt ")
                .append("LEFT JOIN FETCH pt.commonnametxList cn ")
                .append("LEFT JOIN FETCH pt.synomys ")
                .append("LEFT JOIN FETCH pt.taxonTreeDefItem ")
                .append("LEFT JOIN FETCH d.taxon t ")
                .append("LEFT JOIN FETCH t.taxonTreeDefItem ")
                .append("LEFT JOIN FETCH t.synomys ")
                .append("LEFT JOIN FETCH t.commonnametxList cn ")
                .append("LEFT JOIN FETCH d.determiner ") 
                .append("LEFT JOIN FETCH p.prepType ")
                .append("LEFT JOIN FETCH ca.attachment a ")
                .append("LEFT JOIN FETCH a.attachmentImageAttribute ai ")
                .append("LEFT JOIN FETCH ai.morphBankWiew ")
                .append("where c.collectionObjectID in :ids "); 
        return sb.toString().trim();
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
        sb.append("LEFT JOIN FETCH c.collection ct ");
        sb.append("WHERE ct.code = :code ");
        sb.append("AND c.timestampModified BETWEEN :fromDate AND :toDate ");
        sb.append("ORDER BY c.collectionObjectID");
        return sb.toString().trim();
    }
    
    
//    private String buildJpqlWithFilters(Map<String, String> filterMap) {
//        buildBaseJpql1();
//        filterMap.keySet()
//                .stream()
//                .forEach(key -> {
//                    sb.append("AND (");
//                    sb.append(key);
//                    sb.append(" <> :");
//                    sb.append(StringUtils.substringAfterLast(key, "."));
//                    sb.append(" OR ");
//                    sb.append(key);
//                    sb.append(" is Null) ");
//                });
//        sb.append("ORDER BY c.collectionObjectID");
//
//        return sb.toString();
//    }

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
