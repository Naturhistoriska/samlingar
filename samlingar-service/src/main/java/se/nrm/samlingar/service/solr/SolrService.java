package se.nrm.samlingar.service.solr;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.json.JsonQueryRequest;
import org.apache.solr.client.solrj.request.json.TermsFacetMap;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class SolrService {

    private static final Logger LOG = Logger.getLogger(SolrService.class);

    @ConfigProperty(name = "samlingar.solr.url")
    private String solrURL;
    @ConfigProperty(name = "samlingar.solr.core")
    private String solrCore;
    @ConfigProperty(name = "samlingar.solr.username")
    private String username;
    @ConfigProperty(name = "samlingar.solr.password")
    private String password;

    private SolrClient client;

    private SolrQuery query;
    private QueryRequest request;
    private QueryResponse response;

    private final String colon = ":";

    private final String star = "*";

    private final String idKey = "id";
    private final String idFieldKey = "id:";
    private final String scientificNameKey = "scientificName";
    private final String localityKey = "locality";

    private final String facetKey = "facet";
    private final String facetHeatMapKey = "facet.heatmap";
    private final String facetHeatmapGeomKey = "facet.heatmap.geom";
    private final String facetHeatmepGridLevelKey = "facet.heatmap.gridLevel";

    private final String strTrue = "true";

    private final String envelope = "ENVELOPE(-180,180,90,-90)";
    private final String gridLevel = "3";

    private final String yearGap = "+1YEAR";
    private final String monthGap = "+1MONTH";

    private final String bbox = "{!bbox}";
    private final String sfield = "sfield";

    private final String radiusKm = "10000";
    private final String ptKey = "pt";
    private final String dKey = "d";

    private final String idSort = "id asc";

    private final String responseKey = "response";

    private final String cursorMarkKey = "cursorMark";
    
    private final String defType = "defType";
    private final String edismax = "edismax"; 
    private final String qf = "qf";
    private final String mmKey = "mm";
    private final String mmValue = "2<75%";
    private final String catchall = "catchall";
    private final String copyScientificNameKey = "copy_scientificName";
       
    private final int maxExport = 20000;
    private int batchSize = 1000;

    private StringBuilder sb;
    private String gap;

    private List<String> fields;
   
    private final String searchAll = "*:*";

    private final String associatedMediaKey = "associatedMedia";
    private final String collectionCodeKey = "collectionCode";
    private final String collectionNameKey = "collectionName";

    private final String defaultSort = "catalogedDate desc";
    private final int defaultStart = 0;
    private final int defaultRows = 20;
    
    private final String catalogedDateKey = "catalogedDate";

    private final String geoKey = "geo";
    private final String inSwedenKey = "inSweden";
    private final String coordinatesKey = "coordinates";
    private final String typeStatusKey = "typeStatus";
    
    private final String pt ="59,15";

    final TermsFacetMap collectionFacet;
    final TermsFacetMap collectionCodeFacet;
    
    final Map<String, Object> facetCoordinates; 
    final Map<String, Object> facetImages; 
    final Map<String, Object> facetTypeStatus; 
    final Map<String, Object> facetInSweden; 
    final Map<String, Object> facetCollectionName; 
    final Map<String, Object> facetCollectionCode; 
    
    private final String queryKey = "query";
    private final String geoQuery = "geo:*";
    private final String imagesQuery = "hasImage:*";
    private final String typeStatusQuery = "typeStatus:*";
    private final String inSwedenQuery = "country:Sweden*";
    private final String collectionNameQuery = "collectionName:*";
    private final String collectionCodeQuery = "collectionCode:*";

    public SolrService() {
        collectionFacet = new TermsFacetMap(collectionNameKey);
        
        collectionCodeFacet = new TermsFacetMap(collectionCodeKey); 
         
        facetCoordinates = new HashMap<>();
        facetImages = new HashMap<>();
        facetTypeStatus = new HashMap<>();
        facetInSweden = new HashMap<>();
        facetCollectionName = new HashMap<>();
        facetCollectionCode = new HashMap<>();

        facetCoordinates.put(queryKey, geoQuery);
        facetImages.put(queryKey, imagesQuery);
        facetTypeStatus.put(queryKey, typeStatusQuery);
        facetInSweden.put(queryKey, inSwedenQuery);
         
        facetCollectionName.put(queryKey, collectionNameQuery);
        facetCollectionCode.put(queryKey, collectionCodeQuery); 
    }

    @PostConstruct
    public void init() {
        LOG.info("init from search...");
        client = new HttpJettySolrClient.Builder(solrURL + "/" + solrCore).build();
    }

    public String getInitalData() {
        LOG.info("getInitalData");
         
        final JsonQueryRequest jsonRequest = new JsonQueryRequest()
                .setQuery(searchAll)   
                .withFacet(coordinatesKey, facetCoordinates)
                .withFacet(associatedMediaKey, facetImages)
                .withFacet(typeStatusKey, facetTypeStatus)
                .withFacet(inSwedenKey, facetInSweden)
                .withFacet(collectionNameKey, collectionFacet.setLimit(30))
                .setLimit(1);
         
        jsonRequest.setBasicAuthCredentials(username, password); 
        try {  
            response = jsonRequest.process(client);
        } catch (SolrServerException | IOException ex) {
            LOG.error(ex.getMessage());
            return null;
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                LOG.error(ex.getMessage());
            }
        } 
        return response.jsonStr();
    }
}
