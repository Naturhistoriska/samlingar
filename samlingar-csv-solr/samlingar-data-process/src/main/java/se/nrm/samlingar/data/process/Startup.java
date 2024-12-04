package se.nrm.samlingar.data.process;

import java.util.concurrent.CompletableFuture;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes; 
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import se.nrm.samlingar.data.process.logic.DataProcessor;

/**
 *
 * @author idali
 */
@Slf4j
@ApplicationScoped
public class Startup {
 
    @Inject
    private DataProcessor process;

    public Startup() {

    }
 
    void init(@Observes @Initialized(ApplicationScoped.class) Object event) {
        log.info("StartupBean Application - INITIALIZATION");

        CompletableFuture.runAsync(() -> {
            process.run();
            log.info("app processing....");

        });
    }

}
