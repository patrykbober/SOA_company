package pl.patrykbober.soa.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlType(name = "GetCompanyLogoResponse")
public class GetCompanyLogoResponse {

    private String content;

}
