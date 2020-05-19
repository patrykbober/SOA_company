package pl.patrykbober.soa.service;

import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import pl.patrykbober.soa.exception.RestException;

import javax.ejb.Stateless;
import java.io.IOException;
import java.io.InputStream;

@Log
@Stateless
@NoArgsConstructor
public class FileServiceRest {

    public byte[] getLogoImageFromFile(String path) {
        log.info(".getLogoImageFromFile invoked for path " + path);
        byte[] fileContent;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        try {
            if (inputStream == null) {
                return null;
            }
            fileContent = inputStream.readAllBytes();
        } catch (IOException e) {
            log.severe(e.getMessage());
            throw new RestException("Logo image was not found");
        }
        return fileContent;
    }

}
