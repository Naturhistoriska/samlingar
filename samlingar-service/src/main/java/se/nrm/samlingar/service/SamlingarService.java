package se.nrm.samlingar.service;

import org.jboss.logging.Logger;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class SamlingarService {

    private static final Logger LOG = Logger.getLogger(SamlingarService.class);

    public String getInitalData() {
        LOG.info("getInitalData");
        return "getInitalData";
    }   
}
