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
@Entity(name = "Building")
@Table(name = "building")
public class BuildingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "name")
    private String name;

    @EqualsAndHashCode.Include
    @Column(name = "address")
    private String address;

    @Setter(value = AccessLevel.PRIVATE)
    @ManyToMany(mappedBy = "buildings")
    private Set<CompanyEntity> companies = new HashSet<>();

}
