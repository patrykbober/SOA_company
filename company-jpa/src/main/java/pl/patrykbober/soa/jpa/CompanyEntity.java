package pl.patrykbober.soa.jpa;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "Company")
@Table(name = "company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "name")
    private String name;

    @EqualsAndHashCode.Include
    @Column(name = "city")
    private String city;

    @Column(name = "logo_path")
    private String logoPath;

    @Setter(value = AccessLevel.PRIVATE)
    @OneToMany(targetEntity = EmployeeEntity.class, mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeEntity> employees = new HashSet<>();

    @Setter(value = AccessLevel.PRIVATE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "company_has_buildings",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "building_id")
    )
    private Set<BuildingEntity> buildings = new HashSet<>();

    public void addEmployee(EmployeeEntity employee) {
        employees.add(employee);
        employee.setCompany(this);
    }

    public void addBuilding(BuildingEntity building) {
        buildings.add(building);
        building.getCompanies().add(this);
    }

}
