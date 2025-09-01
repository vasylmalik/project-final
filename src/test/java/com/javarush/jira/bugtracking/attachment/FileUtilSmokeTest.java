package com.javarush.jira.bugtracking.attachment;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilSmokeTest {

    @Test
    void uploadDownloadDeleteCycle() throws Exception {
        String type = "test-files";
        String dir = FileUtil.getPath(type);
        String fileName = "hello.txt";
        String content = "Hello FileUtil " + System.nanoTime();

        MockMultipartFile mock = new MockMultipartFile(
                "file", fileName, "text/plain", content.getBytes(StandardCharsets.UTF_8)
        );
        FileUtil.upload(mock, dir, fileName);

        Path uploaded = Paths.get(dir).resolve(fileName).toAbsolutePath();
        assertTrue(Files.exists(uploaded), "файл має існувати після upload");

        Resource resource = FileUtil.download(uploaded.toString());
        assertTrue(resource.exists(), "ресурс існує");
        String readBack = Files.readString(resource.getFile().toPath());
        assertEquals(content, readBack, "вміст має співпадати");

        FileUtil.delete(uploaded.toString());
        assertFalse(Files.exists(uploaded), "файл має бути видалений");
    }
}
