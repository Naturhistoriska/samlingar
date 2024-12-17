package se.nrm.samlingar.data.process.vo;
  
/**
 *
 * @author idali
 */
public class Name {
    private final String nameId;
    private final String name;
    private final String author;
    private final String genus;  

    
    public Name(String nameId, String name, String author, String genus ) { 
        this.nameId = nameId;
        this.name = name;
        this.author = author;
        this.genus = genus; 
    }

    public String getName() {
        return name;
    }
 
    public String getAuthor() {
        return author;
    }

    public String getNameId() {
        return nameId;
    }

    public String getGenus() {
        return genus;
    }
 
    
    @Override
    public String toString() {
        return name + " -- " + author+ " -- " + genus ;
    }
}
