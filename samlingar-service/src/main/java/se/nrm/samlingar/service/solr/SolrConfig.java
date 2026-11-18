package se.nrm.samlingar.service.solr;

import java.time.Duration;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "samlingar.solr")
public interface SolrConfig {
    String baseUrl();
    String collection();
    String username();
    String password();

    @WithDefault("5s")
    Duration connectionTimeout();

    @WithDefault("30s")
    Duration requestTimeout();
}
