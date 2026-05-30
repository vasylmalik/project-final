package com.javarush.jira.bugtracking.attachment;

import com.javarush.jira.common.error.IllegalRequestDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileUtilTest {

    @TempDir
    Path tempDir;

    @Test
    void uploadCreatesDirectoriesAndWritesFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "report.txt",
                "text/plain",
                "hello world".getBytes(StandardCharsets.UTF_8));

        FileUtil.upload(multipartFile, tempDir.resolve("attachments/project").toString(), "1_report.txt");

        Path uploadedFile = tempDir.resolve("attachments/project/1_report.txt");
        assertArrayEquals("hello world".getBytes(StandardCharsets.UTF_8), Files.readAllBytes(uploadedFile));
    }

    @Test
    void uploadRejectsEmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.txt", "text/plain", new byte[0]);

        assertThrows(IllegalRequestDataException.class,
                () -> FileUtil.upload(emptyFile, tempDir.resolve("attachments").toString(), "1_empty.txt"));
    }
}
