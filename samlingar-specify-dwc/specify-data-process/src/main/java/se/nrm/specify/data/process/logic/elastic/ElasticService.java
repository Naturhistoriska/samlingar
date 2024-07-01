package se.nrm.specify.data.process.logic.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import se.nrm.specify.data.model.EntityBean;

/**
 *
 * @author idali
 * 
 */
@Slf4j
public class ElasticService {

    public ElasticService() {

    }

    public void postToElastic(List<EntityBean> beans) throws IOException {

        String serverUrl = "https://localhost:9200";

        RestClient restClient = RestClient
                .builder(HttpHost.create(serverUrl)) 
                .build();

// Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

// And create the API client
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        BulkRequest.Builder br = new BulkRequest.Builder();
        beans.stream()
                .forEach(bean -> {
                    br.operations(op -> op
                            .index(idx -> idx
                            .index("products")
                            .id(bean.getIdentityString())
                            .document(bean)
                            )
                    );
                });

        BulkResponse result = esClient.bulk(br.build());
    }
}
