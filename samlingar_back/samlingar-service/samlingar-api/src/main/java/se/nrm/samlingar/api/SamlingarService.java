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
import javax.ws.rs.core.StreamingOutput;
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
    @Path("/autocomplete")
    @ApiOperation(value = "autocomplete",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response autoCompleteSearch(@QueryParam("text") String text) {
        log.info("autoCompleteSearch: {} ", text);
        return Response.ok(logic.autoCompleteSearch(text)).build();
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
            @QueryParam("collections") String collections,
            @QueryParam("typeStatus") String typeStatus,
            @QueryParam("family") String family,
            @QueryParam("hasCoordinates") String hasCoordinates,
            @QueryParam("hasImage") String hasImage,
            @QueryParam("inSweden") String inSweden,
            @QueryParam("isType") String isType,
            @QueryParam("start") int start,
            @QueryParam("numPerPage") int numPerPage) {

        log.info("filter: {}, {}", collections, typeStatus);

        return Response.ok(logic
                .filterSerch(start, numPerPage, text, collection, collections, 
                        typeStatus, family, hasCoordinates, hasImage, inSweden, 
                        isType)).build();
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
            @QueryParam("collections") String collections,
            @QueryParam("typeStatus") String typeStatus,
            @QueryParam("family") String family,
            @QueryParam("hasCoordinates") String hasCoordinates,
            @QueryParam("hasImage") String hasImage,
            @QueryParam("inSweden") String inSweden,
            @QueryParam("isType") String isType) {

        log.info("geo: {}, {}", collection, typeStatus);

        return Response.ok(logic
                .mapDataSearch(text, collection, collections, typeStatus, family, 
                        hasCoordinates, hasImage, inSweden, isType)).build();
    }
    
    @GET
    @Path("/typestatus")
    @ApiOperation(value = "typestatus",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response typestatusSearch() {
        log.info("statisticSearch: {}, {}");
        return Response.ok(logic.getStaticData()).build();
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
        return Response.ok(logic.getStaticData())
                .header("ACCESS_CONTROL_ALLOW_ORIGIN", "*")
                .build();
    }

    @GET
    @Path("/download")
    @ApiOperation(value = "Statistic",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response download(@QueryParam("text") String text,
            @QueryParam("collection") String collection,
            @QueryParam("typeStatus") String typeStatus,
            @QueryParam("family") String family,
            @QueryParam("numRows") int numRows) {
        log.info("statisticSearch: {}, {}");

        return Response.ok(logic
                .download(text, collection, typeStatus, family, numRows)).build();
    }

    @Path("file/")
    @GET
    @Produces({"application/pdf"})
    public StreamingOutput getFileContent() throws Exception {
     
//        InputStream myStream = ;
//        
//        
//
//// Set the content type and attachment header.
//        response.addHeader("Content-disposition", "attachment;filename=myfilename.txt");
//        response.setContentType("txt/plain");
//
//// Copy the stream to the response's output stream.
//        IOUtils.copy(myStream, response.getOutputStream());
//        response.flushBuffer();
        return null;
    } 
}
