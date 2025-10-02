package se.nrm.samlingar.data.process.logic.bot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import se.nrm.samlingar.data.process.vo.Name;
import se.nrm.samlingar.data.process.vo.NameSynonyms;
import se.nrm.samlingar.data.process.vo.Synonyms;
import se.nrm.samlingar.data.process.vo.Taxon;

/**
 *
 * @author idali
 */
@Slf4j
public class CsvDataExtraction implements Serializable {

    private final String emptySpace = " ";
    private final String familyKey = "familj";



    private final String catalogNameIdKey = "katalognamnsID";
    private final String scientificNameKey = "scientificName";
    private final String kindomKey = "kindom";
    private final String phylumKey = "phylum";
    private final String orderKey = "order"; 
    private final String familyKey1 = "family"; 

    private String catalogId;
    private String nameId;
    private String name;
    private String author;
    private String genus;
    private String family;
    private String catalogNameId;
    
    private String scientificName;
    private String kindom;
    private String phylum;
    private String order; 
    private String highClassification;

    private Name nameVo;
    private Synonyms synonymsVo;
    private Taxon taxonVo;

    private StringBuilder synonymSb;

 
    private Map<String, NameSynonyms> map;
    private List<Name> nameList;
    private List<Synonyms> synonymsList;
    
    
    // name record
    private String authorship;
    private final String authorKey = "auktor"; 
    private final String genusKey = "slaekte";
    private final String catalogIdKey = "katalogID";
    private final String nameIdKey = "namnID";
    private final String nameKey = "namn";
    private Map<String, List<Name>> nameMap;



    public CsvDataExtraction() {

    }
    
    public Map<String, Taxon> extractTaxon(List<CSVRecord> taxonRecords, JsonObject taxonJson) {
        Map<String, Taxon> taxonMap = new LinkedHashMap<>();
        taxonRecords.stream()
                .forEach(taxon -> {
                    scientificName = taxon.get(scientificNameKey);
                    kindom = taxon.get(kindomKey);
                    phylum = taxon.get(kindomKey);
                    
                    taxonMap.put(taxon.get(0), taxonVo);
                });
        
        return null;
    }

    public Map<String, String> extractExsiccate(List<CSVRecord> exsiccateRecords) {
        Map<String, String> exisccateMap = new LinkedHashMap<>();
        exsiccateRecords.stream()
                .forEach(exsiccat -> {
                    exisccateMap.put(exsiccat.get(0), exsiccat.get(1));
                });
        return exisccateMap;
    }

    public Map<String, NameSynonyms> extractName(List<CSVRecord> nameRecords, List<CSVRecord> catalogRecords) {
        log.info("extractName");

        buildNameMap(nameRecords);
        return buildCatalogMap(catalogRecords);
    }

    private Map<String, NameSynonyms> buildCatalogMap(List<CSVRecord> catalogRecords) {
        log.info("buildCatalogMap ");

        map = new LinkedHashMap<>();
        family = null;
        catalogId = null;
        nameId = null;
        scientificName = null;

        catalogRecords.stream()
                .forEach(catalogRecord -> {

                    catalogId = catalogRecord.get(0);

                    log.info("catalogId : {}", catalogId);

                    family = catalogRecord.get(familyKey);
                    catalogNameId = catalogRecord.get(catalogNameIdKey);
                    log.info("catalogNameId : {}", catalogNameId);

                    if (!StringUtils.isBlank(catalogNameId)) {
                        synonymsList = new ArrayList();
                        nameMap.get(catalogId)
                                .stream()
                                .forEach(n -> {
                                    nameId = n.getNameId();
                                    genus = n.getGenus();
                                    author = n.getAuthor();
                                    name = n.getName();
                                    synonymSb = new StringBuilder();

                                    log.info("nameId : {}", nameId);
                                    if (!nameId.equals(catalogNameId)) { 
                                        synonymSb.append(name);
                                        synonymSb.append(emptySpace);
                                        synonymSb.append(author);
                                        synonymsVo = new Synonyms(name, synonymSb.toString().trim());
                                        synonymsList.add(synonymsVo);
                                    } else {
                                        authorship = author;
                                        scientificName = name;
                                    }
                                });
                        map.put(catalogId, new NameSynonyms(scientificName, 
                                genus, authorship, family, synonymsList)); 
                    } 
                });
        return map;
    }

    private void buildNameMap(List<CSVRecord> nameRecords) {
        log.info("buildNameMap");

        nameList = new ArrayList<>();

        nameId = null;
        name = null;
        author = null;
        catalogId = null;
        genus = null;

        nameMap = new LinkedHashMap<>();
        nameRecords.stream()
                .forEach(nameRecord -> {
                    nameId = nameRecord.get(nameIdKey);
                    catalogId = nameRecord.get(catalogIdKey);
                    name = nameRecord.get(nameKey);
                    author = nameRecord.get(authorKey);
                    
                    genus = nameRecord.get(genusKey);
                    nameVo = new Name(nameId, name, author, genus);

                    if (nameMap.containsKey(catalogId)) {
                        nameList = nameMap.get(catalogId);
                    } else {
                        nameList = new ArrayList();
                    }
                    nameList.add(nameVo);
                    nameMap.put(catalogId, nameList);
                });
    }
}
