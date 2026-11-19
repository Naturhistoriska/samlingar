package se.nrm.samlingar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MultivaluedMap;
import se.nrm.samlingar.service.solr.QueryParams;
import se.nrm.samlingar.service.solr.SolrSearchHelper;
import se.nrm.samlingar.service.solr.SolrService;

@ApplicationScoped
public class SamlingarService {
    private static final Logger LOG = Logger.getLogger(SamlingarService.class);

    private static final String SCIENTIFIC_NAME_KEY = "scientificName";
    private static final String CATALOG_NUMBER_KEY = "catalogNumber";
    private static final String TYPE_STATUS_KEY = "typeStatus";
    private static final String LOCALITY_KEY = "locality";
    private static final String COPY_SCIENTIFIC_NAME_KEY = "copy_scientificName";
    private static final String COPY_CATALOG_NUMBER_KEY = "copy_catalogNumber";
    private static final String COPY_TYPE_STATUS_KEY = "copy_typeStatus";
    private static final String COPY_LOCALITY_KEY = "copy_locality";

    private static final String REGEX = "([+\\-!(){}\\[\\]^\"~*?:\\\\|&])"; 
    private static final String REGEX_REPLACEMENT = "\\\\$1";

    private final SolrService solr;

    public SamlingarService(SolrService solr) {
        LOG.info("construct");
        this.solr = solr;
    }

    public String initalData() {
        LOG.info("initalData");
        return solr.initalData();
    }

    public String simpleSearch(MultivaluedMap<String, String> queryParams) {
        Map<String, String> paramMap = new HashMap<>(); 
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            String key = entry.getKey(); 
            paramMap.put(key, entry.getValue().get(0)); 
        } 
        return solr.simpleSearch(paramMap);
    }

    public String search(MultivaluedMap<String, String> queryParams) {
        QueryParams solrQueryParams = QueryParams.build(queryParams);
        return solr.search(solrQueryParams);
    }

    public String freeTextSearch(String text, String mode) {        
        LOG.info(String.format("freeTextSearch: %s", text));
        text = SolrSearchHelper.getInstance().buildFreeTextSearch(text, mode); 
        LOG.info(String.format("text... : %s", text)); 
        return solr.freeTextSearch(text);
    }

    public String autoCompleteSearch(String text, String field) {
        LOG.info(String.format("autoCompleteSearch : %s", field));

        switch (field) {
            case CATALOG_NUMBER_KEY:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, COPY_CATALOG_NUMBER_KEY, false);
                return solr.autoCompleteSearch(text, field); 
            case SCIENTIFIC_NAME_KEY:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, COPY_SCIENTIFIC_NAME_KEY, false);
                return solr.autoCompleteSearch(text, COPY_SCIENTIFIC_NAME_KEY);
            case TYPE_STATUS_KEY:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, COPY_TYPE_STATUS_KEY, false);
                return solr.autoCompleteSearch(text, field);
            case LOCALITY_KEY:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, COPY_LOCALITY_KEY, false);
                return solr.autoCompleteSearch(text, field);
            default:
                text = SolrSearchHelper.getInstance()
                        .buildAutoCompleteSearchText(text, field, true);
                return solr.autoCompleteSearch(text, field);
        }
    }

    public String scientificNameSearch(String text, String searchMode, 
            int start, int numPerPage, String sort ) {
        LOG.info(String.format("scientificNameSearch: %s", text));
         
        text = text.replaceAll(REGEX, REGEX_REPLACEMENT); 
        if (text != null) {
            text = SolrSearchHelper.getInstance()
                    .buildScientificName(text, COPY_SCIENTIFIC_NAME_KEY, searchMode); 
            LOG.info(String.format("scientificName: %s", text));
        }

        return solr.scientificNameSearch(text);
    }

    public String geojson(MultivaluedMap<String, String> queryParams) { 
        QueryParams solrQueryParams = QueryParams.build(queryParams);
        return solr.geojson(solrQueryParams); 
    }

    public String heatmap(MultivaluedMap<String, String> queryParams) {
        QueryParams solrQueryParams = QueryParams.build(queryParams);
        return solr.heatmap(solrQueryParams);
    }

    public String id(String id) { 
        return solr.id(id);
    }

    public String download(MultivaluedMap<String, String> queryParams) {
        QueryParams solrQueryParams = QueryParams.build(queryParams);
        return solr.download(solrQueryParams);
    }

    public String chart(String collectionCode, String isYearChart) {
        boolean yearChart = Boolean.parseBoolean(isYearChart);
        String startDate = SolrSearchHelper.getInstance().getStartDate(yearChart);  
        String endDate = SolrSearchHelper.getInstance().getEndDate();  
        return solr.chart(collectionCode, startDate, endDate, yearChart); 
    }
}
