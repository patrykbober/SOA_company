package pl.patrykbober.soa.controller;

import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import pl.patrykbober.soa.exception.RestException;
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
    public Response login(@FormParam("login") String login,
                          @FormParam("password") String password) {
        try {
            String token = authenticationService.login(login, password);
            return Response.status(Response.Status.OK).entity(token).build();
        } catch (RestException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(e.getMessage()).build();
        }
    }

}
