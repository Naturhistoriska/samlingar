package se.nrm.samlingar.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
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
                version = "0.1.5"
        ),
        tags = {
            @Tag(name = "samlingar-service", description = "samlingar api")
        })
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class SamlingarService {

    private final String wildCard = "*:*";

    @Inject
    private SamlingarLogic logic;
      
    @GET
    @Path("/initialData")
    @ApiOperation(value = "Statistic",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInitalData() {
        log.info("getInitalData");
        return Response.ok(logic.getInitalData()).build();
    }
    
        
    @GET
    @Path("/freeTextSearch")
    @ApiOperation(value = "freeTextSearch",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response freeTextSearch(@QueryParam("text") String text,
                @QueryParam("hasCoordinates") boolean hasCoordinates,
                @QueryParam("hasImage") boolean hasImage,
                @QueryParam("isType") boolean isType,
                @QueryParam("isInSweden") boolean isInSweden,
                @QueryParam("collections") String collections, 
                @QueryParam("start") int start,  
                @QueryParam("numPerPage") int numPerPage,
                @QueryParam("sort") String sort) {
        log.info("freeTextSearch {} -- {}", text, start + " -- " + numPerPage);
        
        log.info("freeTextSearch  has image and map {} -- {}", hasImage, hasCoordinates);

        if (text == null || text.isEmpty()) {
            text = wildCard;
        } 
        
        return Response.ok(logic.freeTextSearch(text, hasImage, 
                hasCoordinates, isType, isInSweden, collections, 
                start, numPerPage, sort)).build();
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
    @Path("/scientificname")
    @ApiOperation(value = "scientificname",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response scientificname(@QueryParam("text") String text,
            @QueryParam("fuzzySearch") boolean fuzzySearch,
            @QueryParam("start") int start, 
            @QueryParam("numPerPage") int numPerPage,
            @QueryParam("sort") String sort) {
        log.info("scientificname  {} -- {}", text, fuzzySearch + " -- " + sort);
 
        return Response.ok(logic.scientificNameSearch(text, fuzzySearch,
                start, numPerPage, sort)).build();
    }
     
    @GET
    @Path("/chart")
    @ApiOperation(value = "ChartData",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChartData(@QueryParam("collection") String collection) {
        return Response.ok(logic.getChartData(collection)).build();
    }
    
    
    @GET
    @Path("/test")
    @ApiOperation(value = "test",
            notes = "Return search results in json",
            response = String.class
    ) 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSearchResults(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();

        int start = 0;
        int numPerPage = 0;
        boolean fuzzySearch = true;
        String scientificName = null;
        String text = "*";
        String sort = null;
                
        Map<String, String> paramMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
            
            switch (entry.getKey()) {
                case "scientificName": 
                    scientificName = queryParams.get("scientificName").get(0); 
                    break;
                case "text": 
                    text = queryParams.get("text").get(0); 
                    break;
                case "start":
                    start = Integer.parseInt(queryParams.get("start").get(0)); 
                    break;
                case "numPerPage":
                    numPerPage = Integer.parseInt(queryParams.get("numPerPage").get(0));
                    break;  
                case "fuzzySearch":
                    fuzzySearch = Boolean.parseBoolean(queryParams.get("fuzzySearch").get(0));
                    break;
                case "sort":
                    sort =  queryParams.get("sort").get(0);
                    break;
                default:
                    paramMap.put(entry.getKey(), entry.getValue().get(0));
                    break;
            }
        }
 
        return Response.ok(logic.search(paramMap, text, scientificName, fuzzySearch,  
                start, numPerPage, sort)).build();
    }
    
    @GET
    @Path("/search")
    @ApiOperation(value = "search",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("text") String text,
            @QueryParam("scientificName") String scientificName,
            @QueryParam("fuzzySearch") boolean fuzzySearch,
            @QueryParam("hasCoordinates") boolean hasCoordinates,
            @QueryParam("hasImage") boolean hasImage,
            @QueryParam("isType") boolean isType,
            @QueryParam("isInSweden") boolean isInSweden,
            @QueryParam("collections") String collections,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("start") int start,
            @QueryParam("numPerPage") int numPerPage,
            @QueryParam("sort") String sort ) {
        
        log.info("search... {}", scientificName);
//        
//        MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
//        Map<String, String> paramMap = new HashMap<>();
//        for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
//            paramMap.put(entry.getKey(), entry.getValue().get(0)); // or handle list if needed
//        }
//        log.info("map...", paramMap);
        
        return Response.ok(logic.search(text, scientificName, fuzzySearch,
                hasImage, hasCoordinates, isType, isInSweden, collections, 
                startDate, endDate, start, numPerPage, sort)).build();
    }
    
        
    @GET
    @Path("/id")
    @ApiOperation(value = "search",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response idSearch(@QueryParam("id") String id ) { 
        log.info("idSearch... {}", id); 
        return Response.ok(logic.searchWithId(id)).build();
    }


//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
    
    
    
    
    

     


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    @GET
//    @Path("/search")
//    @ApiOperation(value = "Search",
//            notes = "Return search results in json",
//            response = String.class
//    )
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response search(@QueryParam("text") String text,
//            @QueryParam("fuzzySearch") boolean fuzzySearch,
//            @QueryParam("start") int start, 
//            @QueryParam("numPerPage") int numPerPage,
//            @QueryParam("sort") String sort) {
//        log.info("search {} -- {}", text, start + " -- " + numPerPage);
//
//        if (text == null || text.isEmpty()) {
//            text = wildCard;
//        }
//        return Response.ok(logic.simpleSearch(text, fuzzySearch,
//                start, numPerPage, sort)).build();
//    }
//    
    
    
    
    
    
    
    @GET
    @Path("/statistic")
    @ApiOperation(value = "Statistic",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response statisticSearch() {
        log.info("statisticSearch: {}, {}");

        return Response.ok(logic.getStatisticData()).build();
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
            @QueryParam("numPerPage") int numPerPage,
            @QueryParam("sort") String sort) {

        log.info("filter: {}, {}", collections, typeStatus);

        if (text == null || text.isEmpty()) {
            text = wildCard;
        }

        return Response.ok(logic
                .filterSerch(start, numPerPage, text, collection, collections,
                        typeStatus, family, hasCoordinates, hasImage, inSweden,
                        isType, sort)).build();
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

        if (text == null || text.isEmpty()) {
            text = wildCard;
        }

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
        log.info("typestatusSearch: {}, {}");
        return Response.ok(logic.getTypeStatus()).build();
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
