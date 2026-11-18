package se.nrm.samlingar.service.solr;

import java.util.concurrent.TimeUnit;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.jetty.HttpJettySolrClient;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

public class SolrClientProducer {

    @Produces
    @Singleton
    SolrClient solrClient(SolrConfig config) {
        return new HttpJettySolrClient.Builder(config.baseUrl())
            .withDefaultCollection(config.collection())
            .withBasicAuthCredentials(config.username(), config.password())
            .withConnectionTimeout(config.connectionTimeout().toMillis(), TimeUnit.MILLISECONDS)
            .withRequestTimeout(config.requestTimeout().toMillis(), TimeUnit.MILLISECONDS)            
            .build();
    }
}
