package se.nrm.samlingar.data.process.vo;

/**
 *
 * @author idali
 */
public class Taxon {
    
    private String scientificName;
    private String kindom;
    private String phylum;
    private String order;
    private String family;
    private String genus;
    private String highClassification;
  
    public Taxon(String scientificName, String kindom, String phylum, String order,
            String family, String genus, String highClassification) {
        this.scientificName = scientificName;
        this.kindom = kindom;
        this.phylum = phylum;
        this.order = order;
        this.genus = genus;
        this.highClassification = highClassification;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getKindom() {
        return kindom;
    }

    public void setKindom(String kindom) {
        this.kindom = kindom;
    }

    public String getPhylum() {
        return phylum;
    }

    public void setPhylum(String phylum) {
        this.phylum = phylum;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getHighClassification() {
        return highClassification;
    }

    public void setHighClassification(String highClassification) {
        this.highClassification = highClassification;
    }
    
    @Override
    public String toString() {
        return scientificName;
    }
    
}
