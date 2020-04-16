package pl.patrykbober.soa.service;

import lombok.NoArgsConstructor;
import pl.patrykbober.soa.model.Company;
import pl.patrykbober.soa.model.CompanyFilter;
import pl.patrykbober.soa.model.Employee;
import pl.patrykbober.soa.request.*;
import pl.patrykbober.soa.response.*;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
@NoArgsConstructor
public class CompanyService {

    private final List<Company> companies = prepareSampleData();

    private FileService fileService;

    @Inject
    public CompanyService(FileService fileService) {
        this.fileService = fileService;
    }

    public AddCompanyResponse create(AddCompanyRequest request) {
        Company company = request.getCompany();
        if (companies.stream().noneMatch(c -> c.getId().equals(company.getId()))) {
            companies.add(company);
        }
        return AddCompanyResponse.builder().company(company).build();
    }

    public UpdateCompanyResponse update(UpdateCompanyRequest request) {
        Optional<Company> companyOptional = companies.stream().filter(c -> c.getId().equals(request.getId())).findFirst();
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            companies.remove(company);
            company.setId(request.getId());
            company.setName(request.getName());
            company.setCity(request.getCity());
            companies.add(company);
            return UpdateCompanyResponse.builder().company(company).build();
        }
        return null;
    }

    public DeleteCompanyResponse delete(DeleteCompanyRequest request) {
        Optional<Company> companyToDelete = companies.stream().filter(c -> c.getId().equals(request.getId())).findFirst();
        if (companyToDelete.isPresent()) {
            companies.remove(companyToDelete.get());
            return DeleteCompanyResponse.builder().success(true).build();
        }
        return DeleteCompanyResponse.builder().success(false).build();
    }

    public ListCompaniesResponse findAll(ListCompaniesRequest request) {
        CompanyFilter filter = request.getFilter();
        List<Company> companyList = companies;
        if (filter.getId() != null) {
            companyList = companyList.stream().filter(c -> filter.getId().equals(c.getId())).collect(Collectors.toList());
        }
        if (filter.getName() != null) {
            companyList = companyList.stream().filter(c -> filter.getName().equals(c.getName())).collect(Collectors.toList());
        }
        if (filter.getCity() != null) {
            companyList = companyList.stream().filter(c -> filter.getCity().equals(c.getCity())).collect(Collectors.toList());
        }
        return ListCompaniesResponse.builder().companies(companyList).build();
    }

    public AddEmployeeResponse addEmployee(AddEmployeeRequest request) {
        Optional<Company> company = companies.stream().filter(c -> c.getId().equals(request.getCompanyId())).findFirst();
        if (company.isPresent()) {
            company.get().getEmployees().add(request.getEmployee());
            return AddEmployeeResponse.builder().success(true).build();
        }
        return AddEmployeeResponse.builder().success(false).build();
    }

    public GetCompanyLogoResponse getLogo(GetCompanyLogoRequest request) {
        return GetCompanyLogoResponse.builder()
                .content(fileService.getFileBase64Content(request.getLogoPath()))
                .build();
    }

    private List<Company> prepareSampleData() {
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Jan")
                .lastName("Kowalski")
                .position("Developer")
                .salary(1000d)
                .build();
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Maciej")
                .lastName("Nowak")
                .position("HR")
                .salary(1500d)
                .build();
        Employee employee3 = Employee.builder()
                .id(3L)
                .firstName("Magdalena")
                .lastName("Malinowska")
                .position("Tester")
                .salary(1200d)
                .build();

        List<Company> companies = new ArrayList<>();
        companies.add(Company.builder()
                .id(1L)
                .name("Motorola")
                .city("Kraków")
                .logoPath("files/motorola-logo.jpg")
                .employees(Collections.singletonList(employee1))
                .build());
        companies.add(Company.builder()
                .id(2L)
                .name("Intel")
                .city("Warszawa")
                .logoPath("files/intel-logo.jpg")
                .employees(Arrays.asList(employee2, employee3))
                .build());

        return companies;
    }
}
