package se.nrm.samlingar.api.logic;
  
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import javax.inject.Inject;  
import javax.ws.rs.core.MultivaluedMap;
import lombok.extern.slf4j.Slf4j;   
import se.nrm.samlingar.api.solr.services.Solr; 
import se.nrm.samlingar.api.utils.SolrSearchHelper;

/**
 *
 * @author idali
 */
@Slf4j
public class SamlingarLogic {
 
    @Inject
    private Solr solr;
   
    
    private final String endDateKey = "endDate"; 
    private final String numPerPageKey = "numPerPage"; 
    private final String scientificNameKey = "scientificName";

    
    private final String startKey = "start";
    private final String startDateKey = "startDate"; 
    
    private final String synonymsKey = "synonyms";
     
     
    private final String sortKey = "sort";
       
    private final String contains = "contains";
    
    private final String wildCard = "*";  
    private final String quote = "\"";
    
    private int start = 0;
    private int numPerPage = 20; 
    private String scientificName;
    private String locality;
    private String searchMode;  
    private String sort;
    private String startDate;
    private String endDate; 
    private String dateRange;
    private boolean yearChart;
    private String synonyms;
    private String catchall;
     
    
    private final String catchallKey = "catchall";
    private final String copyScientificNameKey = "copy_scientificName";
    private final String copyCatalogNumberKey = "copy_catalogNumber";
    private final String copyTypeStatusKey = "copy_typeStatus";
    private final String copyLocalityKey = "copy_locality";
    private final String catalogNumberKey = "catalogNumber";
    private final String typeStatusKey = "typeStatus";
    private final String locationKey = "location"; 
    private final String localityKey = "locality";
     
    private final String regex = "([+\\-!(){}\\[\\]^\"~*?:\\\\|&])"; 
    private final String regexReplecement = "\\\\$1";
     
    
    private Map<String, String> paramMap;
 
    private final String startsWith = "startsWith";
    
    private final String searchModeKey = "searchMode";
    private final String modeKey = "mode";
    
    
    private String mode;
     
    
     
    public String getInitalData() {
        log.info("getInitalData");
        return solr.getInitalData();
    }
     
    public String getChart(String collectionCode, String isYearChart) {
         
        yearChart = Boolean.parseBoolean(isYearChart);
        startDate = SolrSearchHelper.getInstance().getStartDate(yearChart);  
        endDate = SolrSearchHelper.getInstance().getEndDate();  

        return solr.getChart(collectionCode, startDate, endDate, yearChart); 
    }
    
    public String autoCompleteSearch(String text, String field) {

        log.info("autoCompleteSearch : {}", field);

        switch (field) {
            case catalogNumberKey:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, copyCatalogNumberKey, false);
                return solr.autoCompleteSearch(text, field); 
            case scientificNameKey:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, copyScientificNameKey, false);
                return solr.autoCompleteSearch(text, copyScientificNameKey);
            case typeStatusKey:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, copyTypeStatusKey, false);
                return solr.autoCompleteSearch(text, field);
            case localityKey:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, copyLocalityKey, false);
                return solr.autoCompleteSearch(text, field);
            default:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, field, true);
                return solr.autoCompleteSearch(text, field);
        }
    }

    public String simpleSearch(MultivaluedMap<String, String> queryParams ) {
        paramMap = new HashMap<>(); 
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            String key = entry.getKey(); 
            paramMap.put(key, entry.getValue().get(0)); 
        } 
        return solr.simpleSearch(paramMap);
    }
    
    public String freeTextSearch(String text, String mode) {
        
        log.info("freeTextSearch : {}", text);  
        
        text = SolrSearchHelper.getInstance().buildFreeTextSearch(text, mode); 
        log.info("text... : {}", text); 
        return solr.freeTextSearch(text);
    }
    
    public String search(MultivaluedMap<String, String> queryParams) {
         
        buildQueryParam(queryParams);
        return solr.search(paramMap, catchall, scientificName, locality, synonyms, 
                dateRange,  start, numPerPage, sort); 
    } 
     
    public String scientificNameSearch(String text, String searchMode, 
            int start, int numPerPage, String sort ) {
        log.info("scientificNameSearch : {}", text);
         
        text = text.replaceAll(regex, regexReplecement); 
        if(text != null) {
            text = SolrSearchHelper.getInstance()
                    .buildScientificName(text, copyScientificNameKey, searchMode); 
            log.info("scientificName : {}", text);
        }
        
        return solr.scientificNameSearch(text);
    }
    
    public String searchWithId(String id) { 
        return solr.searchWithId(id);
    }
 
      
    public String geoJson(MultivaluedMap<String, String> queryParams) { 
        buildQueryParam(queryParams); 
         
        return solr.geojson(paramMap, catchall, scientificName, locality, synonyms, 
                dateRange, start, numPerPage ); 
    } 
    
    public String getHeatmap(MultivaluedMap<String, String> queryParams) {
        buildQueryParam(queryParams); 
        return  solr.getHeatmap(paramMap, catchall, scientificName, locality, synonyms, dateRange );
    }
    
           
    public String export(MultivaluedMap<String, String> queryParams) {
        
        buildQueryParam(queryParams);

        return solr.export(paramMap, catchall, scientificName, locality, dateRange, 
                0, numPerPage, sort); 
            
    }
    
    private void buildQueryParam(MultivaluedMap<String, String> queryParams) {
        paramMap = new HashMap<>();
        
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
             
            switch (entry.getKey()) {
                case scientificNameKey: 
                    scientificName = queryParams.get(scientificNameKey).get(0); 
                    break;
                case searchModeKey: 
                    searchMode = queryParams.get(searchModeKey).get(0); 
                    break;
                case catchallKey: 
                    catchall = queryParams.get(catchallKey).get(0);  
                    break;
                case modeKey: 
                    mode = queryParams.get(modeKey).get(0);  
                    break;
                case synonymsKey: 
                    synonyms = queryParams.get(synonymsKey).get(0);
                    break;    
                case locationKey: 
                    locality = queryParams.get(locationKey).get(0); 
                    break; 
                case startDateKey: 
                    startDate = queryParams.get(startDateKey).get(0); 
                    break;
                case endDateKey: 
                    endDate = queryParams.get(endDateKey).get(0); 
                    break; 
                case startKey:
                    start = Integer.parseInt(queryParams.get(startKey).get(0)); 
                    break;
                case numPerPageKey:
                    numPerPage = Integer.parseInt(queryParams.get(numPerPageKey).get(0));
                    break;   
                case sortKey:
                    sort =  queryParams.get(sortKey).get(0);
                    break; 
                default:
                    paramMap.put(entry.getKey(), entry.getValue().get(0));
                    break;
            }
        } 
        
        if(scientificName != null) {
            scientificName = SolrSearchHelper.getInstance()
                    .buildScientificName(scientificName, copyScientificNameKey, searchMode);
             
            log.info("scientificName : {}", scientificName);
        }
        if(locality != null) {
            log.info("locality 1 : {}", locality);
            if(!locality.contains(quote)) { 
                locality = SolrSearchHelper.getInstance().buildSearchText(
                        locality, copyLocalityKey, startsWith); 
            }

            log.info("locality : {}", locality);
        }
        
        if (synonyms != null) {
            synonyms = SolrSearchHelper.getInstance().buildSynonyms(synonyms);
            log.info("syynonyms : {}", synonyms);
        }
  
        catchall = catchall == null ? wildCard : catchall;
        if(catchall != null && !catchall.equals(wildCard)) {
            catchall  = SolrSearchHelper.getInstance().buildFreeTextSearch(catchall, mode);
//            catchall = SolrSearchHelper.getInstance().buildContainsQuery(catchall);
        }  
        log.info("catchall: ---- {}", catchall);
         
        dateRange = SolrSearchHelper.getInstance().buildDateRange(startDate, endDate); 
    }
    
     
}
