package se.nrm.specify.data.process.logic;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit; 
import javax.inject.Inject;
import javax.json.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.Swarm;
import se.nrm.specify.data.model.impl.Collectionobject;
import se.nrm.specify.data.process.config.InitialProperties;
import se.nrm.specify.data.process.logic.jpa.DataCrud;
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
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private int end;

    private List<Collectionobject> list;
    private List<Integer> ids;
    private List<Integer> fetchIds;

    private JsonArray jsonArray;
    private int status;

    @Inject
    private DataCrud crud;
//    @Inject
//    private JsonConverter convert;

    @Inject
    private InitialProperties propeties;

//    @Inject
//    private SolrClient solr;

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

//        solrUpdateUrl = propeties.getSolrPath();
        jpql = buildJpqlSb();
        try {
            collections.stream()
                    .map(Integer::parseInt)
                    .forEach(collectionCode -> {
                        try {
                            if (isUpdate) {
                                ids = crud.findUpdateIdsByCollectionCode(collectionCode,
                                        updateJpql, fromDate, toDate);
                            } else { 
//                                status = solr.deleteCollection(collectionCode);
                                ids = crud.findIdsByCollectionCode(collectionCode); 
                            }
//                    ids = crud.findIdsByCollectionCode(collectionCode);
                            log.info("code : {} -- {}", collectionCode, ids.size());

//                            int count = ids.size();
//                            for (int i = 0; i < count; i += max) {
//                                end = i + max <= count ? i + max : count;
//                                log.info("start...");
//
//                                fetchIds = ids.size() >= max ? ids.subList(i, end) : ids;
//                                list = crud.getCollectionobjectsByCollectionCode(collectionCode, fetchIds, jpql);
//                                log.info("end.... {}", list.size());
//                                jsonArray = convert.convert(list, collectionCode);
//                                log.info("what in array : {}", jsonArray.size());
//
//                                status = solr.postToSolr(jsonArray.toString().trim());
//                                log.info("status : {}", status);
//                            }
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

    private String buildJpqlSb() {
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
                .append("WHERE c.collectionMemberID = :collectionMemberID ");
        
        
        
        
        
        
        
        
        
//         sb.append("SELECT DISTINCT c FROM Collectionobject c ")
//                .append("LEFT JOIN FETCH c.accession ")
//                .append("LEFT JOIN FETCH c.collectionObjectAttribute ")
//                .append("LEFT JOIN FETCH c.determinationList d ")
//                .append("LEFT JOIN FETCH c.collection ct ")
//                .append("LEFT JOIN FETCH c.collectingEvent ce ")
//                .append("LEFT JOIN FETCH c.preparationList p ")
//                .append("LEFT JOIN FETCH c.collectionobjectattachmentList ca ")
//                .append("LEFT JOIN FETCH ce.locality lc ")
//                .append("LEFT JOIN FETCH ce.collectorList clts ")
//                .append("LEFT JOIN FETCH clts.agent ")
//                .append("LEFT JOIN FETCH lc.geography g ")
//                .append("LEFT JOIN FETCH d.preferredTaxon pt ")
//                .append("LEFT JOIN FETCH pt.commonnametxList cn ")
//                .append("LEFT JOIN FETCH pt.synomys ")
//                .append("LEFT JOIN FETCH d.taxon t ")
//                .append("LEFT JOIN FETCH t.synomys ")
//                .append("LEFT JOIN FETCH t.commonnametxList cn ")
//                .append("LEFT JOIN FETCH d.determiner ")
//                .append("LEFT JOIN FETCH p.storage ")
//                .append("LEFT JOIN FETCH p.prepType ")
//                .append("LEFT JOIN FETCH ca.attachment a ")
//                .append("LEFT JOIN FETCH a.attachmentImageAttribute ai ")
//                .append("LEFT JOIN FETCH ai.morphBankWiew ")
//                .append("where c.collectionMemberID = :collectionMemberID ")
//                .append("AND c.collectionObjectID in :ids ")
//                .append("ORDER BY c.collectionObjectID");
        return sb.toString().trim();

    }

    private String buildGetUpdateIdsQuery() {
        log.info("buildGetUpdateIdsQuery");
        sb = new StringBuilder();
//        sb.append("SELECT c.collectionObjectID FROM Collectionobject c "); 
//        sb.append("WHERE c.collectionMemberID = :collectionMemberID ");
        sb.append(buildJpqlSb()); 
        sb.append("AND c.timestampModified BETWEEN :fromDate AND :toDate ");
        sb.append("ORDER BY c.collectionObjectID");
        return sb.toString();
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
