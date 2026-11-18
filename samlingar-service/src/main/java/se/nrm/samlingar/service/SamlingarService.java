package se.nrm.samlingar.service;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import se.nrm.samlingar.service.solr.SolrService;

@ApplicationScoped
public class SamlingarService {

    private static final Logger LOG = Logger.getLogger(SamlingarService.class);

    private final SolrService solr;

    public SamlingarService(SolrService solr) {
        LOG.info("construct");
        this.solr = solr;
    }

    public String initalData() {
        LOG.info("initalData");
        return solr.initalData();
    }
}
