package pl.patrykbober.soa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NoArgsConstructor;
import pl.patrykbober.soa.auth.Secured;
import pl.patrykbober.soa.exception.RestException;
import pl.patrykbober.soa.model.Company;
import pl.patrykbober.soa.model.CompanyFilter;
import pl.patrykbober.soa.model.Employee;
import pl.patrykbober.soa.service.CompanyServiceRest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.util.List;

@Stateless
@Api("/companies")
@Path("/companies")
@Produces(MediaType.APPLICATION_JSON)
@NoArgsConstructor
public class CompanyController {

    private CompanyServiceRest companyService;

    @Inject
    public CompanyController(CompanyServiceRest companyService) {
        this.companyService = companyService;
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            Company company = companyService.findById(id);
            return Response.status(Response.Status.OK).entity(company).build();
        } catch (RestException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response listCompanies(@QueryParam("name") String name,
                                  @QueryParam("city") String city) {
        CompanyFilter companyFilter = CompanyFilter.builder()
                .name(name)
                .city(city)
                .build();
        List<Company> companies = companyService.findFiltered(companyFilter);
        return Response.status(Response.Status.OK).entity(companies).build();
    }

    @POST
    @Secured
    public Response createCompany(Company company) {
        Long id = companyService.create(company);
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    @PUT
    @Secured
    @Path("/{id}")
    public Response updateCompany(@PathParam("id") Long id, Company company) {
        try {
            companyService.update(id, company);
            return Response.status(Response.Status.OK).build();
        } catch (RestException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Secured
    @Path("/{id}")
    public Response deleteCompany(@PathParam("id") Long id) {
        try {
            companyService.delete(id);
            return Response.status(Response.Status.OK).build();
        } catch (RestException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Secured
    @Path("/{id}/employees")
    public Response addEmployee(@PathParam("id") Long id, Employee employee) {
        try {
            Long employeeId = companyService.addEmployee(id, employee);
            return Response.status(Response.Status.CREATED).entity(employeeId).build();
        } catch (RestException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}/logo")
    @Produces("image/jpeg")
    public Response getLogo(@PathParam("id") Long id) {
        try {
            byte[] logo = companyService.getLogo(id);
            return Response.status(Response.Status.OK).entity((StreamingOutput) outputStream -> {
                outputStream.write(logo);
                outputStream.flush();
            }).build();
        } catch (RestException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/check")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Health check", response = String.class)
    public Response healthCheck() {
        return Response.ok("It works!").build();
    }

}
