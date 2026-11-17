package se.nrm.samlingar.service;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class SamlingarResource {

    private static final Logger LOG = Logger.getLogger(SamlingarResource.class);

    @Inject
    SamlingarService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation (summary = "Health check")
    public String health() {
        return "{ \"status\": \"ok\", \"message\": \"Museum Search API online\" }";
    }

    @GET
    @Path("/initialData")
    @Operation(
        summary = "Statistic",
        description = "Return search results in json"
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInitalData() {
        LOG.info("getInitalData");
        return Response.ok(service.getInitalData()).build();
    }
}
