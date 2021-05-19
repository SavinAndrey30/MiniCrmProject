package ru.savin.minicrm.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path path = Path.of(uploadDir);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        InputStream inputStream = multipartFile.getInputStream();
        Path filePath = path.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        inputStream.close();
    }
}
