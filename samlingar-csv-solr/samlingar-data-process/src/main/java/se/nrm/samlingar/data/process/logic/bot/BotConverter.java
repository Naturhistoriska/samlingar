package se.nrm.samlingar.data.process.logic.bot;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;  
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.data.process.logic.json.JsonHelper;
import se.nrm.samlingar.data.process.vo.Determination;
import se.nrm.samlingar.data.process.vo.NameSynonyms;
import se.nrm.samlingar.data.process.vo.Synonyms;

/**
 *
 * @author idali
 */
@Slf4j
public class BotConverter implements Serializable {
    
    private final String identificationKey = "previousIdentifications";
    private final String hasImagesKey = "hasImage";
    private final String associatedMediaKey = "associatedMedia";
    private final String emptySpace = " ";
    private final String startBracket = "[";
    private final String endBracket = "]";
    
    private final String scientificNameAuthorshipKey = "scientificNameAuthorship";
    private final String speciesKey = "species";
    private final String genusKey = "genus";
    private final String familyKey = "family";
     
    private List<String> imageList;
    private JsonArrayBuilder imageArrayBuilder;
    
    private String taxonWithAuthor;
    private String identifiedBy;
    private String identifiedYear;
    
    private List<Determination> list;
    private JsonArrayBuilder determinationArrayBuilder;
    private StringBuilder determinationSb;
    
    private JsonArrayBuilder synomysArrayBuilder;
    private JsonArrayBuilder synomyAuthorsArrayBuilder;
    
    private final String synonymKey = "synonyms";
    private final String synonymAuthorKey = "synonymAuthor";
       
    public BotConverter() {
        
    }
    
    public void addImage(JsonObjectBuilder builder, Map<String, List<String>> imageMap, String catalogNumber) {
        try {     
            imageList = imageMap.get(catalogNumber);
            if(imageList != null && !imageList.isEmpty()) {
                imageArrayBuilder = Json.createArrayBuilder();
                imageList.stream()
                        .forEach(image -> {
                            imageArrayBuilder.add(image);
                        }); 
                builder.add(hasImagesKey, true);
                builder.add(associatedMediaKey, imageArrayBuilder);   
            } 
        } catch (Exception ex) {
            log.error("addImage", ex.getMessage());
        }
    }
    
 
    
    public void addSynonyms(JsonObjectBuilder builder,  NameSynonyms nameSynonyms) {
         
        if (nameSynonyms != null) { 
            JsonHelper.getInstance().addAttValue(builder, 
                    scientificNameAuthorshipKey, nameSynonyms.getAuthor());
            JsonHelper.getInstance().addAttValue(builder, speciesKey, nameSynonyms.getName());
            JsonHelper.getInstance().addAttValue(builder, familyKey, nameSynonyms.getFamily());
            JsonHelper.getInstance().addAttValue(builder, genusKey, nameSynonyms.getGenus());
            
            addSynomys(builder, nameSynonyms.getSynomys());
        }

    }
     
    private void addSynomys(JsonObjectBuilder builder, List<Synonyms> synomys) { 
         
        if (synomys != null && !synomys.isEmpty()) {
            log.info("addSynomys : {}", synomys.size());
            synomysArrayBuilder = Json.createArrayBuilder();
            synomyAuthorsArrayBuilder = Json.createArrayBuilder(); 
            synomys.stream()
                    .forEach(synomy -> { 
                        synomysArrayBuilder.add(synomy.getSynonyms());
                        synomyAuthorsArrayBuilder.add(synomy.getSynonymsWithAuthor());
                    });
            builder.add(synonymKey, synomysArrayBuilder);
            builder.add(synonymAuthorKey, synomyAuthorsArrayBuilder);
        }
    }

    public void convertDetermination(JsonObjectBuilder builder,
            Map<String, List<Determination>> determinationMap, String catalogNumber) { 

        taxonWithAuthor = null;
        identifiedBy = null;
        identifiedYear = null;
        try {
            list = determinationMap.get(catalogNumber); 
            if (list != null && !list.isEmpty()) {
//                log.info("has determination : {}", regNumber);
                determinationArrayBuilder = Json.createArrayBuilder();
                list.stream()
                        .forEach(vo -> { 
                            determinationSb = new StringBuilder();
                            taxonWithAuthor = vo.getCurrentDetermination();
                            identifiedBy = vo.getIdentifiedBy();
                            identifiedBy = vo.getIdentifiedYear(); 
                            if (taxonWithAuthor != null) {
                                determinationSb.append(taxonWithAuthor);
                                determinationSb.append(emptySpace);
                                determinationSb.append(identifiedBy);
                                if (!StringUtils.isBlank(identifiedYear)) {
                                    determinationSb.append(emptySpace);
                                    determinationSb.append(startBracket);
                                    determinationSb.append(identifiedYear);
                                    determinationSb.append(endBracket);
                                }
                            }
                            
                            determinationArrayBuilder.add(determinationSb.toString().trim());
                        });
                builder.add(identificationKey, determinationArrayBuilder);
            }

        } catch (Exception ex) {
            log.error("BotConverter.convertDetermination : {}", ex.getMessage());
        }
    }
}
