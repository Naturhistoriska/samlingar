package se.nrm.specify.data.process.logic.json;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate; 
import java.util.Map;
import java.util.Set; 
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import se.nrm.specify.data.model.impl.Preparation;
import se.nrm.specify.data.model.impl.Preptype;
import se.nrm.specify.data.model.impl.Taxon;
import se.nrm.specify.data.process.logic.util.AttachmentPredicates;
import se.nrm.specify.data.process.logic.util.Util;

/**
 *
 * @author idali
 */
@Slf4j
public class EntityToJson implements Serializable {

    private final String associatedMediaKey = "associatedMedia";
    private final String associeradeMineralKey = "associeradeMineral";
    private final String accessionNumberKey = "accessionNumber";

    private final String authorKey = "scientificNameAuthorship";

    private final String catalogNumberKey = "catalogNumber";
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

    private final String fieldNumberKey = "fieldNumber";

    private final String geoKey = "geo";
    private final String latLongKey = "lat_long";

    private final String hasImageKey = "hasImage";
    private final String higherGeographyKey = "higherGeography";

    private final String identifiedByKey = "identifiedBy";

//    private final String iiifManifest = "https://assets.nrm.se/manifest/";
    private final String individualCountKey = "individualCount";

    private final String islandKey = "island";

    private final String islandGroupKey = "islandGroup";
    private final String latitudeKey = "decimalLatitude";
    private final String longitudeKey = "decimalLongitude";
    private final String lifeStageKey = "lifeStage";
    private final String localityKey = "locality";

    private final String minElevationKey = "minimumElevationInMeters";
    private final String maxElevationKey = "maximumElevationInMeters";

    private final String geodeticDatumKey = "geodeticDatum";

    private final String modifiedKey = "modified";
    private final String monthKey = "month";

    private final String endDayKey = "endday";
    private final String endMonthKey = "endmonth";
    private final String endYearKey = "endyear";

    private final String json = ".json";

    private final String otherCatalogNumbersKey = "otherCatalogNumbers";
    private final String occurrenceAttributeRemarksKey = "occurrenceAttributeRemarks";
    private final String occurrenceRemarksKey = "occurrenceRemarks";

    private final String preparationsKey = "preparations";
    private final String prepartionCountKey = "prepCount";

    private final String previousIdentificationsKey = "previousIdentifications";

    private final String reproductiveConditionKey = "reproductiveCondition";

    private final String scientificNameKey = "scientificName";
    private final String serieKey = "serie";
    private final String startDayOfYearKey = "startDayOfYear";
    private final String endDayOfYearKey = "endDayOfYear";
    private final String stateProvinceKey = "stateProvince";
    private final String sexKey = "sex";
    private final String synonymsKey = "synonyms";
    private final String synonymAuthKey = "synonymAuthor";

    private final String taxonRankKey = "taxonRank";
    private final String typeStatusKey = "typeStatus";

    private final String verbatimEventDateKey = "verbatimEventDate";

    private final String verbatimCoordinatesKey = "verbatimCoordinates";
    private final String vernacularNameKey = "vernacularName";

    private final String waterBodyKey = "waterBody";

    private final String yearKey = "year";

    private final String comma = ",";
    private final String leftParentheses = "(";
    private final String rightParentheses = ")";
    private final String slashWithEmptySpace = "/";
    private final String space = " ";
    private final String leftBracket = "[";
    private final String rightBrackets = "]";

    private final String emptyString = "";
    private final String lastCommaRex = ",$";

    private final String sv = "sv";
    private final String strDet = " [det. ";

//    private final String entomologyCollectionCode = "NHRS";
    private final String mineralogyCollection = "NRMMIN";

    private Date eventDate;
    private LocalDate localEventDate;

    private LocalDate endDate;
//    private String strEventDate;

    private Date determinedDate;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String country;

//    private String geoHash; 
    private StringBuilder coordinatesSb;
    private StringBuilder preparationsSb;
    private StringBuilder synonymAuthSb;
    private StringBuilder taxaSb;
    private StringBuilder verbatimCoordinatesSb;

    private JsonArrayBuilder taxaArrayBuilder;
    private JsonArrayBuilder addtionalDetArrayBuilder;

    private JsonArrayBuilder collectorsArrayBuilder;
    private JsonArrayBuilder commonNameArrayBuilder;

    private JsonArrayBuilder synomysArrayBuilder;
    private JsonArrayBuilder synomyAuthorsArrayBuilder;

    private JsonArrayBuilder preparationArrayBuilder;
    private JsonArrayBuilder prepCountArrayBuilder;

    private Collectionobjectattribute collectionObjectAttribute;

    private Preptype preptype;
    private String preptypeName;
    private int preparationCount;

    private Determination currentDetermination;
    private Agent determiner;
    private Taxon currentTaxon;
    private Taxon preferredTaxon;
    private Taxon addtionalTaxon;
    private Attachment attachment;

    private String synonymFullName;

    private Set<Taxon> synonymList;
    private Set<Commonnametx> commonNameList;

    private String typeStatusName;
//    private String typeStatus;
    private boolean isType;

    private boolean hasAnyAttachment;

    private StringBuilder associalMediaSb;
    private StringBuilder waterbodySb;
    private StringBuilder isIslandSb;
    private StringBuilder islandGroupSb;

    private String waterbody;
    private String isIsland;
    private String islandGroup;

    private final String locationRemarksKey = "locationRemarks";
//    
//    private final String possibleType = "possibletype";
//    private final String possibleTypeConvert = "Possible type";

    private Map<String, String> taxonMap;

    public void convertEntityToJson(JsonObjectBuilder attBuilder,
            Collectionobject bean, String collectionCode, String iiifManifest) {
        log.info("catalogNumber : {}", bean.getCatalogNumber());

        addAttValue(attBuilder, catalogNumberKey, bean.getCatalogNumber());
        addAttValue(attBuilder, otherCatalogNumbersKey, bean.getAltCatalogNumber());

        addAttValue(attBuilder, modifiedKey,
                Util.getInstance().dateToString(bean.getTimestampModified()));

        addAttValue(attBuilder, createdDateKey,
                Util.getInstance().dateToString(bean.getTimestampCreated()));

        addIntegerValue(attBuilder, individualCountKey, bean.getCountAmt());

        addAttValue(attBuilder, occurrenceRemarksKey, bean.getRemarks());

        collectionObjectAttribute = bean.getCollectionObjectAttribute();
        if (collectionObjectAttribute != null) {
            addAttValue(attBuilder, occurrenceAttributeRemarksKey, collectionObjectAttribute.getRemarks());

            if (!collectionCode.equals(mineralogyCollection)) {
                addAttValue(attBuilder, sexKey, collectionObjectAttribute.getText1());
                addAttValue(attBuilder, reproductiveConditionKey, collectionObjectAttribute.getText3());
                addAttValue(attBuilder, lifeStageKey, collectionObjectAttribute.getText4());
            }
        }

        if (collectionCode.equals(mineralogyCollection)) {
            addAttValue(attBuilder, serieKey, bean.getText1());
        }

        addAccession(attBuilder, bean.getAccession());
        addCollectingEvent(attBuilder, bean.getCollectingEvent());
        addPreparations(attBuilder, bean.getPreparationList());
        addCurrentDeterminations(attBuilder, bean.getDeterminationList());

        addAdditionalDetermination(attBuilder, bean.getDeterminationList(),
                collectionCode.equals(mineralogyCollection));

//        if (collectionCode.equals(entomologyCollectionCode)) {
//            addImages(attBuilder, bean.getCatalogNumber(),
//                    bean.getCollectionobjectattachmentList(), iiifManifest);
//        }
        
            addImages(attBuilder, bean.getCatalogNumber(),
                    bean.getCollectionobjectattachmentList(), iiifManifest);
 
    }

    private void addImages(JsonObjectBuilder attBuilder, String catalogNumber,
            Set<Collectionobjectattachment> collectionObjectAttachments, String iiifManifest) {

        if (collectionObjectAttachments != null && !collectionObjectAttachments.isEmpty()) {
  
            hasAnyAttachment = collectionObjectAttachments.stream()
                .anyMatch(AttachmentPredicates.validPublicAttachment());


            if (hasAnyAttachment) {
                attBuilder.add(hasImageKey, true);

                associalMediaSb = new StringBuilder();
                associalMediaSb.append(iiifManifest);
                associalMediaSb.append(catalogNumber);
                associalMediaSb.append(json);

                attBuilder.add(associatedMediaKey, associalMediaSb.toString().trim());
            }
        }
    }
 

    private void addAccession(JsonObjectBuilder attBuilder, Accession accession) {
        if (accession != null) {
            addAttValue(attBuilder, accessionNumberKey, accession.getAccessionNumber());
        }
    }

    private void addCollectingEvent(JsonObjectBuilder attBuilder, Collectingevent event) {
        if (event != null) {

            eventDate = (java.sql.Date) event.getStartDate();
            if (event.getStartDate() != null) {
                localEventDate = eventDate.toLocalDate();
                addAttValue(attBuilder, eventDateKey, eventDate.toString());

                attBuilder.add(startDayOfYearKey, localEventDate.getDayOfYear());
                attBuilder.add(yearKey, localEventDate.getYear());
                attBuilder.add(monthKey, localEventDate.getMonthValue());
                attBuilder.add(dayKey, localEventDate.getDayOfMonth());
            }
            if (event.getStartTime() != null) {
                attBuilder.add(eventTimeKey, event.getStartTime());
            }

            if (event.getEndDate() != null) {
                endDate = ((java.sql.Date) event.getEndDate()).toLocalDate();
                attBuilder.add(endDayOfYearKey, endDate.getDayOfYear());
                attBuilder.add(endDayKey, endDate.getDayOfMonth());
                attBuilder.add(endMonthKey, endDate.getMonthValue());
                attBuilder.add(endYearKey, endDate.getYear());
            }

            addAttValue(attBuilder, fieldNumberKey, event.getStationFieldNumber());
            addAttValue(attBuilder, eventRemarksKey, event.getRemarks());
            addAttValue(attBuilder, verbatimEventDateKey, event.getVerbatimDate());

            addLocality(attBuilder, event.getLocality());
            addCollectors(attBuilder, event.getCollectorList());
        }
    }

    private void addCollectors(JsonObjectBuilder attBuilder, Set<Collector> collectors) {
        if (collectors != null && collectors.size() >= 1) {
            collectorsArrayBuilder = Json.createArrayBuilder();
            collectors.stream()
                    .map(Collector::getAgent)
                    .forEach(agent -> {
                        collectorsArrayBuilder.add(agent.getFullName());
                    });
            attBuilder.add(collectorKey, collectorsArrayBuilder);
        }
    }

    private void addLocality(JsonObjectBuilder attBuilder, Locality locality) {
        log.info("aaddLocality");
        if (locality != null) {
            addAttValue(attBuilder, localityKey, locality.getLocalityName());

            try {
                addGeoData(attBuilder, locality);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
            addAttValue(attBuilder, geodeticDatumKey, locality.getDatum());

            addAttValue(attBuilder, minElevationKey, locality.getMinElevation());
            addAttValue(attBuilder, maxElevationKey, locality.getMaxElevation());

            addAttValue(attBuilder, locationRemarksKey, locality.getRemarks());
            addAttValue(attBuilder, coordinateUncertaintyInMetersKey, locality.getLatLongAccuracy());

            addLocalityDetail(attBuilder, locality.getLocalitydetail1Collection());

            addGeography(attBuilder, locality.getGeography());
        }
    }

    private void addGeoData(JsonObjectBuilder attBuilder, Locality locality) {
        log.info("addGeoData ");

        latitude = locality.getLatitude1();
        longitude = locality.getLongitude1();

        if (latitude != null && longitude != null) {
            attBuilder.add(latitudeKey, latitude.doubleValue());
            attBuilder.add(longitudeKey, longitude.doubleValue());
            addCoordinates(attBuilder, latitude.doubleValue(), longitude.doubleValue());

            addVerbatimCoordinates(attBuilder, locality.getLat1Text(), locality.getLong1Text());
        }
    }

    private void addCoordinates(JsonObjectBuilder attBuilder, double latitude, double longitude) {
        coordinatesSb = new StringBuilder();
        coordinatesSb.append(latitude);
        coordinatesSb.append(comma);
        coordinatesSb.append(longitude);
        attBuilder.add(geoKey, coordinatesSb.toString().trim());
        attBuilder.add(latLongKey, coordinatesSb.toString().trim());
    }

    private void addVerbatimCoordinates(JsonObjectBuilder attBuilder, String latText, String lngText) {
        if (latText != null && lngText != null) {
            verbatimCoordinatesSb = new StringBuilder();
            verbatimCoordinatesSb.append(latText);
            verbatimCoordinatesSb.append(slashWithEmptySpace);
            verbatimCoordinatesSb.append(lngText);
            attBuilder.add(verbatimCoordinatesKey, verbatimCoordinatesSb.toString().trim());
        }
    }

    private void addLocalityDetail(JsonObjectBuilder attBuilder, Set<Localitydetail> localityDetail) {
        if (localityDetail != null && !localityDetail.isEmpty()) {
            waterbodySb = new StringBuilder();
            isIslandSb = new StringBuilder();
            islandGroupSb = new StringBuilder();

            localityDetail.stream()
                    .forEach(detail -> {
                        waterbody = detail.getWaterBody();
                        isIsland = detail.getIsland();
                        islandGroup = detail.getIslandGroup();
                        if (waterbody != null) {
                            waterbodySb.append(waterbody);
                            waterbodySb.append(comma);
                        }

                        if (isIsland != null) {
                            isIslandSb.append(isIsland);
                            isIslandSb.append(comma);
                        }

                        if (islandGroup != null) {
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
            }
            addAttValue(attBuilder, higherGeographyKey, geography.getFullName());
            addAttValue(attBuilder, stateProvinceKey, geography.getState());
            addAttValue(attBuilder, countyKey, geography.getCounty());
            addAttValue(attBuilder, continentKey, geography.getContinent());
        }
    }

    private void addPreparations(JsonObjectBuilder attBuilder, Set<Preparation> preparations) {
//        log.info("addPreparation");
        if (preparations != null && !preparations.isEmpty()) {
            preparationArrayBuilder = Json.createArrayBuilder();
            prepCountArrayBuilder = Json.createArrayBuilder();
            preparations.stream()
                    .forEach(preparation -> {
                        preptype = preparation.getPrepType();
                        if (preptype != null) {
                            preptypeName = preptype.getName();
                            preparationArrayBuilder.add(preptypeName);

                            preparationsSb = new StringBuilder();
                            preparationsSb.append(preptypeName);
                            if (preparation.getCountAmt() != null) {
                                preparationCount = preparation.getCountAmt();
                                if (preparationCount > 0) {
                                    preparationsSb.append(leftParentheses);
                                    preparationsSb.append(preparationCount);
                                    preparationsSb.append(rightParentheses);
                                }
                            }
                            prepCountArrayBuilder.add(preparationsSb.toString().trim());
                        }
                    });
            attBuilder.add(preparationsKey, preparationArrayBuilder);
            attBuilder.add(prepartionCountKey, prepCountArrayBuilder);
        }
    }

    private void addCurrentDeterminations(JsonObjectBuilder attBuilder,
            Set<Determination> determinations) {

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
                    currentDetermination.getTaxon(), isType);
        }
//        addTaxa(attBuilder, determinations); 
    }

    private void addTaxon(JsonObjectBuilder attBuilder, Taxon preferredTaxon,
            Taxon taxon, boolean isType) {
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
//                log.info("synonyms size : {}", synonymList.size());
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

    private void addCommonName(JsonObjectBuilder attBuilder) {
        commonNameList = currentTaxon.getCommonnametxList();

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

    private void addClassification(JsonObjectBuilder attBuilder) {

        taxonMap = currentTaxon.getTaxon();
        taxonMap.entrySet().stream()
                .forEach(entry -> {
                    addAttValue(attBuilder, entry.getKey(), entry.getValue());
                });
    }

    private void addAdditionalDetermination(JsonObjectBuilder attBuilder, Set<Determination> determinations, boolean isMineral) {
        taxaArrayBuilder = Json.createArrayBuilder();
        addtionalDetArrayBuilder = Json.createArrayBuilder();
        determinations.stream()
                .filter(d -> !d.getIsCurrent())
                .forEach(ad -> {
                    taxaSb = new StringBuilder();
                    if (isMineral) {
                        preferredTaxon = ad.getPreferredTaxon();
                        if (preferredTaxon != null) {
                            taxaSb.append(preferredTaxon.getFullName());
                            if (ad.getTypeStatusName() != null) {
                                taxaSb.append(space);
                                taxaSb.append(leftBracket);
                                taxaSb.append(ad.getTypeStatusName());
                                taxaSb.append(rightBrackets);
                            }
                            taxaArrayBuilder.add(taxaSb.toString().trim());
                        }
                    } else {
                        addtionalTaxon = ad.getTaxon();
                        if (addtionalTaxon != null) {
                            taxaSb.append(addtionalTaxon.getFullName());
                            if (addtionalTaxon.getAuthor() != null) {
                                taxaSb.append(space);
                                taxaSb.append(addtionalTaxon.getAuthor());
                            }
                            if (ad.getDeterminer() != null) {
                                taxaSb.append(strDet);
                                taxaSb.append(ad.getDeterminer().getFullName());
                                if (ad.getDeterminedDate() != null) {
                                    taxaSb.append(comma);
                                    taxaSb.append(space);
                                    taxaSb.append(ad.getDeterminedDate());
                                }
                                taxaSb.append(rightBrackets);
                            }
                            addtionalDetArrayBuilder.add(taxaSb.toString());
                        }
                    }
                });
        if (isMineral) {
            attBuilder.add(associeradeMineralKey, taxaArrayBuilder);
        } else {
            log.info("addintional determanations");
//            attBuilder.add(additionalDeterminationKey, addtionalDetArrayBuilder);
            attBuilder.add(previousIdentificationsKey, addtionalDetArrayBuilder);
        }
    }

    public void addAttValue(JsonObjectBuilder attBuilder, String key, String value) {
        if (!StringUtils.isBlank(value)) {
            attBuilder.add(key, value);
        }
    }

    public void addAttValue(JsonObjectBuilder attBuilder, String key, int value) {
        attBuilder.add(key, value);
    }

    public void addAttValue(JsonObjectBuilder attBuilder, String key, Double value) {
        if (value != null) {
            attBuilder.add(key, value);
        }
    }

    public void addIntegerValue(JsonObjectBuilder attBuilder, String key, Integer value) {
        if (value != null) {
            attBuilder.add(key, value);
        }
    }

}
