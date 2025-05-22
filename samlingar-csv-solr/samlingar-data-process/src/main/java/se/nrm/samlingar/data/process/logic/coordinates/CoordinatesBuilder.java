package se.nrm.samlingar.data.process.logic.coordinates;

import ch.hsr.geohash.GeoHash; 
import java.io.Serializable; 
import javax.inject.Inject;
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j; 
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.data.process.logic.exception.SamlingarException;
import se.nrm.samlingar.data.process.logic.json.JsonHelper; 

/**
 *
 * @author idali
 */
@Slf4j
public class CoordinatesBuilder implements Serializable {

    private final int numberOfCharacters = 5; 
    private final String emptySpace = " ";
    private final String slash = "/"; 
    
    private String geoHash; 
    private double dblLat;
    private double dblLong; 
    
    private String strLatitude;
    private String strLongitude;
    private String[] latLngArray;
    
    @Inject 
    private CoordinatesConverter convert;
    @Inject
    private BotCoordinatesConvert botConvert;
    
    public CoordinatesBuilder() {

    }
    
    public void buildBotCoordinates(JsonObjectBuilder attBuilder, String coordinates) {
        log.info("buildBotCoordinates : {}", coordinates);
        if(!StringUtils.isBlank(coordinates)) {
            latLngArray = coordinates.split(slash);
         
            strLatitude = latLngArray[0].trim();
            strLongitude = latLngArray[1].trim();
            try {
                dblLat = botConvert.convert(strLatitude, true);
                dblLong = botConvert.convert(strLongitude, false);
                log.info("latitude and longigude: {} -- {}", dblLat, dblLong);

                addGeoData(attBuilder, dblLat, dblLong);
            } catch (SamlingarException ex) {
                log.error("SamlingarException: builderCoordinates : {}", ex.getErrorMessage());
            } catch (Exception ex) {
                log.error("builderCoordinates : {}", ex.getMessage());
            }
        }
    }

    public void build(JsonObjectBuilder attBuilder, String coordinates) {
        log.info("CoordinatesBuilder build : {}", coordinates);
        if(!StringUtils.isBlank(coordinates)) {
            latLngArray = coordinates.split(emptySpace);
         
            strLatitude = latLngArray[0].trim();
            strLongitude = latLngArray[1].trim();
            build(attBuilder, strLatitude, strLongitude);
        }
    }
    
    public void build(JsonObjectBuilder attBuilder, String latitude, String longitude) {
        log.info("build: {} -- {}", latitude, longitude);
        
        try { 
            dblLat = convert.convertLat(latitude);
            dblLong = convert.convertLon(longitude);
            log.info("latitude and longigude: {} -- {}", dblLat, dblLong);
             
            addGeoData(attBuilder, dblLat, dblLong); 
        } catch(SamlingarException ex) { 
//            log.error("SamlingarException: builderCoordinates : {}", ex.getErrorMessage());
        } catch (Exception ex) {
            log.error("builderCoordinates : {}", ex.getMessage());
        }
    }
    
    public void buildPaleoCoordinates(JsonObjectBuilder attBuilder, String latitude, String longitude) {
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
         
//        geoHash = createGeoHash(latitude, longitude); 
//         
        JsonHelper.getInstance().addCoordinates(attBuilder, latitude, longitude); 
//        JsonHelper.getInstance().addGeoHash(attBuilder, geoHash);  
        JsonHelper.getInstance().addPoint(attBuilder, latitude, longitude); 
        
        log.info("point added....");
        JsonHelper.getInstance().addLatAndLong(attBuilder, latitude, longitude); 
        log.info("latAndLong added....");
    }

//    private String createGeoHash(double latitude, double longitude) throws Exception {
//        return GeoHash.withCharacterPrecision(latitude, longitude, numberOfCharacters).toBase32();
//    } 
}
