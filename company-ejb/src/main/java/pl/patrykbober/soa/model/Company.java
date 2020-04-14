package pl.patrykbober.soa.model;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    private Long id;
    private String name;
    private String city;
    private String logoPath;

    @XmlElementWrapper
    @XmlElement(name = "employee")
    private List<Employee> employees = new ArrayList<>();

}
