package pl.patrykbober.soa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Api("/companies")
@Path("/companies")
@Produces(MediaType.APPLICATION_JSON)
@NoArgsConstructor
public class CompanyController {

    @GET
    @Path("/check")
    @ApiOperation(value = "Health check", response = String.class)
    public Response healthCheck() {
        return Response.ok("It works!").build();
    }

}
