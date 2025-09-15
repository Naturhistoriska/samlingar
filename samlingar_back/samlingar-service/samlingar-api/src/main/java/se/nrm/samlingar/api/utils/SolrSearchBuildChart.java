package se.nrm.samlingar.api.utils;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author idali
 */
public class SolrSearchBuildChart {

    private final String totalKey = "total";
    
    private static SolrSearchBuildChart instance = null;

    public static SolrSearchBuildChart getInstance() {
        synchronized (SolrSearchBuildChart.class) {
            if (instance == null) {
                instance = new SolrSearchBuildChart();
            }
        }
        return instance;
    }
    
        
    public String buildChatResult(QueryResponse response) {
        SolrDocumentList docs = response.getResults();

        JsonReader reader = Json.createReader(new StringReader(response.jsonStr()));
        JsonObject jsonObject = reader.readObject();

        JsonObjectBuilder builder = Json.createObjectBuilder();

        for (String key : jsonObject.keySet()) {
            builder.add(key, jsonObject.get(key));
        }
        builder.add(totalKey, docs.getNumFound());
        return builder.build().toString();
    }

}
