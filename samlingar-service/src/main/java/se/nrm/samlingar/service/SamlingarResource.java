package se.nrm.samlingar.service;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

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
    public Response initalData() {
        LOG.info("initalData");
        return Response.ok(service.initalData()).build();
    }

    @GET
    @Path("/simpleSearch")
    @Operation(
        summary = "SimpleSearch",
        description = "Return search results in json"
    ) 
    @Produces(MediaType.APPLICATION_JSON)
    public Response simpleSearch(@Context UriInfo uriInfo) { 
        LOG.info("simpleSearch");
        return Response.ok(service.simpleSearch(uriInfo.getQueryParameters())).build(); 
    }

    @GET
    @Path("/search")
    @Operation(
            summary = "Search",
            description = "Return search results in json"
    ) 
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@Context UriInfo uriInfo) { 
        LOG.info("search");
        return Response.ok(service.search(uriInfo.getQueryParameters())).build(); 
    }
}
