package se.nrm.samlingar.data.process.logic.coordinates;

import ch.hsr.geohash.GeoHash;
import com.peertopark.java.geocalc.DMSCoordinate;
import java.io.Serializable; 
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.data.process.logic.exception.SamlingarException;
import se.nrm.samlingar.data.process.logic.json.JsonHelper;
import se.nrm.samlingar.data.process.logic.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class CoordinatesBuilder implements Serializable {

    private final String northEast = "NE"; 
    private final String emptySpace = " ";
    private final String dblEmptySpace = "  ";
     
    private final String dmsRegex8 = "^\\d{1,3}°\\d+(\\.\\d+)?'(\\d+(\\.\\d+)?)?\\\" [NSWE]$";   // 71°28.8'0.0" N  71°28.8'0.0" N
                                
    private final String dmsRegex1 = "^\\d{1,3}°\\d{1,2}'\\d{1,2}(\\.\\d+)?\\\" [NSWE]$";   // 61°10'15.6" N
    private final String dmsRegex2 = "^\\d{1,3}°\\d{1,2}'\\d{1,2}(\\.\\d+)?\\\"[NSWE]$";    // 57°57'19.8"N
     
    private final String dmsRegex3 = "^\\d{1,3}°\\d{1,2} \\d{1,3} [NSWE]$";   // 57°15 27 N    57°04 000 N
    private final String dmsRegex4 = "^\\d{1,3}°\\d{1,2}  \\d{1,2} [NSWE]$";   // 48°52  00 N
    private final String dmsRegex5 = "^\\d{1,3}°\\d{1,2} \\d{1,2}(\\.\\d+)? [NSWE]$";   // 52°33 42.942 N   
    private final String dmsRegex6 = "^\\d{1,3}°\\d{1,2} \\d{1,2}(\\.\\d+)?  [NSWE]$";      // 49°44 07.8  N
    
    private final String dmsRegex7 = "^\\d{3}°\\d{2} \\d{1,4} [NSWE]$";                   // 007°52 4015 E
    
    private final String dmsRegex9 = "^\\d{1,3}°\\d{2}'\\d+(\\.\\d+)?'' [NSWE]$";  // 114°00'0.0'' E 
    private final String dmsRegex10 = "^\\d{1,3}°\\d{2}(\\.\\d+)?'\\d+(\\.\\d+)?'' [NSWE]$"; //     70°08.5'0.0'' N
    
    private final String dmsRegex11 = "^\\d{1,3}°\\d{2}'\\d{1,3} [NSWE]$";        // 120°34'00 E      
    
    private final String dmsRegex12 = "^\\d{1,2}°\\d{2}'\\d{2}\\\"$";               // 14°44'03"
    
    private final String dmsRegex13 = "^[NSWE]\\d{1,3}°\\d{2} \\d{2}$";             // N55°47 49 
    private final String dmsRegex14 = "^\\d{1,2}°\\d+[NSWE]$";                      // 08°5314    
    
    private final String dmsRegex15 = "^\\d{1,2}°\\d{2}'\\d{2}\\\"[NSWE]$";       // 47°32'00"N     47°32'00"N 
    private final String dmsRegex16 = "^\\d{1,2}°\\d{2}'\\d{2}\\\"[NSWE],$";        // 47°32'00"N,
      
    private final int numberOfCharacters = 5;
    private final String doubleFormat = "%.6f";
     
    private String geoHash;
    private String formattedLat;
    private String formattedLong;
     
    private DMSCoordinate dmsCoord;
    private boolean isNorthEast;

    private int degrees;
    private int minutes;
    private double seconds; 
    private double dblMinuts;
    
    private double dblLat;
    private double dblLong;

    private final String degreeSign = "°";
    private final String minuteSign = "'";
    private final String secondSign = "\"";
    private final String secondSign1 = "''";
     
    
    private final String invalidCoordinatesErrorMsg = "Invalid coordinates: ";

    public CoordinatesBuilder() {

    }

    public void build(JsonObjectBuilder attBuilder, String latitude, String longitude) {
//        log.info("build: {}", latitude, longitude);
        
        try { 
            dblLat = convert(latitude);
            dblLong = convert(longitude);
            log.info("latitude and longigude: {} -- {}", dblLat, dblLong);
            
            addGeoData(attBuilder, dblLat, dblLong); 
        } catch(SamlingarException ex) { 
//            log.error(ex.getErrorMessage());
        } catch (Exception ex) {
//            log.error("builderCoordinates : {}", ex.getMessage());
        }
    }
 
    private void addGeoData(JsonObjectBuilder attBuilder, double latitude,
            double longitude) throws Exception {
        log.info("addGeoData");
         
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
 

    private double convert(String latOrLong) {
        if (StringUtils.isBlank(latOrLong)) {
            throw new SamlingarException("No coordinates");
        }

        try {
            isNorthEast = StringUtils.containsAny(latOrLong, northEast); 
            if (latOrLong.matches(dmsRegex1) || latOrLong.matches(dmsRegex2)
                    || latOrLong.matches(dmsRegex12) || latOrLong.matches(dmsRegex15) 
                    || latOrLong.matches(dmsRegex16)) {
                // 61°10'15.6" N   57°57'19.8"N   14°44'03"   47°32'00"N    47°32'00"N,
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if(latOrLong.matches(dmsRegex8)) {
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                dblMinuts = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, dblMinuts, seconds); 
            } else if(latOrLong.matches(dmsRegex3) || latOrLong.matches(dmsRegex5) 
                    || latOrLong.matches(dmsRegex6) || latOrLong.matches(dmsRegex7)) {
                // 57°15 27 N    52°33 42.942 N      49°44 07.8  N
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, emptySpace));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, emptySpace, emptySpace));
 
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if(latOrLong.matches(dmsRegex4)) {
                // 48°52  00 N  
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, emptySpace));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, dblEmptySpace, emptySpace));
 
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if(latOrLong.matches(dmsRegex9)) {
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign1));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if(latOrLong.matches(dmsRegex10)) {
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                dblMinuts = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign1));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if(latOrLong.matches(dmsRegex11)) {
                // 120°34'00 E
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, emptySpace));
 
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if(latOrLong.matches(dmsRegex13)) {
                // N55°47 49
                latOrLong = StringUtils.substring(latOrLong, 1);
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, emptySpace));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfter(latOrLong, emptySpace));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds); 
            } else if(latOrLong.matches(dmsRegex14)) {
                // 44°5321     08°5314 08°5314 1 44°5321
                log.info("come here... {}", latOrLong);
                return 0;
            } else {
//                if(!latOrLong.equals("57°44'52.4\"N 18°29'40.6\"E")) {
//                    log.error("invalid lat or long : {} -- {}", latOrLong, isNorthEast);
//                } 
                throw new SamlingarException(invalidCoordinatesErrorMsg + latOrLong);
            }
            
        } catch (NumberFormatException e) {
//            log.error("NumberFormatException: {}", e.getMessage());
            throw new SamlingarException(e.getMessage());
        }
 
    }

    

    private double convert(int degrees, int minutes, double seconds) {
        dmsCoord = new DMSCoordinate(degrees, minutes, seconds);
        return dmsCoord.getDecimalDegrees();
    }

    private double convert(int degrees, double minutes, double seconds) {
        dmsCoord = new DMSCoordinate(degrees, minutes, seconds);
        return dmsCoord.getDecimalDegrees();
    }

}
