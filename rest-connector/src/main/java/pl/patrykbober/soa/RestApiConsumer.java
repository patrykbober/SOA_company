package pl.patrykbober.soa;

import pl.patrykbober.soa.dto.CompanyDto;
import pl.patrykbober.soa.dto.EmployeeDto;
import pl.patrykbober.soa.dto.MessageDto;
import pl.patrykbober.soa.protobuf3.dto.CompanyProto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.io.*;
import java.util.List;

public class RestApiConsumer {

    private static final String BASE_URI = "http://localhost:8080/rest-api/api";
    private static final Client client = ClientBuilder.newClient();

    public static void main(String[] args) throws Exception {
        healthCheck();
        br();
        listCompanies();
        br();
        String token = login();
        br();
        Long companyId = createCompany(token);
        br();
        addEmployee(token, companyId);
        br();
        getById(companyId);
        br();
        getLogo(1L);
        br();
        getFiltered();
        br();
        getEmployeesProto(2L);
    }

    private static void healthCheck() {
        System.out.println("Health check:");
        Response response = client.target(BASE_URI)
                .path("companies/check")
                .request(MediaType.TEXT_PLAIN)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            System.out.println(response.readEntity(String.class));
        } else {
            System.out.println(response.getStatus());
            System.out.println("ERROR");
        }
    }

    private static void listCompanies() {
        System.out.println("List companies:");
        Response response = client.target(BASE_URI)
                .path("companies")
                .request(MediaType.APPLICATION_JSON)
                .get();

        System.out.println("Response status: " + response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<CompanyDto> companies = response.readEntity(new GenericType<>() {});
            companies.forEach(System.out::println);
        } else {
            System.out.println(response.readEntity(MessageDto.class).getMessage());
        }
    }

    private static Long createCompany(String token) throws Exception {
        System.out.println("Create company:");

        CompanyDto company = CompanyDto.builder()
                .name("Samsung")
                .city("Kraków")
                .build();

        Response response = client.target(BASE_URI)
                .path("companies")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .post(Entity.entity(company, MediaType.APPLICATION_JSON));

        System.out.println("Response status: " + response.getStatus());
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            Long id = response.readEntity(Long.class);
            System.out.println("Created entity with id: " + id);
            return id;
        } else {
            System.out.println(response.readEntity(MessageDto.class).getMessage());
            throw new Exception();
        }
    }

    private static void addEmployee(String token, Long companyId) throws Exception {
        System.out.println("Add employee:");

        EmployeeDto employee = EmployeeDto.builder()
                .firstName("Marcin")
                .lastName("Zalewski")
                .position("Project manager")
                .salary(2000d)
                .build();

        Response response = client.target(BASE_URI)
                .path("companies")
                .path(companyId.toString())
                .path("employees")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .post(Entity.entity(employee, MediaType.APPLICATION_JSON));

        System.out.println("Response status: " + response.getStatus());
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            Long id = response.readEntity(Long.class);
            System.out.println("Added employee with id: " + id);
        } else {
            System.out.println(response.readEntity(MessageDto.class).getMessage());
            throw new Exception();
        }
    }

    private static void getById(Long companyId) {
        System.out.println("Get by id:");
        Response response = client.target(BASE_URI)
                .path("companies")
                .path(companyId.toString())
                .request(MediaType.APPLICATION_JSON)
                .get();

        System.out.println("Response status: " + response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            CompanyDto company = response.readEntity(CompanyDto.class);
            System.out.println(company);
        } else {
            System.out.println(response.readEntity(MessageDto.class).getMessage());
        }
    }

    private static void getLogo(Long companyId) {
        System.out.println("Get logo:");
        Response response = client.target(BASE_URI)
                .path("companies")
                .path(companyId.toString())
                .path("logo")
                .request()
                .get();

        System.out.println("Response status: " + response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            byte[] logo = response.readEntity(byte[].class);
            File file = new File("files/logo.jpg");
            boolean mkdirs = file.getParentFile().mkdirs();
            try {
                if (file.exists()) {
                    boolean delete = file.delete();
                }
                boolean newFile = file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(logo);
                System.out.println("Image logo.jpg has been saved to files/ directory");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(response.readEntity(MessageDto.class).getMessage());
        }
    }

    private static void getFiltered() {
        System.out.println("Get companies from Kraków:");
        Response response = client.target(BASE_URI)
                .queryParam("city", "Kraków")
                .path("companies")
                .request(MediaType.APPLICATION_JSON)
                .get();

        System.out.println("Response status: " + response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<CompanyDto> companies = response.readEntity(new GenericType<>() {});
            companies.forEach(System.out::println);
        } else {
            System.out.println(response.readEntity(MessageDto.class).getMessage());
        }
    }

    private static void getEmployeesProto(Long companyId) throws IOException {
        System.out.println("Get employees (protocol buffer):");
        Response response = client.target(BASE_URI)
                .path("companies")
                .path(companyId.toString())
                .path("employees")
                .request()
                .header("accept", MediaType.APPLICATION_OCTET_STREAM)
                .get();

        System.out.println("Response status: " + response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            InputStream employeesByteString = response.readEntity(InputStream.class);
            CompanyProto.Employees employees = CompanyProto.Employees.parseFrom(employeesByteString);
            employees.getEmployeeList().stream()
                    .map(e -> EmployeeDto.builder()
                            .id(e.getId())
                            .firstName(e.getFirstName())
                            .lastName(e.getLastName())
                            .position(e.getPosition())
                            .salary(e.getSalary())
                            .build())
                    .forEach(System.out::println);
        } else {
            System.out.println(response.readEntity(MessageDto.class).getMessage());
        }
    }

    private static String login() throws Exception {
        System.out.println("Login:");
        Form form = new Form();
        form.param("login", "testUser");
        form.param("password", "testPassword");

        Response response = client.target(BASE_URI)
                .path("login")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));

        System.out.println("Response status: " + response.getStatus());
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String token = response.readEntity(MessageDto.class).getMessage();
            System.out.println("Token: " + token);
            return token;
        } else {
            throw new Exception();
        }
    }

    private static void br() {
        System.out.println();
        System.out.println("*****************");
        System.out.println();
    }

}
