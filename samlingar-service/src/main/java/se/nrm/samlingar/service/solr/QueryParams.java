package se.nrm.samlingar.service.solr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import jakarta.ws.rs.core.MultivaluedMap;

public class QueryParams {
    private static final Logger LOG = Logger.getLogger(QueryParams.class);

    private final String END_DATE_KEY = "endDate"; 
    private final String NUM_PER_PAGE_KEY = "numPerPage"; 
    private final String SCIENTIFIC_NAME_KEY = "scientificName";
    private final String START_KEY = "start";
    private final String START_DATE_KEY = "startDate"; 
    private final String SYNONYMS_KEY = "synonyms";
    private final String SORT_KEY = "sort";
    private final String CATCHALL_KEY = "catchall";
    private final String COPY_SCIENTIFIC_NAME_KEY = "copy_scientificName";
    private final String COPY_LOCALITY_KEY = "copy_locality";
    private final String LOCATION_KEY = "location"; 
    private final String SEARCH_MODE_KEY = "searchMode";
    private final String MODE_KEY = "mode";
    private final String STARTS_WITH = "startsWith";

    public int start = 0;
    public int numPerPage = 20; 
    public String sort;
    public Map<String, String> paramMap;
    public String scientificName;
    public String locality;
    public String searchMode;  
    public String startDate;
    public String endDate; 
    public String dateRange;
    public String synonyms;
    public String catchall;
    public String mode;

    public static QueryParams build(MultivaluedMap<String, String> queryParams) {
        return new QueryParams(queryParams);
    }

    private QueryParams(MultivaluedMap<String, String> queryParams) {
        this.paramMap = new HashMap<>();
        
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
             
            switch (entry.getKey()) {
                case SCIENTIFIC_NAME_KEY: 
                    this.scientificName = queryParams.get(SCIENTIFIC_NAME_KEY).get(0); 
                    break;
                case SEARCH_MODE_KEY: 
                    this.searchMode = queryParams.get(SEARCH_MODE_KEY).get(0); 
                    break;
                case CATCHALL_KEY: 
                    this.catchall = queryParams.get(CATCHALL_KEY).get(0);  
                    break;
                case MODE_KEY: 
                    this.mode = queryParams.get(MODE_KEY).get(0);  
                    break;
                case SYNONYMS_KEY: 
                    this.synonyms = queryParams.get(SYNONYMS_KEY).get(0);
                    break;    
                case LOCATION_KEY: 
                    this.locality = queryParams.get(LOCATION_KEY).get(0); 
                    break; 
                case START_DATE_KEY: 
                    this.startDate = queryParams.get(START_DATE_KEY).get(0); 
                    break;
                case END_DATE_KEY: 
                    this.endDate = queryParams.get(END_DATE_KEY).get(0); 
                    break; 
                case START_KEY:
                    this.start = Integer.parseInt(queryParams.get(START_KEY).get(0)); 
                    break;
                case NUM_PER_PAGE_KEY:
                    this.numPerPage = Integer.parseInt(queryParams.get(NUM_PER_PAGE_KEY).get(0));
                    break;   
                case SORT_KEY:
                    this.sort = queryParams.get(SORT_KEY).get(0);
                    break; 
                default:
                    this.paramMap.put(entry.getKey(), entry.getValue().get(0));
                    break;
            }
        } 
        
        if (this.scientificName != null) {
            this.scientificName = SolrSearchHelper.getInstance()
                    .buildScientificName(this.scientificName, COPY_SCIENTIFIC_NAME_KEY, searchMode);
             
            LOG.info(String.format("scientificName : %s", scientificName));
        }
        if (this.locality != null) {
            LOG.info(String.format("locality 1 : %s", this.locality));
            if(!this.locality.contains("\"")) { 
                this.locality = SolrSearchHelper.getInstance().buildSearchText(
                        this.locality, COPY_LOCALITY_KEY, STARTS_WITH); 
            }

            LOG.info(String.format("locality : %s", this.locality));
        }
        
        if (this.synonyms != null) {
            this.synonyms = SolrSearchHelper.getInstance().buildSynonyms(this.synonyms);
            LOG.info(String.format("synonyms : %s", this.synonyms));
        }
  
        this.catchall = this.catchall == null ? "*" : this.catchall;
        if (this.catchall != null && !this.catchall.equals("*")) {
            this.catchall = SolrSearchHelper.getInstance().buildFreeTextSearch(this.catchall, this.mode);
        }  
        LOG.info(String.format("catchall: ---- %s", this.catchall));
         
        this.dateRange = SolrSearchHelper.getInstance().buildDateRange(this.startDate, this.endDate); 
    }    
}
