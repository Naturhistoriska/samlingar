package se.nrm.specify.data.process.config;

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

    private final static String CONFIG_INITIALLISING_ERROR = "Property not initialized";
 
    private String solrPath;
    private String collections;
    private boolean isUpdate;
    
    private String username;
    private String password;
     
    public InitialProperties() {
    }

    @Inject
    public InitialProperties(@ConfigurationValue("swarm.solr.path") String solrPath,
            @ConfigurationValue("swarm.collections") String collections,
            @ConfigurationValue("swarm.update") boolean isUpdate,
            @ConfigurationValue("swarm.username") String username,
            @ConfigurationValue("swarm.password") String password ) {
        this.solrPath = solrPath; 
        this.collections = collections;
        this.isUpdate = isUpdate;
        this.username = username;
        this.password = password;
        log.info("InitialProperties : {} }", solrPath);
    } 

    public String getSolrPath() {
        if (solrPath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return solrPath;
    } 
    
    public String getCollections() {
        if (solrPath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return collections;
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
    
    public boolean isUpdate() { 
        return isUpdate;
    } 
}
