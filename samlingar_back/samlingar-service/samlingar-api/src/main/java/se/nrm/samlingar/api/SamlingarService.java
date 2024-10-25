package se.nrm.samlingar.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import se.nrm.samlingar.api.logic.SamlingarLogic;

/**
 *
 * @author idali
 */
@Path("/")
@Api(tags = {"samlingar-service"})
@SwaggerDefinition(
        info = @Info(
                title = "Samlingar service",
                version = "0.0.1"
        ),
        tags = {
            @Tag(name = "samlingar-service", description = "samlingar tool")
        })
@Slf4j
public class SamlingarService {

    @Inject
    private SamlingarLogic logic;

    @GET
    @Path("/search")
    @ApiOperation(value = "Search",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("text") String text,
            @QueryParam("start") int start, @QueryParam("numPerPage") int numPerPage) {
        log.info("search {} -- {}", text, start + " -- " + numPerPage);

        return Response.ok(logic.simpleSearch(text, start, numPerPage)).build();
    }

    @GET
    @Path("/filter")
    @ApiOperation(value = "filter",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterSearch(@QueryParam("text") String text,
            @QueryParam("collection") String collection,
            @QueryParam("typeStatus") String typeStatus,
            @QueryParam("family") String family,
            @QueryParam("hasCoordinates") String hasCoordinates,
            @QueryParam("hasImage") String hasImage, 
            @QueryParam("inSweden") String inSweden, 
            @QueryParam("isType") String isType,
            @QueryParam("start") int start,
            @QueryParam("numPerPage") int numPerPage) {

        log.info("filter: {}, {}", collection, typeStatus);
 
        return Response.ok(logic
                .filterSerch(start, numPerPage, text, collection, typeStatus, 
                        family, hasCoordinates, hasImage, inSweden, isType)).build();
    }
    
    
    @GET
    @Path("/geo")
    @ApiOperation(value = "geo",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response mapData(@QueryParam("text") String text,
            @QueryParam("collection") String collection,
            @QueryParam("typeStatus") String typeStatus,
            @QueryParam("family") String family ) {

        log.info("geo: {}, {}", collection, typeStatus);
 
        return Response.ok(logic
                .mapDataSearch(text, collection, typeStatus, family)).build();
    }

    @GET
    @Path("/statistic")
    @ApiOperation(value = "Statistic",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response statisticSearch() {
        log.info("statisticSearch: {}, {}"); 
        return Response.ok(logic.getStaticData()).build();
    }
}
