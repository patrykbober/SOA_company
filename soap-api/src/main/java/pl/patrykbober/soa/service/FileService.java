package pl.patrykbober.soa.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class FileService {

    public String getFileBase64Content(String path) {
        String fileContent = null;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        try {
            if (inputStream == null) {
                return "";
            }
            fileContent = Base64.getEncoder().encodeToString(inputStream.readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

}
