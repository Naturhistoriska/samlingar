package se.nrm.samlingar.api.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author idali
 */
public class ExportFields {

    private static final String ASSOCIERAD_MINERAL_KEY = "associeradeMineral";
    private static final String BASIS_OF_RECORD_KEY = "basisOfRecord";
    private static final String CATALOG_NUMBER_KEY = "catalogNumber";
    private static final String COLLECTION_NAME_KEY = "collectionName";
    private static final String EXPEDITION_NAME_KEY = "expeditionName";
    private static final String PRESERVATION_KEY = "preservation";
    private static final String PREPARATIONS_KEY = "preparations";
    private static final String LIFE_STAGE_KEY = "lifeStage";
    private static final String INDIVIDUAL_COUNT_KEY = "individualCount";
    private static final String OCCURRENCE_REMARKS_KEY = "occurrenceRemarks";
    private static final String SEX_KEY = "sex";
    private static final String COUNTRY_KEY = "country";
    private static final String COUNTY_KEY = "county";
    private static final String ADDITIONAL_DETERMINATION_KEY = "additionalDetermination";
    private static final String DATE_IDENTIFIED_KEY = "dateIdentified";
    private static final String DISTRICT_KEY = "district";
    private static final String DESCRIPTION_KEY = "description";
    private static final String COLLECTORS_KEY = "collectors";
    private static final String ELEMENT_KEY = "element";
    private static final String END_DAY_KEEY = "endday";
    private static final String END_MONTH_KEY = "endmonth";
    private static final String END_YEAR_KEY = "endyear";
    private static final String EXSICCATE = "exsiccate";
    private static final String EARLIES_AGE_OR_LOWEST_STAGE_KEY = "earliestAgeOrLowestStage";
    private static final String EARLIEST_EPOCH_OR_LOWEST_SERIES_KEY = "earliestEpochOrLowestSeries";
    private static final String EARLIEST_PEERIOD_OR_LOWEST_SYSTEM_KEY = "earliestPeriodOrLowestSystem";
    private static final String HERBARIUM_KEY = "herbarium";
    private static final String IDENTIFIED_BY_KEY = "identifiedBy";
    private static final String IDENTIFIED_YEAR = "identifiedYear";
    private static final String IDENTIFICATION_QUALIFIER_KEY = "identificationQualifier";
    private static final String INFRASPECIFIC_EPITHET_KEY = "infraspecificEpithet";
    private static final String INFRAGENERIC_EPITHET_KEY = "infragenericEpithet";
    private static final String ISLAND_KEY = "island";
    private static final String ISLAND_GROUP_KEY = "islandGroup";
    private static final String LATEST_AGE_OR_HIGHEST_STAGE_KEY = "latestAgeOrHighestStage";
    private static final String LATEST_EPOCH_OR_HIGHEST_SERIES_KEY = "latestEpochOrHighestSeries";
    private static final String LATEST_PERIOD_OR_HIGHEST_SYSTEM_KEY = "latestPeriodOrHighestSystem";
    private static final String LITHOSTRATINGRAPHIC_TERMS_KEY = "lithostratigraphicTerms";
    private static final String ORIGGINALNAME_BASIONYM_KEY = "originalnameBasionym";
    private static final String ORIGINAL_NAME_USAGE_KEY = "originalNameUsage";
    private static final String PRESERVED_SPECIMEN_KEY = "preservedSpecimen";
    private static final String PREVIOUS_IDENTIFICATION_KEY = "previousIdentifications";
    private static final String RECORD_NUMNER_KEY = "recordNumber";
    private static final String SERIE_KEY = "serie";
    private static final String STATION_NUMBER_KEY = "stationNumber";
    private static final String SYNONYMS_KEY = "synonyms";

    private static final String DAY_KEY = "day";
    private static final String MONTH_KEY = "month";
    private static final String YEAR_KEY = "year";

    private static final String STATE_PROVINCE_KEY = "stateProvince";
    private static final String CONTINENT_KEY = "continent";
    private static final String DECIMAL_LONGITUDE_KEY = "decimalLongitude";
    private static final String DECIMAL_LATITUDE_KEY = "decimalLatitude";
    private static final String LOCALITY_KEY = "locality";
    private static final String MIN_ELEVATION_KEY = "minimumElevationInMeters";
    private static final String MAX_ELEVATION_KEY = "maximumElevationInMeters";
    private static final String MIN_DEPTH_KEY = "minimumDepthInMeters";
    private static final String MAX_DEPTH_KEY = "maximumDepthInMeters";
    private static final String WATER_BODY_KEY = "waterBody";

    private static final String FIELD_NUMBER_KEY = "fieldNumber";
    private static final String HABITAT_KEY = "habitat";
    private static final String TYPE_STATUS_KEY = "typeStatus";
    private static final String KINGDOM_KEY = "kingdom";
    private static final String PHYLUM_KEY = "phylum";
    private static final String CLASS_KEY = "clazz";
    private static final String ORDER_KEY = "order";
    private static final String FAMILY_KEY = "family";
    private static final String GENUS_KEY = "genus";
    private static final String SUBGENUS_KEY = "subgenus";
    private static final String SPECIES_KEY = "species";
    private static final String SCIENTIFIC_NAME_KEY = "scientificName";
    private static final String SCIENTIFIC_NAME_AUTHORSHIP_KEY = "scientificNameAuthorship";
    private static final String SPECIFIC_EPITHET_KEY = "specificEpithet";
    private static final String VERNACULAR_NAME_KEY = "vernacularName";
     
//    private static ExportFields instance = null;
//
//    public static ExportFields getInstance() {
//        synchronized (ExportFields.class) {
//            if (instance == null) {
//                instance = new ExportFields();
//            }
//        }
//        return instance;
//    }
//    

    public static final List<String> BUILD_DATA_EXPORT_FIELDS
            = Collections.unmodifiableList(Arrays.asList(
                    BASIS_OF_RECORD_KEY,
                    COLLECTION_NAME_KEY,
                    INDIVIDUAL_COUNT_KEY,
                    CATALOG_NUMBER_KEY,
                    COLLECTORS_KEY,
                    EXPEDITION_NAME_KEY,
                    PRESERVATION_KEY,
                    PREPARATIONS_KEY,
                    RECORD_NUMNER_KEY,
                    COLLECTORS_KEY,
                    CONTINENT_KEY,
                    COUNTRY_KEY,
                    STATE_PROVINCE_KEY,
                    COUNTY_KEY,
                    DISTRICT_KEY,
                    DECIMAL_LONGITUDE_KEY,
                    DECIMAL_LATITUDE_KEY,
                    LOCALITY_KEY,
                    WATER_BODY_KEY,
                    ISLAND_KEY,
                    ISLAND_GROUP_KEY,
                    STATION_NUMBER_KEY,
                    FIELD_NUMBER_KEY,
                    DAY_KEY,
                    MONTH_KEY,
                    YEAR_KEY,
                    END_DAY_KEEY,
                    END_MONTH_KEY,
                    END_YEAR_KEY,
                    LIFE_STAGE_KEY,
                    SEX_KEY,
                    MIN_ELEVATION_KEY,
                    MAX_ELEVATION_KEY,
                    MIN_DEPTH_KEY,
                    MAX_DEPTH_KEY,
                    EARLIES_AGE_OR_LOWEST_STAGE_KEY,
                    EARLIEST_EPOCH_OR_LOWEST_SERIES_KEY,
                    EARLIEST_PEERIOD_OR_LOWEST_SYSTEM_KEY,
                    LATEST_AGE_OR_HIGHEST_STAGE_KEY,
                    LATEST_EPOCH_OR_HIGHEST_SERIES_KEY,
                    LATEST_PERIOD_OR_HIGHEST_SYSTEM_KEY,
                    KINGDOM_KEY,
                    PHYLUM_KEY,
                    CLASS_KEY,
                    ORDER_KEY,
                    FAMILY_KEY,
                    GENUS_KEY,
                    SUBGENUS_KEY,
                    SPECIES_KEY,
                    SCIENTIFIC_NAME_KEY,
                    SCIENTIFIC_NAME_AUTHORSHIP_KEY,
                    SPECIFIC_EPITHET_KEY,
                    INFRASPECIFIC_EPITHET_KEY,
                    INFRAGENERIC_EPITHET_KEY,
                    HABITAT_KEY,
                    SYNONYMS_KEY,
                    TYPE_STATUS_KEY,
                    VERNACULAR_NAME_KEY,
                    ORIGINAL_NAME_USAGE_KEY,
                    PREVIOUS_IDENTIFICATION_KEY,
                    DATE_IDENTIFIED_KEY,
                    IDENTIFIED_BY_KEY,
                    IDENTIFIED_YEAR,
                    IDENTIFICATION_QUALIFIER_KEY,
                    ADDITIONAL_DETERMINATION_KEY,
                    LITHOSTRATINGRAPHIC_TERMS_KEY,
                    PRESERVED_SPECIMEN_KEY,
                    DESCRIPTION_KEY,
                    HERBARIUM_KEY,
                    EXSICCATE,
                    ELEMENT_KEY,
                    ORIGGINALNAME_BASIONYM_KEY,
                    ASSOCIERAD_MINERAL_KEY,
                    SERIE_KEY,
                    OCCURRENCE_REMARKS_KEY
            ));

}
