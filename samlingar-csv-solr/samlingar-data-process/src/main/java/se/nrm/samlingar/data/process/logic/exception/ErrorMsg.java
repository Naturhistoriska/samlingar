package se.nrm.samlingar.data.process.logic.exception;

/**
 *
 * @author idali
 */
public class ErrorMsg {
    
    private final String errorMsgNoCoordinates = "No coordinates";
    private final String errorMsgInvalidCoordinates = "Invalid coordinates: ";
    
    
    private static ErrorMsg instance = null;

    public static ErrorMsg getInstance() {
        synchronized (ErrorMsg.class) {
            if (instance == null) {
                instance = new ErrorMsg();
            }
        }
        return instance;
    }
    
    public String getNoCoordinatesErrorMsg() {
        return errorMsgNoCoordinates;
    }

    public String getInvalidCoordinatesErrorMsg() {
        return errorMsgInvalidCoordinates;
    }

}
