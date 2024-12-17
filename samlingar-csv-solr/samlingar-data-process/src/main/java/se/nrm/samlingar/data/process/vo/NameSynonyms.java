package se.nrm.samlingar.data.process.vo;

import java.util.List;

/**
 *
 * @author idali
 */
public class NameSynonyms {
      
    private final String name;
    private final String genus;
    private final String author;
    private final String family;
     private final List<Synonyms> synomys;
    
    public NameSynonyms(String name, String genus, String author, 
            String family, List<Synonyms> synomys) {
        this.name = name;
        this.genus = genus;
        this.author = author;
        this.family = family;
        this.synomys = synomys;
    }

    public String getName() {
        return name;
    }

    public String getGenus() {
        return genus;
    }

    public String getAuthor() {
        return author;
    }

    public String getFamily() {
        return family;
    }

    public List<Synonyms> getSynomys() {
        return synomys;
    }
    
    @Override
    public String toString() {
        return name + " " + family;
    }
}
