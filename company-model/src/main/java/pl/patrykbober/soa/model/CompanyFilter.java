package pl.patrykbober.soa.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlType(propOrder = {"id", "name", "city"})
public class CompanyFilter {

    private Long id;
    private String name;
    private String city;

}
