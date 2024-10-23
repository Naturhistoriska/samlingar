package se.nrm.samlingar.api.solr;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import se.nrm.samlingar.api.logic.InitialProperties;

/**
 *
 * @author idali
 */
@ApplicationScoped 
@Startup
@Slf4j
public class SolrClientProducer implements Serializable {

    private SolrClient client;

    @Inject
    private InitialProperties properties;

    public SolrClientProducer() {

    }

    public SolrClientProducer(InitialProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        client = new HttpSolrClient.Builder(properties.getSolrURL()).build();
    }

    /**
     *
     * Produce CDI SolrClient
     *
     * @return SolrClient
     */
    @Produces
    @Solr
    public SolrClient getSolrClient() {
        return client;
    }

    /**
     * Close SolrClient when bean is destroyed
     */
    @PreDestroy
    public void preDestroy() {
        log.info("preDestroy - solrClient is closed");
        try {
            client.close();
        } catch (IOException ex) {
        }
    }
    
}
