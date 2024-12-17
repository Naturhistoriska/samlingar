package se.nrm.samlingar.data.process.vo;

/**
 *
 * @author idali
 */
public class Synonyms {
    
    private final String synonyms; 
    private final String synonymsWithAuthor;
    
    public Synonyms(String synonmys, String synonymsWithAuthor) {
        this.synonyms = synonmys;
        this.synonymsWithAuthor = synonymsWithAuthor;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public String getSynonymsWithAuthor() {
        return synonymsWithAuthor;
    }
    
    @Override
    public String toString() {
        return synonyms;
    }
    
}
