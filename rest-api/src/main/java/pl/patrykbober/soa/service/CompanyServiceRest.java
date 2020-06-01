package pl.patrykbober.soa.service;

import lombok.NoArgsConstructor;
import pl.patrykbober.soa.db.dao.CompanyRepository;
import pl.patrykbober.soa.db.dao.EmployeeRepository;
import pl.patrykbober.soa.db.mapper.CompanyMapper;
import pl.patrykbober.soa.db.mapper.EmployeeMapper;
import pl.patrykbober.soa.exception.RestException;
import pl.patrykbober.soa.jpa.BuildingEntity;
import pl.patrykbober.soa.jpa.CompanyEntity;
import pl.patrykbober.soa.jpa.EmployeeEntity;
import pl.patrykbober.soa.model.Company;
import pl.patrykbober.soa.model.CompanyFilter;
import pl.patrykbober.soa.model.Employee;
import pl.patrykbober.soa.protobuf3.dto.CompanyProto;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
@NoArgsConstructor
public class CompanyServiceRest {

    private FileServiceRest fileService;
    private CompanyRepository companyRepository;
    private EmployeeRepository employeeRepository;

    @Inject
    public CompanyServiceRest(FileServiceRest fileService,
                              CompanyRepository companyRepository,
                              EmployeeRepository employeeRepository) {
        this.fileService = fileService;
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    public Long create(Company company) {
        return companyRepository.create(CompanyMapper.toJpa(company));
    }

    public void update(Long id, Company company) {
        company.setId(id);
        companyRepository.update(CompanyMapper.toJpa(company));
    }

    public void delete(Long id) {
        companyRepository.delete(id);
    }

    public Company findById(Long id) {
        return companyRepository.getById(id).map(CompanyMapper::toModel).orElseThrow(() -> new RestException("Resource not found"));
    }

    public List<Company> findAll() {
        return companyRepository.getAll().stream()
                .map(CompanyMapper::toModel)
                .collect(Collectors.toList());
    }

    public List<Company> findFiltered(CompanyFilter filter) {
        return companyRepository.getFiltered(filter).stream()
                .map(CompanyMapper::toModel)
                .collect(Collectors.toList());
    }

    public Long addEmployee(Long companyId, Employee employee) {
        Optional<CompanyEntity> companyOpt = companyRepository.getById(companyId);
        if (companyOpt.isPresent()) {
            EmployeeEntity employeeEntity = EmployeeMapper.toJpa(employee);
            employeeEntity.setCompany(companyOpt.get());
            employeeRepository.create(employeeEntity);
            return employeeEntity.getId();
        } else {
            throw new RestException("Resource not found");
        }
    }

    public byte[] getLogo(Long id) {
        Company company = findById(id);
        return fileService.getLogoImageFromFile(company.getLogoPath());
    }

    public CompanyProto.Employees findEmployeesByCompanyId(Long companyId) {
        List<CompanyProto.Employee> employees = employeeRepository.getByCompanyId(companyId).stream()
                .map(e -> CompanyProto.Employee.newBuilder()
                        .setId(e.getId())
                        .setFirstName(e.getFirstName())
                        .setLastName(e.getLastName())
                        .setPosition(e.getPosition())
                        .setSalary(e.getSalary())
                        .build())
                .collect(Collectors.toList());
        return CompanyProto.Employees.newBuilder().addAllEmployee(employees).build();
    }

    public void mockData() {
        EmployeeEntity employee1 = EmployeeEntity.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .position("Developer")
                .salary(1000d)
                .build();
        EmployeeEntity employee2 = EmployeeEntity.builder()
                .firstName("Maciej")
                .lastName("Nowak")
                .position("HR")
                .salary(1500d)
                .build();
        EmployeeEntity employee3 = EmployeeEntity.builder()
                .firstName("Magdalena")
                .lastName("Malinowska")
                .position("Tester")
                .salary(1200d)
                .build();

        BuildingEntity building1 = BuildingEntity.builder()
                .name("Sky tower")
                .address("Wrocław")
                .build();

        CompanyEntity company1 = CompanyEntity.builder()
                .name("Motorola")
                .city("Kraków")
                .logoPath("files/motorola-logo.jpg")
                .employees(new HashSet<>())
                .buildings(new HashSet<>())
                .build();
        CompanyEntity company2 = CompanyEntity.builder()
                .name("Intel")
                .city("Warszawa")
                .logoPath("files/intel-logo.jpg")
                .employees(new HashSet<>(Arrays.asList(employee1, employee2, employee3)))
                .buildings(new HashSet<>(Arrays.asList(building1)))
                .build();

        companyRepository.create(company1);
        companyRepository.create(company2);
    }

}
