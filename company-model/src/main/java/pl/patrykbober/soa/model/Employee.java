package pl.patrykbober.soa.model;

import lombok.*;

import javax.xml.bind.annotation.XmlType;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlType(propOrder = {"id", "firstName", "lastName", "position", "salary"})
public class Employee {

    private Long id;
    private String firstName;
    private String lastName;
    private String position;
    private Double salary;

}
