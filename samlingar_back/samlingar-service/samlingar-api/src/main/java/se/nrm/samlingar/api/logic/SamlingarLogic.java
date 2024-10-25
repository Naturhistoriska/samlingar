package se.nrm.samlingar.api.logic;
 
import ch.hsr.geohash.GeoHash;
import java.io.StringReader; 
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.api.solr.services.SolrService;

/**
 *
 * @author idali
 */
@Slf4j
public class SamlingarLogic {
   
    @Inject
    private SolrService service;
    
    private final String valKey = "val";
    private final String countKey = "count";
    
    private final String geohashKey = "geohash";
    private final String facetsKey = "facets";
    private final String bucketsKey = "buckets";
    
    private final String latitudeKey = "latitude";
    private final String longitudeKey = "longitude";
    private final String geoDataKey = "geoData";
    
    private final String prefix = "4_";
    
    private GeoHash geohash; 
    
    private JsonObjectBuilder attBuilder;
    private JsonArrayBuilder arrayBuilder;
    
    public String getStaticData() {
        log.info("getStaticData");  
        return service.searchStatisticData();
    }
    
    public String simpleSearch(String text, int start, int numPerPage) {
        log.info("simpleSearch : {}", text);
        return service.simpleSearch(start, numPerPage, text);
    }
    
    public String filterSerch(int start, int numPerPage, String text,
            String collection, String typeStatus, String family) {
        log.info("filterSerch : {} -- {}", collection, typeStatus);
        
        return service.filterSearch(start, numPerPage, text, collection, typeStatus, family);
    }
    
    public String mapDataSearch(String text, String collection, 
            String typeStatus, String family) {
        String jsonString = service.mapDataSearch( text, collection, typeStatus, family);
        
        
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonArray array = jsonReader.readObject().getJsonObject(facetsKey)
                .getJsonObject(geohashKey).getJsonArray(bucketsKey); 
        log.info("geo length : {}", array.size());
    
        arrayBuilder = Json.createArrayBuilder();
        attBuilder = Json.createObjectBuilder(); 
        array.getValuesAs(JsonObject.class)
                .stream()
                .forEach(json -> { 
                    String geohashString = json.getString(valKey); 
                    int count = json.getInt(countKey);
                    geohash =GeoHash.fromGeohashString(
                            StringUtils.substringAfter(geohashString, prefix));  
                    
                    double originLat = geohash.getOriginatingPoint().getLatitude();
                    double originLong = geohash.getOriginatingPoint().getLongitude();
                     
                     
                    attBuilder.add(geohashKey, geohashString);
                    attBuilder.add(latitudeKey, originLat);
                    attBuilder.add(longitudeKey, originLong);
                    attBuilder.add(countKey, count);
                    arrayBuilder.add(attBuilder);   
                }); 
        JsonObjectBuilder jsonBuild = Json.createObjectBuilder(); 
        JsonObject json = jsonBuild.add(geoDataKey, arrayBuilder.build()).build(); 
        return json.toString();
    }
}
