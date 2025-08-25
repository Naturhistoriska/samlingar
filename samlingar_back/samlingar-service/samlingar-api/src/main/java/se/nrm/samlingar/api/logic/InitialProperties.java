package se.nrm.samlingar.api.logic;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Slf4j
public class InitialProperties implements Serializable {
    private final static String CONFIG_INITIALLISING_ERROR = "Property not initialised";
    
    private String solrPath;
    private String solrCore;  
    private String username;
    private String password;

    private final String slash = "/";
    
    public InitialProperties() {
    }

    @Inject
    public InitialProperties(@ConfigurationValue("swarm.solr.path") String solrPath,
            @ConfigurationValue("swarm.solr.core") String solrCore,    
            @ConfigurationValue("swarm.solr.username") String username,
            @ConfigurationValue("swarm.solr.password") String password) {
        this.solrPath = solrPath;
        this.solrCore = solrCore; 
        this.username = username;
        this.password = password; 
    }

    public String getSolrPath() {
        if (solrPath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return solrPath;
    }

    public String getSolrURL() {
        if (solrPath == null || solrCore == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return solrPath + slash + solrCore;
    } 
    
    public String getCore() {
        if (solrCore == null ) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return solrCore;
    }
 
    public String getUsername() {
        if (username == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return username;
    }
    
    public String getPassword() {
        if (password == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return password;
    }
}
