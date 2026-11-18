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

    private final ObjectMapper mapper = new ObjectMapper();

    private final String coordinatesKey = "coordinates";
    private final String facetCount = "facet_counts";
    private final String facetHeatMap = "facet_heatmaps";

    private final String featureCollectionKey = "FeatureCollection";

    private final String countsInts2D = "counts_ints2D";
    private final String countKey = "count";
    private final String geometryKey = "geometry";
    private final String geoKey = "geo";

    private final String typeKey = "type";
    private final String featureKey = "Feature";
    private final String polygonKey = "Polygon";
    private final String rowsKey = "rows";
    private final String columnsKey = "columns";

    private final String minXKey = "minX";
    private final String maxXKey = "maxX";

    private final String minYKey = "minY";
    private final String maxYKey = "maxY";

    private final String propertiesKey = "properties";
    private final String featuresKey = "features";
    
    private final String emptyString = "";

    public static SolrSearchBuildGeoJson getInstance() {
        synchronized (SolrSearchBuildGeoJson.class) {
            if (instance == null) {
                instance = new SolrSearchBuildGeoJson();
            }
        }
        return instance;
    }

    public String buildGeoJson(QueryResponse resp) throws JsonProcessingException {

        SimpleOrderedMap<Object> facetCounts = (SimpleOrderedMap<Object>) resp.getResponse().get(facetCount);
        SimpleOrderedMap<Object> facetHeatmaps = (SimpleOrderedMap<Object>) facetCounts.get(facetHeatMap);
        SimpleOrderedMap<Object> heatmap = (SimpleOrderedMap<Object>) facetHeatmaps.get(geoKey);

        List<List<Integer>> counts = (List<List<Integer>>) heatmap.get(countsInts2D);

        List<Map<String, Object>>features = new ArrayList<>();
        byte[] responseBytes = null;
        if (counts != null) {
            int rows = (Integer) heatmap.get(rowsKey);
            int cols = (Integer) heatmap.get(columnsKey);
            double minX = ((Number) heatmap.get(minXKey)).doubleValue();
            double maxX = ((Number) heatmap.get(maxXKey)).doubleValue();
            double minY = ((Number) heatmap.get(minYKey)).doubleValue();
            double maxY = ((Number) heatmap.get(maxYKey)).doubleValue();

            double cellWidth = (maxX - minX) / cols;
            double cellHeight = (maxY - minY) / rows;

            Map<String, Object> geojson = new HashMap<>();
            geojson.put(typeKey, featureCollectionKey);

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
                    feature.put(typeKey, featureKey);

                    Map<String, Object> geometry = new HashMap<>();
                    geometry.put(typeKey, polygonKey);

                    List<List<Double>> ring = new ArrayList<>();
                    ring.add(Arrays.asList(west, north));
                    ring.add(Arrays.asList(east, north));
                    ring.add(Arrays.asList(east, south));
                    ring.add(Arrays.asList(west, south));
                    ring.add(Arrays.asList(west, north)); // close the polygon

                    List<List<List<Double>>> coordinates = new ArrayList<>();
                    coordinates.add(ring);

                    geometry.put(coordinatesKey, coordinates);

                    feature.put(geometryKey, geometry);

                    Map<String, Object> property = new HashMap<>();
                    property.put(countKey, count);
                    feature.put(propertiesKey, property);

                    features.add(feature);
                }
            }
            geojson.put(featuresKey, features);

            responseBytes = mapper.writeValueAsBytes(geojson);
        }

        return responseBytes == null ? emptyString : new String(responseBytes, StandardCharsets.UTF_8); 
    }
}
