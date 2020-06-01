package pl.patrykbober.soa.service;

import lombok.NoArgsConstructor;
import pl.patrykbober.soa.db.dao.EmployeeRepository;
import pl.patrykbober.soa.db.dao.BuildingRepository;
import pl.patrykbober.soa.db.mapper.BuildingMapper;
import pl.patrykbober.soa.db.mapper.EmployeeMapper;
import pl.patrykbober.soa.exception.RestException;
import pl.patrykbober.soa.jpa.BuildingEntity;
import pl.patrykbober.soa.jpa.EmployeeEntity;
import pl.patrykbober.soa.model.Building;
import pl.patrykbober.soa.model.Employee;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
@NoArgsConstructor
public class BuildingService {

    private BuildingRepository buildingRepository;
    private EmployeeRepository employeeRepository;

    @Inject
    public BuildingService(BuildingRepository buildingRepository,
                           EmployeeRepository employeeRepository) {
        this.buildingRepository = buildingRepository;
        this.employeeRepository = employeeRepository;
    }

    public Long create(Building building) {
        return buildingRepository.create(BuildingMapper.toJpa(building));
    }

    public void update(Long id, Building building) {
        building.setId(id);
        buildingRepository.update(BuildingMapper.toJpa(building));
    }

    public void delete(Long id) {
        buildingRepository.delete(id);
    }

    public Building findById(Long id) {
        return buildingRepository.getById(id).map(BuildingMapper::toModel).orElseThrow(() -> new RestException("Resource not found"));
    }

    public List<Building> findAll() {
        return buildingRepository.getAll().stream()
                .map(BuildingMapper::toModel)
                .collect(Collectors.toList());
    }

    public Long addEmployee(Long projectId, Employee employee) {
        Optional<BuildingEntity> projectOpt = buildingRepository.getById(projectId);
        if (projectOpt.isPresent()) {
            EmployeeEntity employeeEntity = EmployeeMapper.toJpa(employee);
            employeeRepository.create(employeeEntity);
            return employeeEntity.getId();
        } else {
            throw new RestException("Resource not found");
        }
    }

}
