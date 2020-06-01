package pl.patrykbober.soa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NoArgsConstructor;
import pl.patrykbober.soa.auth.Secured;
import pl.patrykbober.soa.exception.RestException;
import pl.patrykbober.soa.model.Building;
import pl.patrykbober.soa.model.Employee;
import pl.patrykbober.soa.response.Message;
import pl.patrykbober.soa.service.BuildingService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@Api("/buildings")
@Path("/buildings")
@Produces(MediaType.APPLICATION_JSON)
@NoArgsConstructor
public class BuildingController {

    private BuildingService buildingService;

    @Inject
    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Find building by id", response = Building.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Building not found", response = Message.class)
    })
    public Response findById(@PathParam("id") Long id) {
        try {
            Building building = buildingService.findById(id);
            return Response.status(Response.Status.OK).entity(building).build();
        } catch (RestException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(new Message(e.getMessage())).build();
        }
    }

    @GET
    @ApiOperation(value = "List all buildings", response = Building.class, responseContainer = "List")
    public Response listBuildings() {
        List<Building> buildings = buildingService.findAll();
        return Response.status(Response.Status.OK).entity(buildings).build();
    }

    @POST
    @Secured
    @ApiOperation(value = "Create new building", code = 201, response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 401, message = "Not authorized")
    })
    public Response createBuilding(Building building) {
        Long id = buildingService.create(building);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

//    @POST
//    @Secured
//    @Path("/{id}/employees")
//    @ApiOperation(value = "Add employee to project", code = 201, response = Long.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 401, message = "Not authorized"),
//            @ApiResponse(code = 404, message = "Company not found", response = Message.class)
//    })
//    public Response addEmployee(@PathParam("id") Long id, Employee employee) {
//        try {
//            Long employeeId = buildingService.addEmployee(id, employee);
//            return Response.status(Response.Status.CREATED).entity(employeeId).build();
//        } catch (RestException e) {
//            return Response.status(Response.Status.NOT_FOUND).entity(new Message(e.getMessage())).build();
//        }
//    }

}
