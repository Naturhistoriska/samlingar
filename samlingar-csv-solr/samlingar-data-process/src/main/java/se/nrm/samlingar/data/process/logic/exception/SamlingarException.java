package se.nrm.samlingar.data.process.logic.exception;

import javax.ejb.ApplicationException;

/**
 *
 * @author idali
 */
@ApplicationException
public class SamlingarException extends RuntimeException {

    private int errorCode;
    private String errorMsg;

    public SamlingarException() {
    }

    public SamlingarException(String s) {
        super(s);
        this.errorMsg = s;
    }

    public SamlingarException(String s, int errorCode) {
        super(s);
        this.errorCode = errorCode;
        this.errorMsg = s;

    }

    public int getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMsg;
    }
}
