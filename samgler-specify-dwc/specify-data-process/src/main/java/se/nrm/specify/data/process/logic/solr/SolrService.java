package se.nrm.specify.data.process.logic.solr;

import java.io.IOException;
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
import se.nrm.specify.data.process.config.InitialProperties;

/**
 *
 * @author idali
 */
@Slf4j
public class SolrService {
    
    private final String utf8 = "UTF-8";
    private final String applicationJson = "application/json";
 
    private HttpPost post;
    private StringEntity entity; 

    private StringBuilder sb;
 
    private final String deleteStringStart = "{'delete': {'query': 'filter(collectionId:(";
    private final String deleteStringEnd = "))'}}";
    
    private int status;

    private CredentialsProvider provider; 

    @Inject
    private InitialProperties propeties;

    public SolrService() {
    }

    public int deleteCollection(int collectionId) throws Exception {
        log.info("deleteCollection : {}", collectionId); 
        sb = new StringBuilder();
        sb.append(deleteStringStart);
        sb.append(collectionId);
        sb.append(deleteStringEnd); 
        
        post = new HttpPost(propeties.getSolrPath());
        
        entity = new StringEntity(sb.toString().trim(), utf8);
        entity.setContentType(applicationJson);
        post.setEntity(entity);
          
        return authAndPost(post); 
    }
 
    public int postToSolr(String jsonString) throws Exception {
        log.info("postToSolr");
  
        post = new HttpPost(propeties.getSolrPath());

        entity = new StringEntity(jsonString, utf8);
        entity.setContentType(applicationJson);
        post.setEntity(entity);
        
        return authAndPost(post); 
    }
    
    private int authAndPost(HttpPost post) throws Exception {
        provider = new BasicCredentialsProvider();
        provider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(propeties.getUsername(), propeties.getPassword())
        );

        try ( CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(provider)
                .build();  CloseableHttpResponse httpResponse = httpClient.execute(post)) {

            // 401 if wrong user/password 
//            log.info("response : {}", httpResponse.getStatusLine().getStatusCode());
            status = httpResponse.getStatusLine().getStatusCode();
            if(status == 401) {
                throw new Exception("Incorrect username and password");
            }
            return httpResponse.getStatusLine().getStatusCode();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return 500;
        }
    }
    
}
