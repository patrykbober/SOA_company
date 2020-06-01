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
@Entity(name = "Employee")
@Table(name = "employee")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "first_name")
    private String firstName;

    @EqualsAndHashCode.Include
    @Column(name = "last_name")
    private String lastName;

    @EqualsAndHashCode.Include
    @Column(name = "position")
    private String position;

    @Column(name = "salary")
    private Double salary;

    @EqualsAndHashCode.Include
    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private CompanyEntity company;

}
