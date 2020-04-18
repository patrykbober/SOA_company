package pl.patrykbober.soa;

import pl.patrykbober.soa.generated.*;

import javax.xml.ws.BindingProvider;
import java.io.*;
import java.util.Base64;
import java.util.List;

public class ApiConsumer {

    public static void main(String[] args) {
        CompanyController controller = new CompanyControllerService().getCompanyControllerPort();
        setCredentials((BindingProvider) controller);

        System.out.println(controller.healthCheck());

        ListCompaniesRequest listCompaniesRequest = new ListCompaniesRequest();
        listCompaniesRequest.setFilter(new CompanyFilter());
        System.out.println("List companies result:");
        controller.listCompanies(listCompaniesRequest).getCompanies().getCompany().forEach(c -> System.out.println(companyToString(c)));

        AddCompanyRequest addCompanyRequest = new AddCompanyRequest();
        Company companyToAdd = new Company();
        companyToAdd.setId(3L);
        companyToAdd.setName("Samsung");
        companyToAdd.setCity("Kraków");
        addCompanyRequest.setCompany(companyToAdd);
        System.out.println("Add company result:");
        System.out.println(companyToString(controller.addCompany(addCompanyRequest).getCompany()));

        AddEmployeeRequest addEmployeeRequest = new AddEmployeeRequest();
        Employee employeeToAdd = new Employee();
        employeeToAdd.setId(4L);
        employeeToAdd.setFirstName("Marcin");
        employeeToAdd.setLastName("Zalewski");
        employeeToAdd.setPosition("Project manager");
        employeeToAdd.setSalary(2000d);
        addEmployeeRequest.setCompanyId(3L);
        addEmployeeRequest.setEmployee(employeeToAdd);
        System.out.println("Add employee result:");
        System.out.println(controller.addEmployee(addEmployeeRequest).isSuccess());

        ListCompaniesRequest listCompaniesRequest1 = new ListCompaniesRequest();
        CompanyFilter companyFilter = new CompanyFilter();
        companyFilter.setId(1L);
        listCompaniesRequest1.setFilter(companyFilter);
        Company company = controller.listCompanies(listCompaniesRequest1).getCompanies().getCompany().get(0);
        GetCompanyLogoRequest getCompanyLogoRequest = new GetCompanyLogoRequest();
        getCompanyLogoRequest.setLogoPath(company.getLogoPath());
        String companyLogoBase64 = controller.getCompanyLogo(getCompanyLogoRequest).getContent();

        File file = new File(company.getLogoPath());
        boolean mkdirs = file.getParentFile().mkdirs();
        try {
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(Base64.getDecoder().decode(companyLogoBase64));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("List companies from Kraków result:");
        ListCompaniesRequest listCompaniesRequest2 = new ListCompaniesRequest();
        CompanyFilter krakowCompanies = new CompanyFilter();
        krakowCompanies.setCity("Kraków");
        listCompaniesRequest2.setFilter(krakowCompanies);
        controller.listCompanies(listCompaniesRequest2).getCompanies().getCompany().forEach(c -> System.out.println(companyToString(c)));
    }

    private static void setCredentials(BindingProvider port) {
        port.getRequestContext().put("javax.xml.ws.security.auth.username", "testUser");
        port.getRequestContext().put("javax.xml.ws.security.auth.password", "testPassword");
    }

    private static String companyToString(Company company) {
        return "Company{" +
                "id=" + company.getId() +
                ", name='" + company.getName() + '\'' +
                ", city='" + company.getCity() + '\'' +
                ", logoPath='" + company.getLogoPath() + '\'' +
                ", employees=" + employeesToString(company.getEmployees().getEmployee()) +
                '}';
    }

    private static String employeesToString(List<Employee> employees) {
        StringBuilder result = new StringBuilder();
        for (Employee employee : employees) {
            result.append("Employee{" + "id=")
                    .append(employee.getId())
                    .append(", firstName='")
                    .append(employee.getFirstName())
                    .append('\'')
                    .append(", lastName='")
                    .append(employee.getLastName())
                    .append('\'')
                    .append(", position='")
                    .append(employee.getPosition())
                    .append('\'')
                    .append(", salary=")
                    .append(employee.getSalary())
                    .append("}, ");
        }
        if (!employees.isEmpty()) {
            result.setLength(result.length() - 2);
        } else {
            result.append("{}");
        }
        return result.toString();
    }

}
