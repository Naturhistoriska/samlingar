package se.nrm.samlingar.api.solr;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import se.nrm.samlingar.api.logic.InitialProperties;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Startup
@Slf4j
public class SolrClientProvider {

    private static HttpSolrClient solrClient;
 
    private static String solrPath;
    private static String username;
    private static String password;
    
    public static HttpSolrClient getSolrClient(InitialProperties properties) {
        log.info("HttpSolrClient : {}", solrClient);
         
        solrPath = properties.getSolrURL();
        username = properties.getUsername();
        password = properties.getPassword();
        
        log.info("solr : {} -- {}", solrPath, username + " -- " + password);
        
        if (solrClient == null) {
            synchronized (SolrClientProvider.class) {
                if (solrClient == null) {

                    CredentialsProvider credsProvider = new BasicCredentialsProvider();
                    credsProvider.setCredentials(AuthScope.ANY,
                            new UsernamePasswordCredentials(username, password));

                    CloseableHttpClient httpClient = HttpClients.custom()
                            .setDefaultCredentialsProvider(credsProvider)
                            .build();

                    solrClient = new HttpSolrClient.Builder(solrPath)
                            .withHttpClient(httpClient)
                            .build();
                }
            }
        }
        return solrClient;
    }
    
    /**
     * Close SolrClient when bean is destroyed
     */
    @PreDestroy
    public void preDestroy() {
        log.info("preDestroy - solrClient is closed");
        try {
            solrClient.close();
        } catch (IOException ex) {
        }
    } 
}
