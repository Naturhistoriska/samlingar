package se.nrm.samlingar.service;

import org.eclipse.microprofile.openapi.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class SamlingarResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation (summary = "Health check")
    public String health() {
        return "{ \"status\": \"ok\", \"message\": \"Museum Search API online\" }";
    }
}
