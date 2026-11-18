package se.nrm.samlingar.service.solr;

public class SolrAccessException extends RuntimeException {

    public SolrAccessException(Throwable cause) {
        super(cause);
    }

    public SolrAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
