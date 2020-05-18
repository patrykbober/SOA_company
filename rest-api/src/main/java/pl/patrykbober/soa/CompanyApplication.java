package pl.patrykbober.soa;

import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class CompanyApplication extends Application {

    public CompanyApplication() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Company REST API");
        beanConfig.setVersion("1.0.0");
        beanConfig.setBasePath("/rest-api/api");
        beanConfig.setResourcePackage("pl.patrykbober.soa.controller");
        beanConfig.setPrettyPrint(true);
        beanConfig.setContact("patrykbober@student.agh.edu.pl");
        beanConfig.setScan(true);
    }

}
