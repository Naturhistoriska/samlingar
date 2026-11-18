package se.nrm.samlingar.service;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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

    @GET
    @Path("/freeTextSearch")
    @Operation(
        summary = "freeTextSearch",
        description = "Return search results in json"
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response freeTextSearch(@QueryParam("catchall") String text, @QueryParam("mode") String mode) {
        LOG.info(String.format("freeTextSearch: %s", text));
        return Response.ok(service.freeTextSearch(text, mode)).build();
    }

    @GET
    @Path("/autocomplete")
    @Operation(
        summary = "autocomplete",
        description = "Return search results in json"
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response autoCompleteSearch(@QueryParam("text") String text,
            @QueryParam("field") String field) {
        LOG.info(String.format("autoCompleteSearch: %s -- %s ", text, field));
        return Response.ok(service.autoCompleteSearch(text, field)).build();
    }

    @GET
    @Path("/scientificname")
    @Operation(
        summary = "scientificname",
        description = "Return search results in json"
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response scientificNameSearch(
            @QueryParam("scientificname") String text,
            @QueryParam("searchMode") String searchMode, 
            @QueryParam("start") int start, 
            @QueryParam("numPerPage") int numPerPage,
            @QueryParam("sort") String sort) {
        LOG.info(String.format("scientificname:  %s -- %s -- %s", text, searchMode, sort));
        return Response.ok(service.scientificNameSearch(text, searchMode, start, numPerPage, sort)).build();
    }

    @GET
    @Path("/geojson")
    @Operation(
            summary = "GeoJSON",
            description = "Return geojson data"
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response geojson(@Context UriInfo uriInfo ) { 
        LOG.info("geojson");
        return Response.ok(service.geojson(uriInfo.getQueryParameters())).build(); 
    }

    @GET
    @Path("/heatmap")  
    @Operation(
        summary = "heatmap",
        description = "Return data in json"
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response heatmap(@Context UriInfo uriInfo ) { 
        LOG.info("heatmap");
        return Response.ok(service.heatmap(uriInfo.getQueryParameters())).build();
    }

    @GET
    @Path("/id")
    @Operation(
        summary = "Search by id",
        description = "Return search results in json"
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response id(@QueryParam("id") String id) { 
        LOG.info(String.format("id: %s", id)); 
        return Response.ok(service.id(id)).build();
    }
}
