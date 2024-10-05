package todo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class SaveFileUtil {

    @Value("${file.upload-dir}")
    private String baseUploadDir;

    public String saveFile(byte[] bytes, String filename) throws IOException {
        String uniqueFileName = UUIDUtil.generateUUID() + "_" + filename;
        Path path = Paths.get(baseUploadDir + uniqueFileName);
        Files.write(path, bytes);

        String serverUrl = "http://127.0.0.1:8080/";
        return serverUrl + uniqueFileName;
    }
}
