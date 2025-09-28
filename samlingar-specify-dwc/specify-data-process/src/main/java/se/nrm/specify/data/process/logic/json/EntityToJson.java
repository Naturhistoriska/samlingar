package se.nrm.specify.data.process.logic.json;
 
import java.io.Serializable; 
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList; 
import java.util.List;
import java.util.Map;   
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArrayBuilder; 
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import se.nrm.specify.data.model.EntityBean;
import se.nrm.specify.data.model.impl.Accession;
import se.nrm.specify.data.model.impl.Agent;
import se.nrm.specify.data.model.impl.Attachment;
import se.nrm.specify.data.model.impl.Collectingevent;
import se.nrm.specify.data.model.impl.Collectionobject;
import se.nrm.specify.data.model.impl.Collectionobjectattachment;
import se.nrm.specify.data.model.impl.Collectionobjectattribute;
import se.nrm.specify.data.model.impl.Collector;
import se.nrm.specify.data.model.impl.Commonnametx;
import se.nrm.specify.data.model.impl.Determination;
import se.nrm.specify.data.model.impl.Geography;
import se.nrm.specify.data.model.impl.Locality;
import se.nrm.specify.data.model.impl.Localitydetail;
import se.nrm.specify.data.model.impl.Morphbankview;
import se.nrm.specify.data.model.impl.Preparation;
import se.nrm.specify.data.model.impl.Preptype;
import se.nrm.specify.data.model.impl.Storage;
import se.nrm.specify.data.model.impl.Taxon;
import se.nrm.specify.data.process.logic.reflection.ReflectionHelper;
import se.nrm.specify.data.process.logic.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class EntityToJson implements Serializable {
    
    private final String associeradeMineralKey = "associeradeMineral";
    private final String accessionNumberKey = "accessionNumber";
//    private final String accessionRemarksKey = "accessionRemarks";
    private final String additionalDeterminationKey = "additionalDetermination";
    private final String associatedMediaKey = "associatedMedia";
    
    
    
    private final String authorKey = "scientificNameAuthorship";
     
    private final String catalogNumberKey = "catalogNumber"; 
    private final String catalogedDateKey = "catalogedDate";
    private final String catalogedYearKey = "catalogedYear";
    private final String catalogedMonthKey = "catalogedMonth"; 
    private final String collectorKey = "collectors";
    private final String countryKey = "country";
    private final String countyKey = "county";
    private final String continentKey = "continent";
    private final String createdDateKey = "catalogedDate";
    
    private final String coordinateUncertaintyInMetersKey = "coordinateUncertaintyInMeters";
    
    private final String dateIdentifiedKey = "dateIdentified";
    private final String dayKey = "day";
    
    private final String eventDateKey = "eventDate";
    private final String eventRemarksKey = "eventRemarks";
    private final String eventTimeKey = "eventTime";
    
    private final String familyKey = "family";
    private final String orderKey = "order";
    private final String classKey = "clazz";
    private final String phylumKey = "phylum";
    private final String kingdomKey = "kingdom";
    
    private final String fieldNumberKey = "fieldNumber";
    
//    private final String geohashKey = "geohash";
//    private final String geopointKey = "geopoint";
    private final String geoKey = "geo";
    private final String genusKey = "genus";
    
    private final String higherGeographyKey = "higherGeography";
    
    private final String higherClassificationKey = "higherClassification";
    
    private final String identifiedByKey = "identifiedBy";
    
    private final String individualCountKey = "individualCount";
    
    private final String islandKey = "island";
    
    private final String islandGroupKey = "islandGroup";
    private final String latitudeKey = "decimalLatitude";
    private final String longitudeKey = "decimalLongitude";
    private final String lifeStageKey = "lifeStage";
    private final String localityKey = "locality";
    private final String hasImageKey = "hasImage";
    
    private final String minElevationKey = "minimumElevationInMeters";
    private final String maxElevationKey = "maximumElevationInMeters";
    
    private final String geodeticDatumKey = "geodeticDatum";
    
    private final String modifiedKey = "modified";
    private final String monthKey = "month";
    
    private final String otherCatalogNumbersKey = "otherCatalogNumbers";
    private final String occurrenceAttributeRemarksKey  = "occurrenceAttributeRemarks";
    private final String occurrenceRemarksKey = "occurrenceRemarks";
    
//    private final String pointKey = "point"; 
//    private final String point1Key = "point-1"; 
//    private final String point01Key = "point-0.1"; 
//    private final String point001Key = "point-0.01"; 
//    private final String point0001Key = "point-0.001"; 
    private final String preparationsKey = "preparations";
    private final String prepartionCountKey = "prepCount";
    
    private final String previousIdentificationsKey = "previousIdentifications";
    
    private final String reproductiveConditionKey = "reproductiveCondition";
    
    private final String scientificNameKey = "scientificName";
    private final String serieKey = "serie";
    private final String startDayOfYearKey = "startDayOfYear";
    private final String endDayOfYearKey = "endDayOfYear";
    private final String stateProvinceKey = "stateProvince";
    private final String seKey = "se";
    private final String sexKey = "sex";
    private final String speciesKey = "species";
    private final String storageKey = "storage";
    private final String synonymsKey = "synonyms";
    private final String synonymAuthKey = "synonymAuthor";
    
    private final String taxonRankKey = "taxonRank";
    private final String typeStatusKey = "typeStatus";
    
    private final String verbatimEventDateKey = "verbatimEventDate";

//    private final String verbatimCoordinatesKey = "verbatimCoordinates";
    private final String vernacularNameKey = "vernacularName"; 
    
    private final String waterBodyKey = "waterBody";
    
    private final String yearKey = "year";
    
//    private final String doubleFormat1 = "%.1f";
//    private final String doubleFormat2 = "%.2f";
//    private final String doubleFormat3 = "%.3f";
//    
//    private final int numberOfCharacters = 5; 
    private final String comma = ",";
    private final String underScore = "_";
    private final String leftParentheses = "(";
    private final String rightParentheses = ")";
    private final String slash = "/";
    private final String space = " ";
    private final String pipe = " | ";
    private final String leftBracket = "[";
    private final String rightBrackets = "]";
    
    private final String emptyString = "";
    private final String lastCommaRex = ",$";
    
    
    private final String sweden = "sweden";
    private final String sverige = "Sverige";
    private final String sv = "sv";
    private final String strDet = " [det. ";


    private final String entomologyCollectionCode = "NHRS";
    private final String mineralogyCollection = "NRMMIN";
    
    private boolean isCollection;
    private Date eventDate;
    private LocalDate localEventDate;
    
    private Date endDate;
    private String strEventDate;
    
    private Date determinedDate;
    
    private LocalDate localDate;
    
    private BigDecimal latitude;
    private BigDecimal longitude;
    
    private String country;
    
//    private String formattedLat;
//    private String formattedLong;
//    private int intLat;
//    private int intLong;
//    private String format;
    
//    private String geoHash; 
    private StringBuilder coordinatesSb; 
//    private StringBuilder geoHashSb;
//    private StringBuilder pointSb; 
    private StringBuilder imageSb;
    private StringBuilder preparationsSb;
    private StringBuilder synonymAuthSb;
    private StringBuilder taxaSb;
     
    private JsonArrayBuilder taxaArrayBuilder;
    private JsonArrayBuilder addtionalDetArrayBuilder;

    
    private String morphBankId;
    private int morphBankImageId; 
    private String associatedMedia;
    
    private Collectionobjectattribute collectionObjectAttribute;
    
    private Storage storage;
    private Preptype preptype;
    private String preptypeName;
    private int preparationCount;
    
    private Set<Collectionobjectattachment> collectionobjectattachmentList; 
    private Determination currentDetermination;
    private Agent determiner;
    private Taxon currentTaxon; 
    private Taxon preferredTaxon;
    private Taxon addtionalTaxon;
    
    private String synonymFullName;
     
    private Set<Taxon> synonymList;
    private Set<Commonnametx> commonNameList;
    
    private String typeStatusName;
    private String typeStatus;
    private boolean isType;
    
    private StringBuilder waterbodySb;
    private StringBuilder isIslandSb;
    private StringBuilder islandGroupSb;
    
    private String waterbody;
    private String isIsland;
    private String islandGroup;
    
    private final String locationRemarksKey = "locationRemarks";
    
    private final String possibleType = "possibletype";
    private final String possibleTypeConvert = "Possible type";
    
    private JsonArrayBuilder collectorsArrayBuilder;
    private JsonArrayBuilder commonNameArrayBuilder;
//    private JsonArrayBuilder georHashArrayBuilder;
//    private JsonArrayBuilder pointArrayBuilder;
     
    private JsonArrayBuilder imageViewArrayBuilder;
    
    private JsonArrayBuilder synomysArrayBuilder;
    private JsonArrayBuilder synomyAuthorsArrayBuilder;
    
    
    public void addImages(JsonObjectBuilder attBuilder, Collectionobject collectionObject) {
        morphBankId = collectionObject.getReservedText();
 
        if (!StringUtils.isBlank(morphBankId)) {
             
            imageViewArrayBuilder = Json.createArrayBuilder(); 
            
            collectionobjectattachmentList = collectionObject.getCollectionobjectattachmentList();
            if(collectionobjectattachmentList != null && !collectionobjectattachmentList.isEmpty()) {
                
                collectionobjectattachmentList.stream()
                    .map(ca -> ca.getAttachment())
                    .filter(hasImageAttachmentecPredicate)
                    .map(a -> a.getAttachmentImageAttribute())
                    .forEach(attImg -> { 
                        morphBankImageId = attImg.getMBImageID();   
                        associatedMedia = buildMorphbankView(morphBankImageId, attImg.getMorphBankWiew());
                        if(!StringUtils.isBlank(associatedMedia)) {
                            attBuilder.add(hasImageKey, true);
                            imageViewArrayBuilder.add(associatedMedia); 
                        }  
                    }); 
                
                attBuilder.add(associatedMediaKey, imageViewArrayBuilder); 
            }  
        }
    }
    
     private String buildMorphbankView(int mbImageId, Morphbankview view) {
        if (view != null) {
            imageSb = new StringBuilder();
            imageSb.append(mbImageId);
            imageSb.append(space);
            imageSb.append(leftParentheses);

            if (!StringUtils.isBlank(view.getSex()) ) {
                imageSb.append(view.getSex());
                imageSb.append(pipe);
            }
            if (!StringUtils.isBlank(view.getDevelopmentState())) {
                imageSb.append(view.getDevelopmentState());
                imageSb.append(pipe);
            }
            if (!StringUtils.isBlank(view.getSpecimenPart()) ) {
                imageSb.append(view.getSpecimenPart());
                imageSb.append(pipe);
            }
            if (!StringUtils.isBlank(view.getViewAngle())) {
                imageSb.append(view.getViewAngle());
                imageSb.append(pipe);
            }
            if (!StringUtils.isBlank(view.getViewName())) {
                imageSb.append(view.getViewName());
                imageSb.append(pipe);
            }
            
            imageSb.append(rightParentheses);
            return imageSb.toString().trim(); 
        }
        return null;
    }
    
    private final Predicate<Attachment> hasImageAttachmentecPredicate
            = attachment -> attachment != null 
                    && attachment.getAttachmentImageAttribute() != null;
    
    
 
    public void convertEntityToJson1(JsonObjectBuilder attBuilder, 
            Collectionobject bean, String collectionCode) {
        log.info("catalogNumber : {}", bean.getCatalogNumber());
        
        addAttValue(attBuilder, catalogNumberKey, bean.getCatalogNumber());
 
        addAttValue(attBuilder, otherCatalogNumbersKey, bean.getAltCatalogNumber());
 
         
//        addCatalogedDate(attBuilder, (java.sql.Date) bean.getCatalogedDate());
        addAccession(attBuilder, bean.getAccession());
         
        addModifiedDate(attBuilder, 
                Util.getInstance().dateToString(bean.getTimestampModified()));
         
        addAttValue(attBuilder, createdDateKey, Util.getInstance().dateToString(bean.getTimestampCreated())); 
 
        addIntegerValue(attBuilder, individualCountKey, bean.getCountAmt()); 
   
        
        
        addAttValue(attBuilder, occurrenceRemarksKey, bean.getRemarks()); 
         
        
        collectionObjectAttribute = bean.getCollectionObjectAttribute();
        if(collectionObjectAttribute != null) {
            addAttValue(attBuilder,  occurrenceAttributeRemarksKey, collectionObjectAttribute.getRemarks());
            addAttValue(attBuilder, sexKey, collectionObjectAttribute.getText1());
            addAttValue(attBuilder, reproductiveConditionKey, collectionObjectAttribute.getText3());
            addAttValue(attBuilder, lifeStageKey, collectionObjectAttribute.getText4());
        }
          
        
        addCurrentDeterminations(attBuilder, bean.getDeterminationList());
          
        addCollectingEvent(attBuilder, bean.getCollectingEvent());
     
        addPreparations(attBuilder, bean.getPreparationList());
         
        if (collectionCode.equals(entomologyCollectionCode)) {
            addImages(attBuilder, bean);
        }
        if (collectionCode.equals(mineralogyCollection)) {
            addAttValue(attBuilder, serieKey, bean.getText1());
        } 
        
        addAdditionalDetermination(attBuilder, bean.getDeterminationList(),  
                collectionCode.equals(mineralogyCollection)); 
    }
    
  
    
    private void addAdditionalDetermination(JsonObjectBuilder attBuilder, Set<Determination> determinations, boolean isMineral) {
        taxaArrayBuilder = Json.createArrayBuilder();
        addtionalDetArrayBuilder = Json.createArrayBuilder();
        determinations.stream()
                .filter(d -> !d.getIsCurrent())
                .forEach(cd -> { 
                    taxaSb = new StringBuilder(); 
                    if (isMineral) {
                        preferredTaxon = cd.getPreferredTaxon(); 
                        if (preferredTaxon != null) { 
                            taxaSb.append(preferredTaxon.getFullName()); 
                            if (cd.getTypeStatusName() != null) {
                                taxaSb.append(space);
                                taxaSb.append(leftBracket);
                                taxaSb.append(cd.getTypeStatusName());
                                taxaSb.append(rightBrackets);
                            } 
                            taxaArrayBuilder.add(taxaSb.toString().trim()); 
                        }
                    } else {
                        addtionalTaxon = cd.getTaxon();
                        if (addtionalTaxon != null) { 
                            taxaSb.append(addtionalTaxon.getFullName());
                            if (addtionalTaxon.getAuthor() != null) {
                                taxaSb.append(space);
                                taxaSb.append(addtionalTaxon.getAuthor());
                            }
                            if (cd.getDeterminer() != null) {
                                
                                taxaSb.append(strDet);
                                taxaSb.append(cd.getDeterminer().getFullName());
                                if (cd.getDeterminedDate() != null) {
                                    taxaSb.append(comma);
                                    taxaSb.append(space); 
                                    taxaSb.append(cd.getDeterminedDate());
                                }
                                taxaSb.append(rightBrackets);
                            }
                            addtionalDetArrayBuilder.add(taxaSb.toString()); 
                        }
                    }
                });
        if(isMineral) {
            attBuilder.add(associeradeMineralKey, taxaArrayBuilder);
        } else { 
            log.info("addintional determanations");
//            attBuilder.add(additionalDeterminationKey, addtionalDetArrayBuilder);
            attBuilder.add(previousIdentificationsKey, addtionalDetArrayBuilder);
        } 
    }



    private void addCurrentDeterminations(JsonObjectBuilder attBuilder, Set<Determination> determinations) {

        isType = false;
        currentDetermination = determinations.stream()
                .filter(d -> d.getIsCurrent())
                .findFirst().orElse(null);
 
        if (currentDetermination != null) {
            determiner = currentDetermination.getDeterminer();
            if (determiner != null) {
                addAttValue(attBuilder, identifiedByKey, determiner.getFullName());
            }

            determinedDate = (java.sql.Date) currentDetermination.getDeterminedDate();
            if (determinedDate != null) {
                attBuilder.add(dateIdentifiedKey,
                        Util.getInstance().dateToString(determinedDate));
            }
             
            typeStatusName = currentDetermination.getTypeStatusName();
            if (!StringUtils.isBlank(typeStatusName)) {
                isType = true; 
                addAttValue(attBuilder, typeStatusKey,  
                        StringUtils.capitalize(typeStatusName).trim()); 
            }
            addTaxon(attBuilder, currentDetermination.getPreferredTaxon(),
                    currentDetermination.getTaxon(), isType );
        } 
//        addTaxa(attBuilder, determinations); 
    }
    
 
    private void addTaxon(JsonObjectBuilder attBuilder, Taxon preferredTaxon, Taxon taxon, boolean isType) {

        currentTaxon = isType ? taxon : preferredTaxon; 
        
        if (currentTaxon != null) {
            addAttValue(attBuilder, scientificNameKey, currentTaxon.getFullName());
            addAttValue(attBuilder, taxonRankKey, currentTaxon.getTaxonTreeDefItem().getName());
            addAttValue(attBuilder, authorKey, currentTaxon.getAuthor());

            if (isType) {
                if (!taxon.getTaxonID().equals(preferredTaxon.getTaxonID())) {
                    addAttValue(attBuilder, synonymsKey, preferredTaxon.getFullName());
                    synonymAuthSb = new StringBuilder();
                    synonymAuthSb.append(preferredTaxon.getFullName());
                    synonymAuthSb.append(space);
                    synonymAuthSb.append(preferredTaxon.getAuthor());

                    addAttValue(attBuilder, synonymAuthKey, synonymAuthSb.toString().trim());
                }
            } else {
                synonymList = currentTaxon.getSynomys();
                log.info("synonyms size : {}", synonymList.size());
                if (synonymList != null && !synonymList.isEmpty()) {
                    synomysArrayBuilder = Json.createArrayBuilder();
                    synomyAuthorsArrayBuilder = Json.createArrayBuilder();

                    synonymList.stream()
                            .collect(Collectors.toList())
                            .stream()
                            .forEach(synonym -> {
                                synonymAuthSb = new StringBuilder();
                                synonymFullName = synonym.getFullName(); 
                                synonymAuthSb.append(synonymFullName);
                                synonymAuthSb.append(space);
                                synonymAuthSb.append(synonym.getAuthor());

                                synomysArrayBuilder.add(synonymFullName);
                                synomyAuthorsArrayBuilder.add(synonymAuthSb.toString().trim());
                            });
                    attBuilder.add(synonymsKey, synomysArrayBuilder);
                    attBuilder.add(synonymAuthKey, synomyAuthorsArrayBuilder);
                }
            } 
            
            addCommonName(attBuilder); 
            addClassification(attBuilder); 
        }
    }
    
    
    private void addClassification(JsonObjectBuilder attBuilder) { 
        addAttValue(attBuilder, speciesKey, currentTaxon.getSpecies());
        addAttValue(attBuilder, genusKey, currentTaxon.getGenus());
        addAttValue(attBuilder, familyKey, currentTaxon.getFamily());
        addAttValue(attBuilder, orderKey, currentTaxon.getOrder());
        addAttValue(attBuilder, classKey, currentTaxon.getClazz());
        addAttValue(attBuilder, phylumKey, currentTaxon.getPhylum());
        addAttValue(attBuilder, kingdomKey, currentTaxon.getKingdom());
        addAttValue(attBuilder, higherClassificationKey, currentTaxon.getHighClassification());
    }


    private void addCommonName(JsonObjectBuilder attBuilder) { 
        commonNameList = currentTaxon.getCommonnametxList();
  
        log.info("addCommonName {}", commonNameList.size());
        if (commonNameList != null && !commonNameList.isEmpty()) {
            commonNameArrayBuilder = Json.createArrayBuilder();

            commonNameList.stream()
                    .collect(Collectors.toList()).stream()
                    .filter(cn -> cn.getLanguage().equals(sv))
                    .forEach(c -> {
                        commonNameArrayBuilder.add(c.getName());
                    });

            attBuilder.add(vernacularNameKey, commonNameArrayBuilder);
        }
    }
    
    
    private void addPreparations(JsonObjectBuilder attBuilder, Set<Preparation> preparations) {
        log.info("addPreparation");
        if (preparations != null && !preparations.isEmpty()) {
            preparations.stream()
                    .forEach(preparation -> { 
                        storage = preparation.getStorage();
                        if (storage != null) {
                            addAttValue(attBuilder, storageKey, storage.getFullName());  
                        }
 
                        preptype = preparation.getPrepType();  
                        if(preptype != null) {
                            preptypeName = preptype.getName(); 
                            addAttValue(attBuilder, preparationsKey, preptypeName); 
                            
                            preparationsSb = new StringBuilder();
                            preparationsSb.append(preptypeName);  
                            if(preparation.getCountAmt() != null) {
                                 preparationCount = preparation.getCountAmt();
                                 if (preparationCount > 0) {
                                    preparationsSb.append(leftParentheses);
                                    preparationsSb.append(preparationCount);
                                    preparationsSb.append(rightParentheses); 
                                }
                            }  
                            addAttValue(attBuilder, prepartionCountKey,
                                        preparationsSb.toString().trim());
                        } 
                        
                    });
        }
    }
    
    private void addCollectingEvent(JsonObjectBuilder attBuilder, Collectingevent event) {
        if (event != null) {
            log.info("event id : {}", event.getCollectingEventID());
            
            eventDate = (java.sql.Date) event.getStartDate();
            if (eventDate != null) {
                strEventDate = Util.getInstance().dateToString(eventDate);
                localEventDate = eventDate.toLocalDate(); 
                
                attBuilder.add(eventDateKey, strEventDate); 
                attBuilder.add(startDayOfYearKey, localEventDate.getDayOfYear());
                attBuilder.add(yearKey, localEventDate.getYear());
                attBuilder.add(monthKey, localEventDate.getMonthValue());
                attBuilder.add(dayKey, localEventDate.getDayOfMonth());
            }
            
            endDate = (java.sql.Date) event.getEndDate();
             
            if (endDate != null) { 
                attBuilder.add(endDayOfYearKey, (endDate.toLocalDate()).getDayOfYear()); 
            }  
            
            if(event.getStartTime() != null) {
                attBuilder.add(eventTimeKey, event.getStartTime());  
            }
             
            addAttValue(attBuilder, fieldNumberKey, event.getStationFieldNumber());
    
            addAttValue(attBuilder, eventRemarksKey, event.getRemarks());
            addAttValue(attBuilder, verbatimEventDateKey, event.getVerbatimDate());
 
            
            addLocality(attBuilder, event.getLocality());  
 
            
            addCollectors(attBuilder, event.getCollectorList());
            log.info("addCollectors added");
        }
    }
    
    
    private void addCollectors(JsonObjectBuilder attBuilder, Set<Collector> collectors) {
        if (collectors != null) {   
            if(collectors.size() > 1) {
                log.info("collectors : {}", collectors.size());
            }
            
            collectorsArrayBuilder = Json.createArrayBuilder();
            collectors.stream()
                    .map(Collector::getAgent) 
                    .forEach(agent -> {
                        collectorsArrayBuilder.add(agent.getFullName()); 
                    });
            attBuilder.add(collectorKey,collectorsArrayBuilder);
        }
    }
 
 
    private void addGeoData(JsonObjectBuilder attBuilder, double latitude, double longitude) throws Exception {
        log.info("addGeoData : {} -- {}", latitude, longitude);
          
        addCoordinates(attBuilder, latitude, longitude); 
//        addPoint(attBuilder, latitude, longitude);  
    }
    
    private void addCoordinates(JsonObjectBuilder attBuilder, double latitude, double longitude) {

        coordinatesSb = new StringBuilder();
        coordinatesSb.append(latitude);
        coordinatesSb.append(comma);
        coordinatesSb.append(longitude);
//        attBuilder.add(verbatimCoordinatesKey, coordinatesSb.toString().trim());
        attBuilder.add(geoKey, coordinatesSb.toString().trim());
    }
    
//    private void addPoint(JsonObjectBuilder attBuilder, int i, String key,
//            double latitude, double longtude) {
//        format = getDoubleFormat(i);
//        formattedLat = String.format(format, latitude);
//        formattedLong = String.format(format, longtude);
//
//        pointSb = new StringBuilder();
//        pointSb.append(formattedLat);
//        pointSb.append(comma);
//        pointSb.append(formattedLong);
//        attBuilder.add(key, pointSb.toString());
//    }
    
//    private void addPoint(JsonObjectBuilder attBuilder, double latitude, double longtude) {
//        log.info("addPoint : {} -- {}", latitude, longtude);
//         
//        intLat = (int) latitude;
//        intLong = (int) longtude;
//
//        pointSb = new StringBuilder(); 
//        pointSb.append(intLat);
//        pointSb.append(comma);
//        pointSb.append(intLong);
//        attBuilder.add(point1Key, pointSb.toString());
//         
//        addPoint(attBuilder, 1, point01Key, latitude, longtude);
//        addPoint(attBuilder, 2, point001Key, latitude, longtude);
//        addPoint(attBuilder, 3, point0001Key, latitude, longtude);
//    }
    
        
//    private String getDoubleFormat(int i) {
//        switch (i) {
//            case 1:
//                return doubleFormat1;
//            case 2:
//                return doubleFormat2;
//            default:
//                return doubleFormat3;
//        }
//    }
 
    
    private void addLocality(JsonObjectBuilder attBuilder, Locality locality) {
        log.info("aaddLocality");
        if (locality != null) {
            addAttValue(attBuilder, localityKey, locality.getLocalityName()); 
 
            latitude = locality.getLatitude1();
            longitude = locality.getLongitude1(); 
            
            if (latitude != null && longitude != null ) { 
                attBuilder.add(latitudeKey, latitude.doubleValue());
                attBuilder.add(longitudeKey, longitude.doubleValue());
                try {
                    addGeoData(attBuilder, latitude.doubleValue(), longitude.doubleValue());
                } catch (Exception ex) { 
                    log.error(ex.getMessage());
                }
            } 
            
            addAttValue(attBuilder, geodeticDatumKey, locality.getDatum());  
               
            addAttValue(attBuilder, minElevationKey, locality.getMinElevation()); 
            addAttValue(attBuilder, maxElevationKey, locality.getMaxElevation()); 
             
            addAttValue(attBuilder, locationRemarksKey, locality.getRemarks());  
            addAttValue(attBuilder, coordinateUncertaintyInMetersKey, locality.getLatLongAccuracy()); 
  
            addCollectionDetail(attBuilder, locality.getLocalitydetail1Collection());
          
            addGeography(attBuilder, locality.getGeography()); 
        }
    }
    
    private void addCollectionDetail(JsonObjectBuilder attBuilder, Set<Localitydetail> localityDetail) {
        if( localityDetail != null && !localityDetail.isEmpty()) {
            waterbodySb = new StringBuilder();
            isIslandSb = new StringBuilder();
            islandGroupSb = new StringBuilder();
            
            localityDetail.stream()
                    .forEach(detail -> { 
                        waterbody = detail.getWaterBody();
                        isIsland = detail.getIsland();
                        islandGroup = detail.getIslandGroup();
                        if( waterbody != null) {
                            waterbodySb.append(waterbody);
                            waterbodySb.append(comma);
                        }
                        
                        if( isIsland != null) {
                            isIslandSb.append(isIsland);
                            isIslandSb.append(comma);
                        }
                        
                        if( islandGroup != null) {
                            islandGroupSb.append(islandGroup);
                            islandGroupSb.append(comma);
                        }  
                    });
            addAttValue(attBuilder, waterBodyKey, (waterbodySb.toString()).replaceFirst(lastCommaRex, emptyString)); 
            addAttValue(attBuilder, islandKey, isIslandSb.toString().replaceFirst(lastCommaRex, emptyString)); 
            addAttValue(attBuilder, islandGroupKey, isIslandSb.toString().replaceFirst(lastCommaRex, emptyString)); 
        }  
    }
    
    private void addGeography(JsonObjectBuilder attBuilder, Geography geography) {
//        log.info("addGeography : {}", geography);
 
        if (geography != null) {
            country = geography.getCountry();
            if (country != null) {
                attBuilder.add(countryKey, country);
                if (country.equalsIgnoreCase(sweden)) { 
                    attBuilder.add(seKey, sverige);
                }
            }
            addAttValue(attBuilder, higherGeographyKey, geography.getFullName());
            addAttValue(attBuilder, stateProvinceKey, geography.getState());
            addAttValue(attBuilder, countyKey, geography.getCounty());
            addAttValue(attBuilder, continentKey, geography.getContinent());
        }
    }


     
    private void addAccession(JsonObjectBuilder attBuilder, Accession accession) { 
        if (accession != null) {
            addAttValue(attBuilder, accessionNumberKey, accession.getAccessionNumber()); 
        } 
    }
    
    private void addModifiedDate(JsonObjectBuilder attBuilder, String modifiedDate) {  
        addAttValue(attBuilder, modifiedKey, modifiedDate); 
    }
 
    
     
//    private void addCatalogedDate(JsonObjectBuilder attBuilder, Date catalogedDate) {
//
//        if (catalogedDate != null) { 
//            attBuilder.add(catalogedDateKey,
//                    Util.getInstance().dateToString(catalogedDate));
//             
//            localDate = catalogedDate.toLocalDate();
//            attBuilder.add(catalogedYearKey, localDate.getYear());
//            attBuilder.add(catalogedMonthKey, localDate.getMonth().name()); 
//        }
//    }
    
    public void addAttValue(JsonObjectBuilder attBuilder, String key, String value) {
        if(!StringUtils.isBlank(value)) {
            attBuilder.add(key, value); 
        }
    }
    
    public void addAttValue(JsonObjectBuilder attBuilder, String key, int value) { 
        attBuilder.add(key, value); 
    }
    
    public void addAttValue(JsonObjectBuilder attBuilder, String key, Double value) { 
        if(value != null) {
            attBuilder.add(key, value); 
        } 
    }
    
    public void addIntegerValue(JsonObjectBuilder attBuilder, String key, Integer value) { 
        if(value != null) {
            attBuilder.add(key, value); 
        } 
    }
    

    public Object getStringValueFromEntity(EntityBean bean, String field) {
        return ReflectionHelper.getInstance().getValueFromFieldOrMethod(bean, field);
    }
    
    
    

    public void convertEntityToJson1(JsonObjectBuilder builder, JsonObject json,
            EntityBean bean, Map<String, List<String>> map, boolean isCollection) {

        json.keySet().stream()
                .forEach(key -> {
                    if (JsonHelper.getInstance().isStringType(json.get(key).getValueType())) {
                        Object value = getStringValueFromEntity(bean, json.getString(key));
                        if (value != null) {
                            if (isCollection) {
                                List<String> list;
                                if (map.containsKey(key)) {
                                    list = map.get(key);
                                } else {
                                    list = new ArrayList();
                                }
                                list.add(Util.getInstance().convertDataValueToString(value));
                                map.put(key, list);
                            } else {
                                JsonHelper.getInstance().addAttributes(builder, key, value);
                            }
                        }
                    } else {
                        if (ReflectionHelper.getInstance().isCollection(bean.getClass(), key)) {
                            List<EntityBean> beans = ReflectionHelper.getInstance().getChildListFromParent(bean, key);
                            convertEntitiesToJson(builder, json.getJsonObject(key), beans, map);
                        } else {
                            EntityBean child = (EntityBean) ReflectionHelper.getInstance().getChildFromParent(bean, key);
                            if (child != null) {
                                convertEntityToJson1(builder, json.getJsonObject(key), child, map, isCollection);
                            }
                        }
                    }
                });
    }

    public void convertEntitiesToJson(JsonObjectBuilder builder, JsonObject json,
            List<EntityBean> beans, Map<String, List<String>> map) {
        isCollection = true;
        beans.stream()
                .forEach(bean -> {
                    convertEntityToJson1(builder, json, bean, map, isCollection);
                });
        map.keySet().stream()
                .forEach(key -> {
                    builder.add(key, String.join(", ", map.get(key)));
                });
        isCollection = false;
    }
}
