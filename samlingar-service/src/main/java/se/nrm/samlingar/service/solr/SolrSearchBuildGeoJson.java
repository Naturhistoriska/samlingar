package se.nrm.samlingar.service.solr;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.SimpleOrderedMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SolrSearchBuildGeoJson {
    private static SolrSearchBuildGeoJson instance = null;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String COORDINATES_KEY = "coordinates";
    private static final String FACET_COUNT = "facet_counts";
    private static final String FACET_HEAT_MAP = "facet_heatmaps";

    private static final String FEATURE_COLLECTION_KEY = "FeatureCollection";

    private static final String COUNTS_INTS_2D = "counts_ints2D";
    private static final String COUNT_KEY = "count";
    private static final String GEOMETRY_KEY = "geometry";
    private static final String GEO_KEY = "geo";

    private static final String TYPE_KEY = "type";
    private static final String FEATURE_KEY = "Feature";
    private static final String POLYGON_KEY = "Polygon";
    private static final String ROWS_KEY = "rows";
    private static final String COLUMNS_KEY = "columns";

    private static final String MIN_X_KEY = "minX";
    private static final String MAX_X_KEY = "maxX";

    private static final String MIN_Y_KEY = "minY";
    private static final String MAX_Y_KEY = "maxY";

    private static final String PROPERTIES_KEY = "properties";
    private static final String FEATURES_KEY = "features";

    public static SolrSearchBuildGeoJson getInstance() {
        synchronized (SolrSearchBuildGeoJson.class) {
            if (instance == null) {
                instance = new SolrSearchBuildGeoJson();
            }
        }
        return instance;
    }

    public String buildGeoJson(QueryResponse resp) throws JsonProcessingException {
        SimpleOrderedMap<Object> facetCounts = (SimpleOrderedMap<Object>) resp.getResponse().get(FACET_COUNT);
        SimpleOrderedMap<Object> facetHeatmaps = (SimpleOrderedMap<Object>) facetCounts.get(FACET_HEAT_MAP);
        SimpleOrderedMap<Object> heatmap = (SimpleOrderedMap<Object>) facetHeatmaps.get(GEO_KEY);

        List<List<Integer>> counts = (List<List<Integer>>) heatmap.get(COUNTS_INTS_2D);

        List<Map<String, Object>>features = new ArrayList<>();
        byte[] responseBytes = null;
        if (counts != null) {
            int rows = (Integer) heatmap.get(ROWS_KEY);
            int cols = (Integer) heatmap.get(COLUMNS_KEY);
            double minX = ((Number) heatmap.get(MIN_X_KEY)).doubleValue();
            double maxX = ((Number) heatmap.get(MAX_X_KEY)).doubleValue();
            double minY = ((Number) heatmap.get(MIN_Y_KEY)).doubleValue();
            double maxY = ((Number) heatmap.get(MAX_Y_KEY)).doubleValue();

            double cellWidth = (maxX - minX) / cols;
            double cellHeight = (maxY - minY) / rows;

            Map<String, Object> geojson = new HashMap<>();
            geojson.put(TYPE_KEY, FEATURE_COLLECTION_KEY);

            for (int y = 0; y < counts.size(); y++) {
                List<Integer> rowList = counts.get(y);
                if (rowList == null) {
                    continue;
                }

                for (int x = 0; x < rowList.size(); x++) {
                    Integer count = rowList.get(x);
                    if (count == null || count == 0) {
                        continue;
                    }

                    double west = minX + x * cellWidth;
                    double east = west + cellWidth;
                    double north = maxY - y * cellHeight;
                    double south = north - cellHeight;

                    Map<String, Object> feature = new HashMap<>();
                    feature.put(TYPE_KEY, FEATURE_KEY);

                    Map<String, Object> geometry = new HashMap<>();
                    geometry.put(TYPE_KEY, POLYGON_KEY);

                    List<List<Double>> ring = new ArrayList<>();
                    ring.add(Arrays.asList(west, north));
                    ring.add(Arrays.asList(east, north));
                    ring.add(Arrays.asList(east, south));
                    ring.add(Arrays.asList(west, south));
                    ring.add(Arrays.asList(west, north)); // close the polygon

                    List<List<List<Double>>> coordinates = new ArrayList<>();
                    coordinates.add(ring);

                    geometry.put(COORDINATES_KEY, coordinates);

                    feature.put(GEOMETRY_KEY, geometry);

                    Map<String, Object> property = new HashMap<>();
                    property.put(COUNT_KEY, count);
                    feature.put(PROPERTIES_KEY, property);

                    features.add(feature);
                }
            }
            geojson.put(FEATURES_KEY, features);

            responseBytes = MAPPER.writeValueAsBytes(geojson);
        }

        return responseBytes == null ? "" : new String(responseBytes, StandardCharsets.UTF_8); 
    }
}
