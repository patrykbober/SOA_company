package pl.patrykbober.soa;

import lombok.NoArgsConstructor;
import org.jboss.annotation.security.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;
import pl.patrykbober.soa.request.*;
import pl.patrykbober.soa.response.*;
import pl.patrykbober.soa.service.CompanyService;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@Stateless
@WebService
@NoArgsConstructor
@SecurityDomain("test-security-domain")
@DeclareRoles({"testGroup"})
@PermitAll
@WebContext(contextRoot = "/soa", urlPattern = "/company", authMethod = "BASIC", transportGuarantee = "NONE")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class CompanyController {

    private CompanyService companyService;

    @Inject
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @WebMethod
    @WebResult(name = "listCompaniesResponse")
    public ListCompaniesResponse listCompanies(@WebParam(name = "ListCompaniesRequest") ListCompaniesRequest request) {
        return companyService.findAll(request);
    }

    @WebMethod
    @WebResult(name = "addCompanyResponse")
    public AddCompanyResponse addCompany(@WebParam(name = "AddCompanyRequest") AddCompanyRequest request) {
        return companyService.create(request);
    }

    @RolesAllowed("testGroup")
    @WebMethod
    @WebResult(name = "updateCompanyResponse")
    public UpdateCompanyResponse updateCompany(@WebParam(name = "UpdateCompanyRequest") UpdateCompanyRequest request) {
        return companyService.update(request);
    }

    @RolesAllowed("testGroup")
    @WebMethod
    @WebResult(name = "deleteCompanyResponse")
    public DeleteCompanyResponse deleteCompany(@WebParam(name = "DeleteCompanyRequest") DeleteCompanyRequest request) {
        return companyService.delete(request);
    }

    @WebMethod
    @WebResult(name = "addEmployeeResponse")
    public AddEmployeeResponse addEmployee(@WebParam(name = "AddEmployeeRequest") AddEmployeeRequest request) {
        return companyService.addEmployee(request);
    }

    @WebMethod
    @WebResult(name = "getCompanyLogoResponse")
    public GetCompanyLogoResponse getCompanyLogo(@WebParam(name = "GetCompanyLogoRequest") GetCompanyLogoRequest request) {
        return companyService.getLogo(request);
    }

    @WebMethod
    public String healthCheck() {
        return "it works!";
    }

}