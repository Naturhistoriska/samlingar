package se.nrm.samlingar.data.process.logic.coordinates;

import com.peertopark.java.geocalc.DMSCoordinate;
import java.io.Serializable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.data.process.logic.exception.ErrorMsg;
import se.nrm.samlingar.data.process.logic.exception.SamlingarException;
import se.nrm.samlingar.data.process.logic.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class BotCoordinatesConvert implements Serializable {
    
    
    
    private final String regex1 = "\\d{1,3}º \\d{1,2},\\d{1,3}' [NSEW]";
    private final String regex2 = "\\d{1,3}º \\d{1,2}' [NSEW]";                 // 47º 39' N
    private final String regex3 = "\\d{1,3}º \\d{1,2}' \\d{1,3}'' [NSEW]";      // 11º 16' 33'' E
    private final String regex4 = "\\d{1,3},\\d+º [NSEW]";                         // 50,804988º N
    
    private final String regex5 = "\\d+º [NSEW]";                  //59º N   
    private final String regex6 = "\\d{1,3}º \\d{1,2}' \\d{1,2},\\d+'' [NSEW]"; // 56º 07' 53,4'' N
        
    
    private DMSCoordinate dmsCoord;
    private boolean isNorthEast;
    
    private final String northEast = "NEne";
    private final String nesw = "NESWnesw";
    
    private final String degreeSign = "°";
    private final String degreeSign1 = "º";
    private final String minuteSign = "'";
    private final String secondSign = "''";
    
    private final String comma = ",";
    private final String dot = ".";
    
    private int degrees;
    private int minutes;
    private double seconds;
    private double dblMinuts;
    private double dblLatOrLong;

    
    public BotCoordinatesConvert() {
        
    }
    
    public double convert(String latOrLong, boolean isLatitude) {
        if (StringUtils.isBlank(latOrLong)) {
            throw new SamlingarException(ErrorMsg.getInstance().getNoCoordinatesErrorMsg());
        }
        
        isNorthEast = StringUtils.containsAny(latOrLong, northEast);
        try {
            if (latOrLong.matches(regex1) || latOrLong.matches(regex2)) {
                // 33º 59,967' S    25º 41,89' E
                degrees = Util.getInstance().stringToInt(
                        StringUtils.substringBefore(latOrLong, degreeSign1));
 
                dblMinuts = Util.getInstance().stringToDouble(
                        StringUtils.substringBetween(
                                StringUtils.replace(latOrLong, comma, dot), degreeSign1, minuteSign)
                        ); 
                 
                degrees = isNorthEast ? degrees : (-1) * degrees;
                
                if(isLatitude) {
                    if(degrees >= 90 || degrees <= -90) {
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                } else {
                    if(degrees >= 180 || degrees <= -180) { 
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                }
                return convert(degrees, dblMinuts, 0);
            } else if(latOrLong.matches(regex3) ) {
                  // 11º 16' 33'' E
                degrees = Util.getInstance().stringToInt(
                        StringUtils.substringBefore(latOrLong, degreeSign1));
 
                dblMinuts = Util.getInstance().stringToDouble(
                        StringUtils.substringBetween(latOrLong, degreeSign1, minuteSign)); 
                
                seconds = Util.getInstance().stringToDouble(
                        StringUtils.substringBetween(latOrLong, minuteSign, secondSign)); 
                
                if(isLatitude) {
                    if(degrees >= 90 || degrees <= -90) {
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                } else {
                    if(degrees >= 180 || degrees <= -180) { 
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                }
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, dblMinuts, seconds);
            } else if(latOrLong.matches(regex4)) {
                // 50,804988º N
                dblLatOrLong =  Util.getInstance().stringToDouble(
                        StringUtils.substringBefore(
                                StringUtils.replace(latOrLong, comma, dot), degreeSign1));
                if(isLatitude) {
                    if(dblLatOrLong >= 90.0 || dblLatOrLong <= -90.0) { 
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                } else {
                    if(dblLatOrLong >= 180.0 || dblLatOrLong <= -180.0) { 
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                }
                
                return isNorthEast ? dblLatOrLong : (-1) * dblLatOrLong;
            } else if(latOrLong.matches(regex5)) {
                // 50,804988º N
                dblLatOrLong =  Util.getInstance().stringToDouble(
                        StringUtils.substringBefore(latOrLong, degreeSign1));
                
                if(isLatitude) {
                    if(dblLatOrLong >= 90.0 || dblLatOrLong <= -90.0) { 
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                } else {
                    if(dblLatOrLong >= 180.0 || dblLatOrLong <= -180.0) { 
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                }
                
                return isNorthEast ? dblLatOrLong : (-1) * dblLatOrLong;
                
            } else if(latOrLong.matches(regex6)) {
                // 56º 07' 53,4'' N
                degrees = Util.getInstance().stringToInt(
                        StringUtils.substringBefore(latOrLong, degreeSign1));
                
                minutes = Util.getInstance().stringToInt(
                        StringUtils.substringBetween(latOrLong, degreeSign1, minuteSign).trim());
                
                seconds = Util.getInstance().stringToDouble(
                        StringUtils.substringBetween(
                                StringUtils.replace(latOrLong, comma, dot), 
                                minuteSign, secondSign).trim()
                        ); 
                if(isLatitude) {
                    if(degrees >= 90 || degrees <= -90) {
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                } else {
                    if(degrees >= 180 || degrees <= -180) { 
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                    }
                }
                
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, dblMinuts, seconds);
            } else {
                throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
            }
        } catch (NumberFormatException e) {
            log.error("NumberFormatException: {} ", e.getMessage());
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
