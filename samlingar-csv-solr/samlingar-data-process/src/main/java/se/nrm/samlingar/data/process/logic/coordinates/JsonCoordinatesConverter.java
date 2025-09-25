package se.nrm.samlingar.data.process.logic.coordinates;

import java.io.Serializable;
import javax.inject.Inject; 
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import se.nrm.samlingar.data.process.logic.exception.SamlingarException;
import se.nrm.samlingar.data.process.logic.json.JsonHelper;

/**
 *
 * @author idali
 */
@Slf4j
public class JsonCoordinatesConverter implements Serializable {
 
    private final String emptySpace = " ";
     
    private final String degreeSign = "°";
    private final String minuteSign = "'"; 
    private final String secondSign = "\"";
    private final String slash = "/";
 
    private double doubleLat;
    private double doubleLng; 
    
    private String latDegree;
    private String latMinute;
    private String latSecond;
    private String lonDegree;
    private String lonMinute;
    private String lonSecond;
    
    private String latDirection;
    private String lonDirection;
    
    private StringBuilder verbatimCoordinatesSb;
 

    private final double double200 = 200.0;

    @Inject
    private CoordinatesConverter convert;

    @Inject
    private BotCoordinatesConvert botConvert;

    public JsonCoordinatesConverter() {

    }

    public JsonCoordinatesConverter(CoordinatesConverter convert) {
        this.convert = convert;
    }

    public void convertVascularPlantsCoordination(JsonObjectBuilder attBuilder,
            String latDegreeKey, String latMinuteKey, String latSecondKey,
            String latDirectionKey, String lngDegreeKey, String lngMinuteKey,
            String lngSecondKey, String lngDirectionKey, CSVRecord record) {
        
        latDegree = record.get(latDegreeKey).trim();
        latMinute = record.get(latMinuteKey).trim();
        latSecond = record.get(latSecondKey).trim(); 
        latDirection = record.get(latDirectionKey).trim();
        
        lonDegree = record.get(lngDegreeKey).trim();
        lonMinute = record.get(lngMinuteKey).trim();
        lonSecond = record.get(lngSecondKey).trim(); 
        lonDirection = record.get(lngDirectionKey).trim();

        doubleLat = botConvert.convertVascularPlantsLatLng(latDegree,
                latMinute, latSecond, latDirection );
        doubleLng = botConvert.convertVascularPlantsLatLng(lonDegree,
                lonMinute, lonSecond, lonDirection);

        if (doubleLat != double200 && doubleLng != double200) {
            log.info("coordinates : {} -- {}", doubleLat, doubleLng);
            try {
                addGeoData(attBuilder, doubleLat, doubleLng);
            } catch (SamlingarException ex) {
//            log.error("SamlingarException: builderCoordinates : {}", ex.getErrorMessage());
            } catch (Exception ex) {
                log.error("builderCoordinates : {}", ex.getMessage());
            } 
        }
        addVerbatimCoordinates(attBuilder);
    }
    
    private void addVerbatimCoordinates(JsonObjectBuilder attBuilder ) {
          
        verbatimCoordinatesSb = new StringBuilder();
        verbatimCoordinatesSb.append(latDegree);
        verbatimCoordinatesSb.append(degreeSign);
        verbatimCoordinatesSb.append(emptySpace);
        verbatimCoordinatesSb.append(latMinute);
        verbatimCoordinatesSb.append(minuteSign);
        verbatimCoordinatesSb.append(emptySpace);
        verbatimCoordinatesSb.append(latSecond);
        verbatimCoordinatesSb.append(secondSign);
        verbatimCoordinatesSb.append(emptySpace);
        verbatimCoordinatesSb.append(latDirection);
        verbatimCoordinatesSb.append(emptySpace);
        verbatimCoordinatesSb.append(slash);
        verbatimCoordinatesSb.append(emptySpace);
         
        verbatimCoordinatesSb.append(lonDegree);
        verbatimCoordinatesSb.append(degreeSign);
        verbatimCoordinatesSb.append(emptySpace);
        verbatimCoordinatesSb.append(lonMinute);
        verbatimCoordinatesSb.append(minuteSign);
        verbatimCoordinatesSb.append(emptySpace);
        verbatimCoordinatesSb.append(lonSecond);
        verbatimCoordinatesSb.append(secondSign);
        verbatimCoordinatesSb.append(emptySpace);
        verbatimCoordinatesSb.append(lonDirection);
        
        JsonHelper.getInstance()
                .addVerbatimCoordinates(attBuilder, verbatimCoordinatesSb.toString());
        
    }

    private void addGeoData(JsonObjectBuilder attBuilder, double latitude,
            double longitude) throws Exception {

        JsonHelper.getInstance().addCoordinates(attBuilder, latitude, longitude);
//        JsonHelper.getInstance().addPoint(attBuilder, latitude, longitude);
 
        JsonHelper.getInstance().addLatAndLong(attBuilder, latitude, longitude);
        log.info("latAndLong added....");
    }

//
//    public void convertPaleoCoordinates(JsonObjectBuilder attBuilder,
//            String latitudeKey, String longitudeKey, CSVRecord record) {
//        try {
//            latitude = record.get(latitudeKey).trim();
//            longitude = record.get(longitudeKey).trim();
//
//            if (!StringUtils.isAnyBlank(latitude, longitude)) {
//                doubleLat = paleoCoordinateConvert.convert(latitude, true);
//                doubleLng = paleoCoordinateConvert.convert(longitude, false);
//
//                if (doubleLat != double200 && doubleLng != double200) {
//                    builderCoordinates(attBuilder, doubleLat, doubleLng);
//                } else {
//                    log.error("wrong coordinages");
//                }
//            }
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }
//    }
//    private void builderCoordinates(JsonObjectBuilder attBuilder, double latitude, double longitude) {
//        try { 
//            attBuilder.add(latitudeKey, latitude);
//            attBuilder.add(longtitudeKey, longitude);
//
//            addGeo(attBuilder, latitude, longitude);
//
//            JsonHelper.getInstance().addLatLngText(attBuilder,
//                    latitude, latitudeTextKey, north);
//            JsonHelper.getInstance().addLatLngText(attBuilder,
//                    longitude, longitudeTextKey, east);
//
//            addCoordinates(attBuilder, latitude, longitude);
//            attBuilder.add(mapKey, true);
//        } catch (Exception ex) {
//            log.error("builderCoordinates : {}", ex.getMessage());
//        }
//
//    }
//
//    private void addCoordinates(JsonObjectBuilder attBuilder, double latitude, double longitude) {
//        sb = new StringBuilder();
//        sb.append(north);
//        sb.append(latitude);
//        sb.append(east);
//        sb.append(longitude);
//        attBuilder.add(coordinateKey, sb.toString().trim());
//    }
// 
// 
//    private void addGeo(JsonObjectBuilder attBuilder, double latitude, double longitude) {
//        sb = new StringBuilder();
//        sb.append(latitude);
//        sb.append(comma);
//        sb.append(longitude);
//        attBuilder.add(geoKey, sb.toString().trim());
//    }
}
