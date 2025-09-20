package se.nrm.samlingar.data.process.vo;

/**
 *
 * @author idali
 */
public class Determination {
    
    private final String currentDetermination;
    private final String identifiedBy;
//    private final String identifiedYear; 
    private final String dateIdentified;
    
    public Determination(String currentDetermination, String identifiedBy, String dateIdentified) { 
        this.currentDetermination = currentDetermination;
        this.identifiedBy = identifiedBy; 
        this.dateIdentified = dateIdentified;
    }

    public String getCurrentDetermination() {
        return currentDetermination;
    }

    public String getDateIdentified() {
        return dateIdentified;
    }

    public String getIdentifiedBy() {
        return identifiedBy;
    }

//    public String getIdentifiedYear() {
//        return identifiedYear;
//    }
// 
    
    @Override
    public String toString() {
        return currentDetermination + " -- " + identifiedBy;
    }
}
