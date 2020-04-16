package pl.patrykbober.soa.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.patrykbober.soa.model.Company;

import javax.xml.bind.annotation.XmlType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlType(name = "UpdateCompanyResponse")
public class UpdateCompanyResponse {

    private Company company;

}
