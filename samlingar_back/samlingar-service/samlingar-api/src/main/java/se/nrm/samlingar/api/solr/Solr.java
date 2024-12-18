package se.nrm.samlingar.api.solr;

import java.lang.annotation.Retention;  
import java.lang.annotation.Target; 
import javax.inject.Qualifier; 
import static java.lang.annotation.ElementType.FIELD;  
import static java.lang.annotation.ElementType.METHOD;  
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;  
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * @author idali
 */
@Qualifier
@Retention(RUNTIME)
@Target({FIELD, TYPE, METHOD, PARAMETER})
public @interface Solr {
  
}
