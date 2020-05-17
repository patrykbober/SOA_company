package pl.patrykbober.soa.service;

import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@NoArgsConstructor
public class FileServiceRest {

    public byte[] getFileBase64Content(String path) {
        byte[] fileContent = null;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        try {
            if (inputStream == null) {
                return null;
            }
            fileContent = inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

}
