package com.javarush.jira.bugtracking.attachment;

import com.javarush.jira.common.error.IllegalRequestDataException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Objects;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileUtil {

    private FileUtil() {}

    public static void upload(MultipartFile multipartFile, String directoryPath, String fileName) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalRequestDataException("Empty file");
        }
        try {
            Path dir = Paths.get(directoryPath).toAbsolutePath().normalize();
            Files.createDirectories(dir);

            String safeName = Paths.get(
                    Objects.requireNonNullElse(fileName, "file")
            ).getFileName().toString().trim();
            if (safeName.isEmpty()) {
                throw new IllegalRequestDataException("Invalid file name");
            }

            Path target = dir.resolve(safeName).normalize();
            if (!target.startsWith(dir)) {
                throw new IllegalRequestDataException("Path traversal detected");
            }

            try (InputStream in = multipartFile.getInputStream()) {
                Files.copy(in, target, REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new IllegalRequestDataException(
                    "Failed to upload file: " + multipartFile.getOriginalFilename()
            );
        }
    }

    public static void delete(String filePath) {
        try {
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            throw new IllegalRequestDataException("Failed to delete file: " + filePath);
        }
    }

    public static Resource download(String filePath) {
        Path path = Paths.get(filePath).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            throw new IllegalRequestDataException("File not found: " + filePath);
        }
        return new FileSystemResource(path);
    }

    public static String getPath(String type) {
        return Paths.get("uploads", type).toString();
    }
}
