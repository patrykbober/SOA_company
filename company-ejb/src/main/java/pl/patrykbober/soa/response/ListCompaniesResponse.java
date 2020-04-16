package pl.patrykbober.soa.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.patrykbober.soa.model.Company;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlType(name = "ListCompaniesResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListCompaniesResponse {

    @XmlElementWrapper(name = "companies")
    @XmlElement(name = "company")
    private List<Company> companies;

}
