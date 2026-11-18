package se.nrm.samlingar.service.solr;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.request.json.TermsFacetMap;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SolrService {
    private static final Logger LOG = Logger.getLogger(SolrService.class);

    private static final String ID_KEY = "id";
    private static final String ID_FIELD_KEY = "id:";
    private static final String SCIENTIFIC_NAME_KEY = "scientificName";
    private static final String LOCALITY_KEY = "locality";

    private static final String FACET_KEY = "facet";
    private static final String FACET_HEAT_MAP_KEY = "facet.heatmap";
    private static final String FACET_HEATMAP_GEOM_KEY = "facet.heatmap.geom";
    private static final String FACET_HEATMAP_GRID_LEVEL_KEY = "facet.heatmap.gridLevel";

    private static final String ENVELOPE = "ENVELOPE(-180,180,90,-90)";
    private static final String GRID_LEVEL = "3";

    private static final String YEAR_GAP = "+1YEAR";
    private static final String MONTH_GAP = "+1MONTH";

    private static final String BBOX = "{!bbox}";
    private static final String SFIELD = "sfield";

    private static final String RADIUS_KM = "10000";
    private static final String PT_KEY = "pt";
    private static final String D_KEY = "d";

    private static final String ID_SORT = "id asc";

    private static final String RESPONSE_KEY = "response";

    private static final String CURSOR_MARK_KEY = "cursorMark";
    
    private static final String DEF_TYPE = "defType";
    private static final String EDISMAX = "edismax"; 
    private static final String QF = "qf";
    private static final String MM_KEY = "mm";
    private static final String MM_VALUE = "2<75%";
    private static final String CATCHALL = "catchall";
    private static final String COPY_SCIENTIFIC_NAME_KEY = "copy_scientificName";
       
    private static final int MAX_EXPORT = 20000;
    private static final int BATCH_SIZE = 1000;

    private static final String SEARCH_ALL = "*:*";

    private static final String ASSOCIATED_MEDIA_KEY = "associatedMedia";
    private static final String COLLECTION_CODE_KEY = "collectionCode";
    private static final String COLLECTION_NAME_KEY = "collectionName";

    private static final String DEFAULT_SORT = "catalogedDate desc";
    private static final int DEFAULT_START = 0;
    private static final int DEFAULT_ROWS = 20;
    
    private static final String CATALOGED_DATE_KEY = "catalogedDate";

    private static final String GEO_KEY = "geo";
    private static final String IN_SWEDEN_KEY = "inSweden";
    private static final String COORDINATES_KEY = "coordinates";
    private static final String TYPE_STATUS_KEY = "typeStatus";
    
    private static final String PT = "59,15";

    private static final String QUERY_KEY = "query";
    private static final String GEO_QUERY = "geo:*";
    private static final String IMAGES_QUERY = "hasImage:*";
    private static final String TYPE_STATUS_QUERY = "typeStatus:*";
    private static final String IN_SWEDEN_QUERY = "country:Sweden*";
    private static final String COLLECTION_NAME_QUERY = "collectionName:*";
    private static final String COLLECTION_CODE_QUERY = "collectionCode:*";

    private static final Map<String, Object> FACET_COORDINATES = Map.of(QUERY_KEY, GEO_QUERY); 
    private static final Map<String, Object> FACET_IMAGES = Map.of(QUERY_KEY, IMAGES_QUERY); 
    private static final Map<String, Object> FACET_TYPE_STATUS = Map.of(QUERY_KEY, TYPE_STATUS_QUERY); 
    private static final Map<String, Object> FACET_IN_SWEDEN = Map.of(QUERY_KEY, IN_SWEDEN_QUERY); 
    private static final Map<String, Object> FACET_COLLECTION_NAME = Map.of(QUERY_KEY, COLLECTION_NAME_QUERY); 
    private static final Map<String, Object> FACET_COLLECTION_CODE = Map.of(QUERY_KEY, COLLECTION_CODE_QUERY); 

    private final SolrClient client;

    public SolrService(SolrClient client) {        
        LOG.info("construct");
        this.client = client;
    }

    private TermsFacetMap collectionFacet() {
        return new TermsFacetMap(COLLECTION_NAME_KEY);
    }

    private TermsFacetMap collectionCodeFacet() {
        return new TermsFacetMap(COLLECTION_CODE_KEY);
    }

    public String initalData() {
        LOG.info("initalData");
        LOG.info(this.client.toString());
         
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(SEARCH_ALL)   
                .withFacet(COORDINATES_KEY, FACET_COORDINATES)
                .withFacet(ASSOCIATED_MEDIA_KEY, FACET_IMAGES)
                .withFacet(TYPE_STATUS_KEY, FACET_TYPE_STATUS)
                .withFacet(IN_SWEDEN_KEY, FACET_IN_SWEDEN)
                .withFacet(COLLECTION_NAME_KEY, collectionFacet().setLimit(30))
                .setLimit(1);
         
        try {  
            return jsonRequest.process(this.client).jsonStr();
        } catch (SolrServerException | IOException e) {
            throw new SolrAccessException(e);
        }
    }

    public String simpleSearch(Map<String, String> paramMap) { 
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(SEARCH_ALL)
                .setOffset(DEFAULT_START)
                .setLimit(DEFAULT_ROWS)
                .setSort(DEFAULT_SORT)
                .withFacet(COORDINATES_KEY, FACET_COORDINATES) 
                .withFacet(COLLECTION_CODE_KEY, collectionCodeFacet().setLimit(30))
                .withFacet(COLLECTION_NAME_KEY, collectionFacet().setLimit(30));
 
        paramMap.forEach((key, value) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(key)
                    .append(":")
                    .append(value);
            jsonRequest.withFilter(sb.toString());
        });

        try {
            return jsonRequest.process(client).jsonStr();
        } catch (SolrServerException | IOException e) {
            throw new SolrAccessException(e);
        }
    }
}
