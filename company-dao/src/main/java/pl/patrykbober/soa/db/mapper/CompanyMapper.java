package pl.patrykbober.soa.db.mapper;

import pl.patrykbober.soa.jpa.CompanyEntity;
import pl.patrykbober.soa.model.Company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class CompanyMapper {

    public static CompanyEntity toJpa(Company company) {
        return CompanyEntity.builder()
                .id(company.getId())
                .name(company.getName())
                .city(company.getCity())
                .logoPath(company.getLogoPath())
                .employees(company.getEmployees() != null ? company.getEmployees().stream().map(EmployeeMapper::toJpa).collect(Collectors.toSet()) : new HashSet<>())
                .buildings(company.getBuildings() != null ? company.getBuildings().stream().map(BuildingMapper::toJpa).collect(Collectors.toSet()) : new HashSet<>())
                .build();
    }

    public static Company toModel(CompanyEntity company) {
        return Company.builder()
                .id(company.getId())
                .name(company.getName())
                .city(company.getCity())
                .logoPath(company.getLogoPath())
                .employees(company.getEmployees() != null ? company.getEmployees().stream().map(EmployeeMapper::toModel).collect(Collectors.toList()) : new ArrayList<>())
                .buildings(company.getBuildings() != null ? company.getBuildings().stream().map(BuildingMapper::toModel).collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

}
