package se.nrm.samlingar.service;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import se.nrm.samlingar.service.solr.SolrService;

@RequestScoped
public class SamlingarService {

    private static final Logger LOG = Logger.getLogger(SamlingarService.class);

    @Inject
    SolrService solr;

    public String getInitalData() {
        LOG.info("getInitalData");
        return solr.getInitalData();
    }
}
