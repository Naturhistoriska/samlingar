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
public class CoordinatesConverter implements Serializable {
  
    private final String dFormat = "^-?\\d{1,3}$";                                            // 56
    private final String dFormat1 = "^-?\\d+°$";                                            // 43º
    private final String doubleFormatRegex1 = "^\\d{1,3},\\d+$";                            // 76,773404
    private final String doubleFormatRegex2 = "^-?\\d{1,3}\\,\\d+$";                        // -76,773404
    private final String doubleFormatRegex3 = "^\\d{1,3}.\\d+$";                            // 76.773404
    private final String doubleFormatRegex4 = "^-?\\d{1,3}\\.\\d+$";                        // -76.773404

    private final String dmsFormatRegex1 = "^\\d{1,3}°\\d{1,2}'\\d{1,3}\\.\\d+\"$";         // 61°10'15.6"   
    private final String dmsFormatRegex2 = "^\\d{1,3}°\\d{1,2}'\\d{1,2}\"$";                // 47°32'00"     47°32'00"

    private final String dmsFormatRegex3 = "^\\d{1,3}°\\d{1,2}\\.\\d+'\\d{1,2}\\.\\d+\"$";  // 71°28.8'0.0" 71°28.8'0.0" N  

    private final String dmsFormatRegex4 = "^\\d{1,3}°\\d{1,2}(\\.\\d+)?'\\d+(\\.\\d+)?''$";  // 70°08.5'0.0'' 
    private final String dmsFormatRegex9 = "^\\d{1,3}°\\d{1,2}(\\.\\d+)?'\\''$";              // 70°08.5''' 

    private final String dmsFormatRegex5 = "^\\d{1,3}°\\d{2}'\\d{1,3}$";                    // 65°51'00
    private final String dmsFormatRegex6 = "^\\d{1,3}°\\d{1,2}´\\d{1,3}\"$";                // 76°27´00"
        
    private final String dmsFormatRegex7 = "^\\d{1,3}°\\d{2}'$";                            // 21°24'N
    private final String dmsFormatRegex8 = "^\\d{1,3}°\\d{1,2}\\.\\d+'\\d{1,2}\"$";         // 123°48.5'0" E
    
    private final String dddFormatRegex1 = "^\\d{1,3}°\\d{1,2} \\d{1,3}$";                  // 57°15 27    57°04 000  
    private final String dddFormatRegex2 = "^\\d{1,3}°\\d{1,2}\\s\\d{1,2}\\.\\d+$";         // 49°44 07.8   
    private final String dddFormatRegex3 = "^\\d{1,3}°\\d{1,2}  \\d{1,2}$";                 // 48°52  00 N   57°01  53 
    private final String dddFormatRegex4 = "^\\d{1,3}°\\s\\d{1,2}\\s\\s\\d{1,2}$";          // 54° 36  00
    
    
    private final String regex1 = "^\\d{1,3}\\s\\d{1,2}\\s\\d{1,2}$";                       // 78 30 00
    private final String regex2 = "^\\d{1,3}'\\d{1,2}'\\d{1,2}$";                           // 64'23'00
    private final String regex3 = "^(\\d+)\\d+$";                                           // N560700       E0143130
                                         
    private final String regex4 = "^(\\d+)\\'\\d+\\\"\\d+\\\"$";                            // 34'40"00"  N34'40"00" 
    private final String regex5 = "^(\\d+)\\'\\d+\\\"\\d{1,2}$";                            // E111'55"00
    private final String regex6 = "^(\\d+)\\s\\d+$";                                        // N63 05   
    
    private final String regex7 = "^-?\\d{1,3}\\.\\d+°$";                                   //  -51.70° 
    
    
    private final String latRex1  = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$";      // 76.773404 -76.773404
//    private final String latRex2  = "^[-+]?([1-8]?\\d(\\,\\d+)?|90(\\.0+)?)$";      // 76,773404 -76.773404
    private final String latRex2 =  "^[-+]?([1-8]?\\d{1,2}?|90(\\.0+)?)$";
    private final String latRex3  = "^[-+]?([1-8]?\\d(\\,\\d+)?|90(\\.0+)?)$";      // 76,773404 -76.773404
    
    private final String rex1 = "^[-+]?\\d{1,3}\\.\\d+°$";   
    
    
    
    private final String lonRex1 = "^-?((1[0-7]\\d)|(\\d{1,2}))(\\.\\d+)?$|^-?180(\\.0+)?$";
    private final String lonRex2 = "^-?((1[0-7]\\d)|(\\d{1,2}))?$|^-?180(\\.0+)?$";
    private final String lonRex3 = "^-?((1[0-7]\\d)|(\\d{1,2}))(\\,\\d+)?$|^-?180(\\.0+)?$";
    
    
    
    
//    private final String lonRex1 =  "^[-+]?([1-8]?\\d(\\.\\d+)?|180(\\.0+)?)$";
//    private final String lonRex2 =  "^[-+]?([1-8]?\\d{1,3}?|180(\\.0+)?)$";
    
     
    // pfossil.csv
    // W240°24'50"
    
    // afossil.csv
    // 47°32'00"N, 
    // °34'08.8" N 
    // 57°44'52.4"N 18°29'40.6"E
    // 56°52 20 N, 16° 49  13  E
    // 076°420'25"W
    
    private DMSCoordinate dmsCoord;
    private boolean isNorthEast;

    private int degrees;
    private int minutes;
    private double seconds;
    private double dblMinuts;
 

    private final String northEast = "NEne";
    private final String nesw = "NESWnesw";

    private final String emptyString = "";
    private final String emptySpace = " "; 

    private final String degreeSign = "°";
    private final String minuteSign = "'";
    private final String minuteSign1 = "´";
    private final String secondSign = "\"";
    private final String secondSign1 = "''";

    private final String colon = ":";
    private final String comma = ",";
    private final String dot = ".";
    
    private final double dblZero = 0.0;
    
    private double dblLat;
    private double dblLon;

    public CoordinatesConverter() {

    }
    
    public double convertLat(String lat) {
        log.info("convertLat: {}", lat);
        
        if (StringUtils.isBlank(lat)) {
            throw new SamlingarException(ErrorMsg.getInstance().getNoCoordinatesErrorMsg());
        }
        
        try {
            if (lat.matches(latRex1) || lat.matches(latRex2)) {
                dblLat = Util.getInstance().stringToDouble(lat);  
                if(dblLat >= -90.0 && dblLat <= 90.0) {
                    return dblLat;
                } else { 
                    throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + lat);
                } 
            } else if(lat.matches(latRex3)) {
                dblLat = Util.getInstance().stringToDouble(
                        StringUtils.replace(lat, comma, dot));
                if(dblLat >= -90.0 && dblLat <= 90.0) {
                    return dblLat;
                } else { 
                    throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + lat);
                } 
            } else if(lat.matches(rex1)) {
                // 63.43
                dblLat = Util.getInstance().stringToDouble(StringUtils.substringBefore(lat, degreeSign));
                if(dblLat >= -90.0 && dblLat <= 90.0) {
                    return dblLat;
                } else { 
                    throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + lat);
                } 
            } else {
                log.error("what...{}", lat);
                throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + lat);
            } 
        } catch (NumberFormatException e) {
            log.error("NumberFormatException: {} ", e.getMessage());
            throw new SamlingarException(e.getMessage());
        }  
    }
    
    public double convertLon(String lon) {
        log.info("convertLon: {}", lon);
        
        if (StringUtils.isBlank(lon)) {
            throw new SamlingarException(ErrorMsg.getInstance().getNoCoordinatesErrorMsg());
        }
        
        try {
            if (lon.matches(lonRex1) || lon.matches(lonRex2)) {
                return Util.getInstance().stringToDouble(lon); 
            } else if(lon.matches(lonRex3)) { 
                return Util.getInstance().stringToDouble(
                        StringUtils.replace(lon, comma, dot));
            }  else if(lon.matches(rex1)) {
                dblLon = Util.getInstance().stringToDouble(StringUtils.substringBefore(lon, degreeSign));
                if(dblLon >= -180.0 && dblLon <= 180.0) {
                    return dblLon;
                } else { 
                    throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + lon);
                } 
            } else {
                log.error("what...{}", lon);
                throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + lon);
            }
           
            
        } catch (NumberFormatException e) {
            log.error("NumberFormatException: {} ", e.getMessage());
            throw new SamlingarException(e.getMessage());
        }  
    }
    
    
    
    

    public double convert(String latOrLong ) {
        log.info("convert: {}", latOrLong);
        if (StringUtils.isBlank(latOrLong)) {
            throw new SamlingarException(ErrorMsg.getInstance().getNoCoordinatesErrorMsg());
        }

        try {
            if (latOrLong.matches(doubleFormatRegex1) || latOrLong.matches(doubleFormatRegex2)) {
                 
                return Util.getInstance().stringToDouble(
                        StringUtils.replace(latOrLong, comma, dot));
            } else if (latOrLong.matches(doubleFormatRegex3) || latOrLong.matches(doubleFormatRegex4)
                    || latOrLong.matches(dFormat)) {
                
                
                return Util.getInstance().stringToDouble(latOrLong);
            } else if(latOrLong.matches(dFormat1)) {
                return Util.getInstance().stringToDouble(StringUtils.substringBefore(latOrLong, degreeSign));
            }

            isNorthEast = StringUtils.containsAny(latOrLong, northEast);
            latOrLong = StringUtils.replaceChars(latOrLong, nesw, emptyString).trim();

            if (latOrLong.matches(dmsFormatRegex1) || latOrLong.matches(dmsFormatRegex2)) {
                // 61°10'15.6"              47°32'00"       
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if (latOrLong.matches(dmsFormatRegex3) || latOrLong.matches(dmsFormatRegex8)) {
                // 71°28.8'0.0"     123°48.5'0"
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                dblMinuts = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, dblMinuts, seconds);
            } else if (latOrLong.matches(dmsFormatRegex4)) {
                //   70°08.5'0.0'' 
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                dblMinuts = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign1));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, dblMinuts, seconds);
                
            } 
             else if(latOrLong.matches(dmsFormatRegex9)) {
                    // 8°6'''
                    degrees = Util.getInstance().stringToInt(
                            StringUtils.substringBefore(latOrLong, degreeSign));
                    minutes = Util.getInstance().stringToInt(
                            StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                    seconds = 0;
                    degrees = isNorthEast ? degrees : (-1) * degrees;
                     
                    return convert(degrees, minutes, seconds);
            } else if (latOrLong.matches(dmsFormatRegex5)) {
                // 65°51'00 
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                dblMinuts = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfter(latOrLong, minuteSign));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, dblMinuts, seconds);
            } else if (latOrLong.matches(dmsFormatRegex6)) {
                // 76°27´00"
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                dblMinuts = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign1));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign1, secondSign));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, dblMinuts, seconds);
            } else if (latOrLong.matches(dmsFormatRegex7)) {
                // 21°24'
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                dblMinuts = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
               
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, dblMinuts, dblZero);
            } else if (latOrLong.matches(dddFormatRegex1) || latOrLong.matches(dddFormatRegex2)
                    || latOrLong.matches(dddFormatRegex3)) {
                // 57°15 27    57°04 000    49°44  07.8 
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, emptySpace));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfterLast(latOrLong, emptySpace));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if (latOrLong.matches(dddFormatRegex4)) {
                // 54° 36  00
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, emptySpace, emptySpace));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfterLast(latOrLong, emptySpace));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if (latOrLong.matches(regex1)) {
                // 78 30 00  
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, emptySpace));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, emptySpace, emptySpace));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfterLast(latOrLong, emptySpace));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if (latOrLong.matches(regex2)) {
                // 64'23'00
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, minuteSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, minuteSign, minuteSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfterLast(latOrLong, minuteSign));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if (latOrLong.matches(regex3)) {
                // N560700       E0143130   
                switch (latOrLong.length()) {
                    case 7:
                        degrees = Util.getInstance().stringToInt(StringUtils.substring(latOrLong, 0, 3));
                        minutes = Util.getInstance().stringToInt(StringUtils.substring(latOrLong, 3, 5));
                        seconds = Util.getInstance().stringToDouble(StringUtils.substring(latOrLong, 5));
                        break;
                    case 6:
                        degrees = Util.getInstance().stringToInt(StringUtils.substring(latOrLong, 0, 2));
                        minutes = Util.getInstance().stringToInt(StringUtils.substring(latOrLong, 2, 4));
                        seconds = Util.getInstance().stringToDouble(StringUtils.substring(latOrLong, 4));
                        break;
                    default:
                        throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
                }
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if (latOrLong.matches(regex4)) {
                //  34'40"00" 
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, minuteSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, minuteSign, secondSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, secondSign, secondSign));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if (latOrLong.matches(regex5)) {
                //  111'55"00
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, minuteSign));
                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, minuteSign, secondSign));
                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfter(latOrLong, secondSign));
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, seconds);
            } else if (latOrLong.matches(regex6)) {
                //  63 05  
                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, emptySpace));
                minutes = Util.getInstance().stringToInt(StringUtils.substringAfter(latOrLong, emptySpace)); 
                degrees = isNorthEast ? degrees : (-1) * degrees;
                return convert(degrees, minutes, dblZero);
            } else if (latOrLong.matches(regex7)) {
                log.info("what... : {}", latOrLong);
                //  -51.70° 
                return Util.getInstance().stringToDouble(StringUtils.substringBefore(latOrLong, degreeSign));  
            } else {
                if (!latOrLong.equals("57°44'52.4\"N 18°29'40.6\"E")) {
                    log.error("invalid lat or long : {} -- {}", latOrLong, isNorthEast);
                }

                throw new SamlingarException(ErrorMsg.getInstance().getInvalidCoordinatesErrorMsg() + latOrLong);
            }

        } catch (NumberFormatException e) {
            log.error("NumberFormatException: {} ", e.getMessage());
            throw new SamlingarException(e.getMessage());
        }

//            if (latOrLong.matches(dmsRegex1Format1) || latOrLong.matches(dmsRegex1Format2)
//                    || latOrLong.matches(dmsRegex1Format3) || latOrLong.matches(dmsRegex1Format4) 
//                    || latOrLong.matches(dmsRegex1Format5) || latOrLong.matches(dmsRegex1Format6)) {
//                // 61°10'15.6" N   57°57'19.8"N   14°44'03"   47°32'00"N    47°32'00"N,
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign));
//                degrees = isSouthWest ? (-1) * degrees : degrees;
//                return convert(degrees, minutes, seconds);  
//            } else if(latOrLong.matches(dddRegex1Format1) || latOrLong.matches(dddRegex1Format2) 
//                    || latOrLong.matches(dddRegex1Format3) ) {
//                // 57°15 27 N    52°33 42.942 N      49°44 07.8  N  007°52 4015 E
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, emptySpace));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, emptySpace, emptySpace));
// 
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds);
//            } else if(latOrLong.matches(dddRegex1Format4)) {
//                // 48°52  00 N  
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, emptySpace));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, dblEmptySpace, emptySpace));
// 
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds);
//            } else if(latOrLong.matches(dmsRegex2Format1)) {
//                // 114°00'0.0'' E 
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign1));
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds);
//            } else if(latOrLong.matches(dmsRegex10)) {
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
//                dblMinuts = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign1));
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds);
//            } else if(latOrLong.matches(dmsRegex11)) {
//                // 120°34'00 E
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, emptySpace));
// 
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds);
//            } else if(latOrLong.matches(dmsRegex13)) {
//                // N55°47 49
//                latOrLong = StringUtils.substring(latOrLong, 1);
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, emptySpace));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfter(latOrLong, emptySpace));
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds); 
//            } else if(latOrLong.matches(dmsRegex14)) {
//                // 44°5321     08°5314 08°5314 1 44°5321
//                log.info("come here... {}", latOrLong);
//                return 0;
//            } else if(latOrLong.matches(regex1)) {
//                // N65°31'35"
//                latOrLong = StringUtils.substring(latOrLong, 1);
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, minuteSign));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringBetween(latOrLong, minuteSign, secondSign));
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds); 
//            } else if(latOrLong.matches(regex2)) {
//                // N65 31 35
//                latOrLong = StringUtils.substring(latOrLong, 1);
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, emptySpace));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, emptySpace, emptySpace));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfterLast(latOrLong, emptySpace));
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds); 
//            } else if(latOrLong.matches(regex3)) {
//                // S64'23'00
//                latOrLong = StringUtils.substring(latOrLong, 1);
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, minuteSign));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, minuteSign, minuteSign));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfterLast(latOrLong, minuteSign));
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds); 
//             } else if(latOrLong.matches(regex2)) {
//                // N57°49  30
//                latOrLong = StringUtils.substring(latOrLong, 1);
//                degrees = Util.getInstance().stringToInt(StringUtils.substringBefore(latOrLong, degreeSign));
//                minutes = Util.getInstance().stringToInt(StringUtils.substringBetween(latOrLong, degreeSign, emptySpace));
//                seconds = Util.getInstance().stringToDouble(StringUtils.substringAfterLast(latOrLong, emptySpace));
//                degrees = isNorthEast ? degrees : (-1) * degrees;
//                return convert(degrees, minutes, seconds); 
//            } else {
////                if(!latOrLong.equals("57°44'52.4\"N 18°29'40.6\"E")) {
//                    log.error("invalid lat or long : {} -- {}", latOrLong, isNorthEast);
////                } 
//                throw new SamlingarException(invalidCoordinatesErrorMsg + latOrLong);
//            }
    }
    
    private boolean isValidLatitude(double latitude) {
        return latitude >= -90.0 && latitude <= 90.0;
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
