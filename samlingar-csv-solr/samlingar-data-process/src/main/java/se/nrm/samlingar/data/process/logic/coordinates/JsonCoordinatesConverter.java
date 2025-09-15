package se.nrm.samlingar.data.process.logic.coordinates;

import java.io.Serializable;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
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

    private final String latitudeKey = "latitude";
    private final String longtitudeKey = "longitude";

    private final String geoKey = "geo";
    private final String geohashKey = "geohash";
    private final String coordinateKey = "coordinate";
    private final String mapKey = "map";

    private String latitude;
    private String longitude;
    private double doubleLat;
    private double doubleLng;

    private String[] latLngArray;

//    private String coordinates;
    private final String comma = ",";
    private final String underScore = "_";
    private final String slash = "/";
    private final String emptySpace = " ";

    private final int numberOfCharacters = 9;
    private final String north = "N";
    private final String east = "E";

    private final String latitudeTextKey = "latitudeText";
    private final String longitudeTextKey = "longitudeText";

    private StringBuilder sb;
    private StringBuilder geoHashSb;

    private JsonArrayBuilder georHashArrayBuilder;

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

        doubleLat = botConvert.convertVascularPlantsLatLng(latDegreeKey,
                latMinuteKey, latSecondKey, latDirectionKey, record);
        doubleLng = botConvert.convertVascularPlantsLatLng(lngDegreeKey,
                lngMinuteKey, lngSecondKey, lngDirectionKey, record);

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
    }

    private void addGeoData(JsonObjectBuilder attBuilder, double latitude,
            double longitude) throws Exception {

        JsonHelper.getInstance().addCoordinates(attBuilder, latitude, longitude);
        JsonHelper.getInstance().addPoint(attBuilder, latitude, longitude);

        log.info("point added....");
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
