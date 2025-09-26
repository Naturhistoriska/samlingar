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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType; 
import javax.ws.rs.core.Response; 
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
    @Path("/chart")
    @ApiOperation(value = "Search",
            notes = "Return search results in json",
            response = String.class
    ) 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChart(@QueryParam("collectionCode") String collectionCode, 
            @QueryParam("isYearChart") String isYearChart) { 
        log.info("getChart : {} -- {}", collectionCode, isYearChart);
        return Response.ok(logic.getChart(collectionCode, isYearChart)).build(); 
    }
    
    
    @GET
    @Path("/freeTextSearch")
    @ApiOperation(value = "freeTextSearch",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response freeTextSearch(@QueryParam("catchall") String text) {
        log.info("freeTextSearch {} ", text);
          
        return Response.ok(logic.freeTextSearch(text )).build();
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
    
    @GET
    @Path("/autocomplete")
    @ApiOperation(value = "autocomplete",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response autoCompleteSearch(@QueryParam("text") String text,
            @QueryParam("field") String field) {
        log.info("autoCompleteSearch: {} -- {} ", text, field);
         
        return Response.ok(logic.autoCompleteSearch(text, field)).build();
    }
     
    @GET
    @Path("/scientificname")
    @ApiOperation(value = "scientificname",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response scientificname(@QueryParam("scientificname") String text,
            @QueryParam("searchMode") String searchMode, 
            @QueryParam("start") int start, 
            @QueryParam("numPerPage") int numPerPage,
            @QueryParam("sort") String sort) {
        log.info("scientificname  {} -- {}", text, searchMode + " -- " + sort);
 
        return Response.ok(logic.scientificNameSearch(text, 
                searchMode, start, numPerPage, sort)).build();
    }
    
    @GET
    @Path("/search")
    @ApiOperation(value = "Search",
            notes = "Return search results in json",
            response = String.class
    ) 
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSearchResults(@Context UriInfo uriInfo) { 
        log.info("getSearchResults");
        return Response.ok(logic.search(uriInfo.getQueryParameters())).build(); 
    }
    
    @GET
    @Path("/simpleSearch")
    @ApiOperation(value = "SimpleSearch",
            notes = "Return search results in json",
            response = String.class
    ) 
    @Produces(MediaType.APPLICATION_JSON)
    public Response simpleSearch(@Context UriInfo uriInfo) { 
        log.info("simpleSearch");
        return Response.ok(logic.simpleSearch(uriInfo.getQueryParameters())).build(); 
    }
     
    
                
    @GET
    @Path("/id")
    @ApiOperation(value = "Search by id",
            notes = "Return search results in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response idSearch(@QueryParam("id") String id ) { 
        log.info("idSearch... {}", id); 
        return Response.ok(logic.searchWithId(id)).build();
    }
    
    @GET
    @Path("/geojson")
    @ApiOperation(value = "geojson",
            notes = "Return data in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response geojson(@Context UriInfo uriInfo ) { 
        log.info("start....");
        
        return Response.ok(logic
                .geoJson(uriInfo.getQueryParameters())).build(); 
    }
    
        
    

    @GET
    @Path("/download")
    @ApiOperation(value = "download",
            notes = "Return data in json",
            response = String.class
    ) 
    public Response download(@Context UriInfo uriInfo ) {  
        log.info("download");
          
        return Response.ok(logic
                .export(uriInfo.getQueryParameters())).build();
    }
    
    
    
    
    
    
    
    
    
 
        

    
    @GET
    @Path("/heatmap")  
    @ApiOperation(value = "heatmap",
            notes = "Return data in json",
            response = String.class
    )
    @Produces(MediaType.APPLICATION_JSON)
    public Response heatmap(@Context UriInfo uriInfo ) { 
        log.info("start....");
         
        Response res = Response.ok(logic
                .getHeatmap(uriInfo.getQueryParameters())).build();
        
        
        log.info("end.....");
        return res;
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
//    
//    
//    
//    
    
    

    
    
    

    
   
     
            





    
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
 
    
    
    
    
    
    
    
        
//    @GET
//    @Path("/freeTextSearch")
//    @ApiOperation(value = "freeTextSearch",
//            notes = "Return search results in json",
//            response = String.class
//    )
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response freeTextSearch(@QueryParam("text") String text,
//                @QueryParam("hasCoordinates") boolean hasCoordinates,
//                @QueryParam("hasImage") boolean hasImage,
//                @QueryParam("isType") boolean isType,
//                @QueryParam("isInSweden") boolean isInSweden,
//                @QueryParam("collections") String collections, 
//                @QueryParam("start") int start,  
//                @QueryParam("numPerPage") int numPerPage,
//                @QueryParam("sort") String sort) {
//        log.info("freeTextSearch {} -- {}", text, start + " -- " + numPerPage);
//        
//        log.info("freeTextSearch  has image and map {} -- {}", hasImage, hasCoordinates);
//
//        if (text == null || text.isEmpty()) {
//            text = wildCard;
//        } 
//        
//        return Response.ok(logic.freeTextSearch(text, hasImage, 
//                hasCoordinates, isType, isInSweden, collections, 
//                start, numPerPage, sort)).build();
//    }
      


     
    
    
//    
//    @GET
//    @Path("/chart")
//    @ApiOperation(value = "ChartData",
//            notes = "Return search results in json",
//            response = String.class
//    )
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getChartData(@QueryParam("collection") String collection) {
//        return Response.ok(logic.getChartData(collection)).build();
//    }
    
    

//    @GET
//    @Path("/search")
//    @ApiOperation(value = "search",
//            notes = "Return search results in json",
//            response = String.class
//    )
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response search(@QueryParam("text") String text,
//            @QueryParam("scientificName") String scientificName,
//            @QueryParam("fuzzySearch") boolean fuzzySearch,
//            @QueryParam("hasCoordinates") boolean hasCoordinates,
//            @QueryParam("hasImage") boolean hasImage,
//            @QueryParam("isType") boolean isType,
//            @QueryParam("isInSweden") boolean isInSweden,
//            @QueryParam("collections") String collections,
//            @QueryParam("startDate") String startDate,
//            @QueryParam("endDate") String endDate,
//            @QueryParam("start") int start,
//            @QueryParam("numPerPage") int numPerPage,
//            @QueryParam("sort") String sort ) {
//        
//        log.info("search... {}", scientificName);
// 
//        return Response.ok(logic.search(text, scientificName, fuzzySearch,
//                hasImage, hasCoordinates, isType, isInSweden, collections, 
//                startDate, endDate, start, numPerPage, sort)).build();
//    }
    


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
//    @Path("/filter")
//    @ApiOperation(value = "filter",
//            notes = "Return search results in json",
//            response = String.class
//    )
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response filterSearch(@QueryParam("text") String text,
//            @QueryParam("collection") String collection,
//            @QueryParam("collections") String collections,
//            @QueryParam("typeStatus") String typeStatus,
//            @QueryParam("family") String family,
//            @QueryParam("hasCoordinates") String hasCoordinates,
//            @QueryParam("hasImage") String hasImage,
//            @QueryParam("inSweden") String inSweden,
//            @QueryParam("isType") String isType,
//            @QueryParam("start") int start,
//            @QueryParam("numPerPage") int numPerPage,
//            @QueryParam("sort") String sort) {
//
//        log.info("filter: {}, {}", collections, typeStatus);
//
//        if (text == null || text.isEmpty()) {
//            text = wildCard;
//        }
//
//        return Response.ok(logic
//                .filterSerch(start, numPerPage, text, collection, collections,
//                        typeStatus, family, hasCoordinates, hasImage, inSweden,
//                        isType, sort)).build();
//    }

//    @GET
//    @Path("/geo")
//    @ApiOperation(value = "geo",
//            notes = "Return search results in json",
//            response = String.class
//    )
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response mapData(@QueryParam("text") String text,
//            @QueryParam("collection") String collection,
//            @QueryParam("collections") String collections,
//            @QueryParam("typeStatus") String typeStatus,
//            @QueryParam("family") String family,
//            @QueryParam("hasCoordinates") String hasCoordinates,
//            @QueryParam("hasImage") String hasImage,
//            @QueryParam("inSweden") String inSweden,
//            @QueryParam("isType") String isType) {
//
//        log.info("geo: {}, {}", collection, typeStatus);
//
//        if (text == null || text.isEmpty()) {
//            text = wildCard;
//        }
//
//        return Response.ok(logic
//                .mapDataSearch(text, collection, collections, typeStatus, family,
//                        hasCoordinates, hasImage, inSweden, isType)).build();
//    }

//    @GET
//    @Path("/typestatus")
//    @ApiOperation(value = "typestatus",
//            notes = "Return search results in json",
//            response = String.class
//    )
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response typestatusSearch() {
//        log.info("typestatusSearch: {}, {}");
//        return Response.ok(logic.getTypeStatus()).build();
//    }







 
}
