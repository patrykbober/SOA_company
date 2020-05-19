package pl.patrykbober.soa.repository;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import pl.patrykbober.soa.exception.RestException;
import pl.patrykbober.soa.model.Company;
import pl.patrykbober.soa.model.CompanyFilter;
import pl.patrykbober.soa.model.Employee;

import javax.ejb.Stateful;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Log
@Stateful
@NoArgsConstructor
public class CompanyRepository {
    private static final Map<Long, Company> companyDB = new ConcurrentHashMap<>();
    private static final AtomicLong companyIdCounter = new AtomicLong();
    private static final AtomicLong employeeIdCounter = new AtomicLong();

    public Long create(Company company) {
        log.info(".create invoked for company " + company.getName());
        company.setId(companyIdCounter.incrementAndGet());
        if (company.getEmployees() == null) {
            company.setEmployees(new ArrayList<>());
        }
        companyDB.put(company.getId(), company);

        return company.getId();
    }

    public Optional<Company> getById(Long id) {
        log.info(".getById invoked for id " + id);
        if (companyDB.containsKey(id)) {
            return Optional.of(companyDB.get(id));
        } else {
            log.severe(String.format("company with id %d was not found", id));
            return Optional.empty();
        }
    }

    public List<Company> getAll() {
        log.info(".getAll invoked");
        return new ArrayList<>(companyDB.values());
    }

    public List<Company> getFiltered(CompanyFilter filter) {
        log.info(".getFiltered invoked");
        List<Company> companyList = getAll();
        if (filter != null && filter.getName() != null) {
            companyList = companyList.stream().filter(c -> c.getName().toLowerCase().contains(filter.getName().toLowerCase())).collect(Collectors.toList());
        }
        if (filter != null && filter.getCity() != null) {
            companyList = companyList.stream().filter(c -> c.getCity().toLowerCase().contains(filter.getCity().toLowerCase())).collect(Collectors.toList());
        }

        return companyList;
    }

    public void update(Long id, Company company) {
        log.info(".update invoked for id " + id);
        if (companyDB.containsKey(id)) {
            company.setId(id);
            companyDB.put(id, company);
        } else {
            log.severe(String.format("company with id %d was not found", id));
            throw new RestException("Resource not found");
        }
    }

    public void delete(Long id) {
        log.info(".delete invoked for id " + id);
        if (companyDB.containsKey(id)) {
            companyDB.remove(id);
        } else {
            log.severe(String.format("company with id %d was not found", id));
            throw new RestException("Resource not found");
        }
    }

    public Long addEmployee(Long companyId, Employee employee) {
        log.info(".addEmployee invoked for companyId " + companyId);
        if (companyDB.containsKey(companyId)) {
            employee.setId(employeeIdCounter.incrementAndGet());
            companyDB.get(companyId).getEmployees().add(employee);
        } else {
            log.severe(String.format("company with id %d was not found", companyId));
            throw new RestException("Resource not found");
        }
        return employee.getId();
    }

    public List<Employee> getEmployeesByCompanyId(Long companyId) {
        log.info(".getEmployeesByCompanyId invoked for id " + companyId);
        if (companyDB.containsKey(companyId)) {
            return companyDB.get(companyId).getEmployees();
        } else {
            log.severe(String.format("company with id %d was not found", companyId));
            throw new RestException("Resource not found");
        }
    }

    // initialize DB with sample data
    static {
        Employee employee1 = Employee.builder()
                .id(employeeIdCounter.incrementAndGet())
                .firstName("Jan")
                .lastName("Kowalski")
                .position("Developer")
                .salary(1000d)
                .build();
        Employee employee2 = Employee.builder()
                .id(employeeIdCounter.incrementAndGet())
                .firstName("Maciej")
                .lastName("Nowak")
                .position("HR")
                .salary(1500d)
                .build();
        Employee employee3 = Employee.builder()
                .id(employeeIdCounter.incrementAndGet())
                .firstName("Magdalena")
                .lastName("Malinowska")
                .position("Tester")
                .salary(1200d)
                .build();

        Company company = Company.builder()
                .id(companyIdCounter.incrementAndGet())
                .name("Motorola")
                .city("Kraków")
                .logoPath("files/motorola-logo.jpg")
                .employees(Collections.singletonList(employee1))
                .build();
        companyDB.put(company.getId(), company);

        company = Company.builder()
                .id(companyIdCounter.incrementAndGet())
                .name("Intel")
                .city("Warszawa")
                .logoPath("files/intel-logo.jpg")
                .employees(Arrays.asList(employee2, employee3))
                .build();
        companyDB.put(company.getId(), company);
    }

}
