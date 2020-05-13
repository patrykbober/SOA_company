package pl.patrykbober.soa.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(propOrder = {"id", "name", "city"})
public class CompanyFilter {

    private Long id;
    private String name;
    private String city;

}
