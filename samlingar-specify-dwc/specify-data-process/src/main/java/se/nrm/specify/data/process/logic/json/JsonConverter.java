package se.nrm.specify.data.process.logic.json;

import java.io.Serializable; 
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder; 
import javax.json.JsonObjectBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils; 
import se.nrm.specify.data.model.impl.Collection;
import se.nrm.specify.data.model.impl.Collectionobject;
import se.nrm.specify.data.model.impl.Institution; 

/**
 *
 * @author idali
 */
@Slf4j
public class JsonConverter implements Serializable {

    private final String idKey = "id";

    private final String collectionCodeKey = "collectionCode";
    private final String collectionNameKey = "collectionName";
    private final String basisOfRecordKey = "basisOfRecord";

    private final String licenseKey = "license";
    private final String accessRightsKey = "accessRights";
    private final String institutionIDKey = "institutionID";
    private final String institutionCodeKey = "institutionCode";

    @Inject
    private EntityToJson entityToJson;

    private JsonArrayBuilder arrayBuilder;
    private JsonObjectBuilder attBuilder;

    private final String underScore = "_";

    private Map<String, List<String>> map;

    private final String filterKey = "filters";

    private String collectionName;
    private String collectionCode;
    private String collectionType;
    private String copyright;
    private String altName;
    private String code;

    private StringBuilder idSb;

    public JsonConverter() {

    }

    public JsonConverter(EntityToJson entityJson) {
        this.entityToJson = entityJson;
    }

    public JsonArray convert(List<Collectionobject> beans, Institution institution, Collection collection) {
        log.info("convert : {}", beans.size());

        arrayBuilder = Json.createArrayBuilder();
        AtomicInteger counter = new AtomicInteger(0);

        collectionName = collection.getCollectionName();
        collectionCode = collection.getCode();
        collectionType = collection.getCollectionType();

        copyright = institution.getCopyright();
        altName = institution.getAltName();
        code = institution.getCode();

        beans.stream()
                .forEach(bean -> {
                    counter.getAndIncrement();
                    attBuilder = Json.createObjectBuilder();

                    addCollection();
                    addInstitution();

                    attBuilder.add(idKey, addId(bean.getCollectionObjectID()));
                    entityToJson.convertEntityToJson1(attBuilder, bean, collectionCode);
                    arrayBuilder.add(attBuilder);
                });

        return arrayBuilder.build();
    }

    private String addId(int id) {
//        log.info("id : {}", id);
        idSb = new StringBuilder();
        idSb.append(collectionCode);
        idSb.append(underScore);
        idSb.append(id);
        return idSb.toString().trim();
    }

    private void addInstitution() {
        attBuilder.add(licenseKey, copyright);
        attBuilder.add(institutionIDKey, altName);
        attBuilder.add(institutionCodeKey, code);
    }

    private void addCollection() {
        attBuilder.add(collectionCodeKey, collectionCode);
        attBuilder.add(collectionNameKey, collectionName);

        if (!StringUtils.isBlank(collectionType)) {
            attBuilder.add(basisOfRecordKey, collectionType);
        }

    }

//    public JsonArray convert1(List<EntityBean> beans, JsonObject mappingJson) {
//
//        arrayBuilder = Json.createArrayBuilder();
//        attBuilder = Json.createObjectBuilder();
//
//        beans.stream()
//                .forEach(bean -> {
//                    JsonHelper.getInstance().addId(attBuilder, bean.getEntityId());
//                    map = new HashMap<>();
//                    mappingJson.keySet().stream()
//                            .filter(key -> !key.equals(filterKey))
//                            .forEach(key -> {
//                                if (JsonHelper.getInstance().isStringType(mappingJson.get(key).getValueType())) {
//                                    Object value = entityToJson.getStringValueFromEntity(bean, mappingJson.getString(key));
//                                    if (value != null) {
//                                        JsonHelper.getInstance().addAttributes(attBuilder, key, value);
//                                    }
//                                } else {
//                                    if (ReflectionHelper.getInstance().isCollection(bean.getClass(), key)) {
//                                        List<EntityBean> list = ReflectionHelper.getInstance().getChildListFromParent(bean, key);
//                                        entityToJson.convertEntitiesToJson(attBuilder, mappingJson.getJsonObject(key), list, map);
//                                    } else {
//                                        EntityBean child = (EntityBean) ReflectionHelper.getInstance().getChildFromParent(bean, key);
//                                        if (child != null) {
//                                            entityToJson.convertEntityToJson1(attBuilder, mappingJson.getJsonObject(key), child, map, false);
//                                        }
//                                    }
//                                }
//                            });
//                    arrayBuilder.add(attBuilder);
//                });
//        return arrayBuilder.build();
//    }

}
