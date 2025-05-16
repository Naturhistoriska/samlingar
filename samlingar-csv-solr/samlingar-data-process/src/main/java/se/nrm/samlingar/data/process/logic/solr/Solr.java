package se.nrm.samlingar.data.process.logic.solr;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import se.nrm.samlingar.data.process.config.InitialProperties;

/**
 *
 * @author idali
 */
@Slf4j
public class Solr {
    private final String utf8 = "UTF-8";
    private final String applicationJson = "application/json";
    
    private final String deleteStringStart = "{'delete': {'query': 'filter(id:(";
    private final String deleteStringEnd = "))'}}";
    private final String star = "*";
    
//    private final String authErrorMsg = "Incorrect username and password";
    
    private StringBuilder sb;
      
    private HttpPost post;
    private StringEntity entity; 
    
    private String userName;
    private String password; 
    private String solrUrl; 
    
    private int status;

    private CredentialsProvider provider; 

    @Inject
    private InitialProperties properties;
    
    public Solr() {

    }
    
    @PostConstruct
    public void init() {
        log.info("init");
        this.userName = properties.getUsername();
        this.password = properties.getPassword();
        this.solrUrl = properties.getSolrPath(); 
        
        provider = new BasicCredentialsProvider();
        provider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(userName, password)
        );  
    }
    
    public int deleteCollection(String collectionId) {
        log.info("deleteCollection : {}", collectionId); 
        sb = new StringBuilder();
        sb.append(deleteStringStart);
        sb.append(collectionId);
        sb.append(star);
        sb.append(deleteStringEnd); 
        
        log.info("delete query : {}", sb.toString());
        
        post = new HttpPost(solrUrl);
        
        entity = new StringEntity(sb.toString().trim(), utf8);
        entity.setContentType(applicationJson);
        post.setEntity(entity);
         
        
        return authAndPost(post); 
    }
     
    public int postToSolr(String jsonString) {
        log.info("postToSolr");
         
  
        post = new HttpPost(solrUrl);

        entity = new StringEntity(jsonString, utf8);
        entity.setContentType(applicationJson);
        post.setEntity(entity);
        
        return authAndPost(post); 
    }
 
    private int authAndPost(HttpPost request)  { 
        
        try ( CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();  
                CloseableHttpResponse httpResponse = httpClient.execute(request)) { 
            status = httpResponse.getStatusLine().getStatusCode();
            log.info("status : {}", status);
            if(status == 401) {
                return 401;
            }
            return httpResponse.getStatusLine().getStatusCode();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return 500;
        }
    }
    
}
