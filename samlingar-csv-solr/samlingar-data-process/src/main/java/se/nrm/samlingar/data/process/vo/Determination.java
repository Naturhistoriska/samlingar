package se.nrm.samlingar.data.process.vo;

/**
 *
 * @author idali
 */
public class Determination {
    
    private final String currentDetermination;
    private final String identifiedBy;
    private final String identifiedYear; 
    
    public Determination(String currentDetermination, String identifiedBy, 
            String identifiedYear ) { 
        this.currentDetermination = currentDetermination;
        this.identifiedBy = identifiedBy;
        this.identifiedYear = identifiedYear; 
    }

    public String getCurrentDetermination() {
        return currentDetermination;
    }

    public String getIdentifiedBy() {
        return identifiedBy;
    }

    public String getIdentifiedYear() {
        return identifiedYear;
    }
 
    
    @Override
    public String toString() {
        return currentDetermination + " -- " + identifiedBy;
    }
}
