package pl.patrykbober.soa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.NoArgsConstructor;
import pl.patrykbober.soa.exception.RestException;
import pl.patrykbober.soa.response.Message;
import pl.patrykbober.soa.service.AuthenticationService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/login")
@Api("/login")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
@NoArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @Inject
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @POST
    @ApiOperation(value = "Authenticate and get token", response = Message.class)
    @ApiResponse(code = 401, message = "Invalid user or password", response = Message.class)
    public Response login(@FormParam("login") String login,
                          @FormParam("password") String password) {
        try {
            String token = authenticationService.login(login, password);
            return Response.status(Response.Status.OK).entity(new Message(token)).build();
        } catch (RestException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new Message(e.getMessage())).build();
        }
    }

}
