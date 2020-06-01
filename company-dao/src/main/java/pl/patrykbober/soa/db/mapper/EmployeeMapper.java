package pl.patrykbober.soa.db.mapper;

import pl.patrykbober.soa.jpa.EmployeeEntity;
import pl.patrykbober.soa.model.Employee;

public class EmployeeMapper {

    public static EmployeeEntity toJpa(Employee employee) {
        return EmployeeEntity.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .build();
    }

    public static Employee toModel(EmployeeEntity employee) {
        return Employee.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .build();
    }

}
