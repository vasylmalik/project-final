package com.javarush.jira.bugtracking.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    @PostMapping("/{type}")
    public ResponseEntity<String> upload(
            @PathVariable String type,
            @RequestPart("file") MultipartFile file) {

        String path = FileUtil.getPath(type);
        String fileName = file.getOriginalFilename();
        FileUtil.upload(file, path, fileName);

        return ResponseEntity.ok(path + "/" + fileName);
    }

    @DeleteMapping("/{fileLink}")
    public ResponseEntity<Void> delete(@PathVariable String fileLink) {
        FileUtil.delete(fileLink);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{fileLink}")
    public ResponseEntity<Resource> download(@PathVariable String fileLink) {
        Resource resource = FileUtil.download(fileLink);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
