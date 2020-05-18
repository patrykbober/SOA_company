package pl.patrykbober.soa.repository;

import lombok.NoArgsConstructor;
import pl.patrykbober.soa.exception.RestException;
import pl.patrykbober.soa.model.Company;
import pl.patrykbober.soa.model.CompanyFilter;
import pl.patrykbober.soa.model.Employee;

import javax.ejb.Stateful;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Stateful
@NoArgsConstructor
public class CompanyRepository {
    private static final Map<Long, Company> companyDB = new ConcurrentHashMap<>();
    private static final AtomicLong companyIdCounter = new AtomicLong();
    private static final AtomicLong employeeIdCounter = new AtomicLong();

    public Long create(Company company) {
        company.setId(companyIdCounter.incrementAndGet());
        if (company.getEmployees() == null) {
            company.setEmployees(new ArrayList<>());
        }
        companyDB.put(company.getId(), company);

        return company.getId();
    }

    public Optional<Company> getById(Long id) {
        if (companyDB.containsKey(id)) {
            return Optional.of(companyDB.get(id));
        } else {
            return Optional.empty();
        }
    }

    public List<Company> getAll() {
        return new ArrayList<>(companyDB.values());
    }

    public List<Company> getFiltered(CompanyFilter filter) {
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
        if (companyDB.containsKey(id)) {
            company.setId(id);
            companyDB.put(id, company);
        } else {
            throw new RestException("Resource not found");
        }
    }

    public void delete(Long id) {
        if (companyDB.containsKey(id)) {
            companyDB.remove(id);
        } else {
            throw new RestException("Resource not found");
        }
    }

    public Long addEmployee(Long companyId, Employee employee) {
        if (companyDB.containsKey(companyId)) {
            employee.setId(employeeIdCounter.incrementAndGet());
            companyDB.get(companyId).getEmployees().add(employee);
        } else {
            throw new RestException("Resource not found");
        }
        return employee.getId();
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
