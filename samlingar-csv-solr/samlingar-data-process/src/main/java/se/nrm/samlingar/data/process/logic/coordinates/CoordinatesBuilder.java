package se.nrm.samlingar.data.process.logic.coordinates;

import ch.hsr.geohash.GeoHash; 
import java.io.Serializable; 
import javax.inject.Inject;
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j; 
import se.nrm.samlingar.data.process.logic.exception.SamlingarException;
import se.nrm.samlingar.data.process.logic.json.JsonHelper; 

/**
 *
 * @author idali
 */
@Slf4j
public class CoordinatesBuilder implements Serializable {

    private final int numberOfCharacters = 5; 
     
    private String geoHash; 
    private double dblLat;
    private double dblLong; 
    
    @Inject 
    private CoordinatesConverter convert;

    public CoordinatesBuilder() {

    }

    public void build(JsonObjectBuilder attBuilder, String latitude, String longitude) {
        log.info("build: {} -- {}", latitude, longitude);
        
        try { 
            dblLat = convert.convert(latitude);
            dblLong = convert.convert(longitude);
            log.info("latitude and longigude: {} -- {}", dblLat, dblLong);
             
            addGeoData(attBuilder, dblLat, dblLong); 
        } catch(SamlingarException ex) { 
//            log.error("SamlingarException: builderCoordinates : {}", ex.getErrorMessage());
        } catch (Exception ex) {
            log.error("builderCoordinates : {}", ex.getMessage());
        }
    }
 
    private void addGeoData(JsonObjectBuilder attBuilder, double latitude,
            double longitude) throws Exception {
//        log.info("addGeoData : {} -- {}", latitude, longitude);
         
        geoHash = createGeoHash(latitude, longitude); 
//         
        JsonHelper.getInstance().addCoordinates(attBuilder, latitude, longitude); 
        JsonHelper.getInstance().addGeoHash(attBuilder, geoHash);  
        JsonHelper.getInstance().addPoint(attBuilder, latitude, longitude); 
        JsonHelper.getInstance().addLatAndLong(attBuilder, latitude, longitude); 
    }

    private String createGeoHash(double latitude, double longitude) throws Exception {
        return GeoHash.withCharacterPrecision(latitude, longitude, numberOfCharacters).toBase32();
    } 
}
