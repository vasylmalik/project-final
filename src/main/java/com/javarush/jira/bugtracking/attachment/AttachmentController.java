package com.javarush.jira.bugtracking.attachment;

import com.javarush.jira.bugtracking.ObjectType;
import com.javarush.jira.login.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.javarush.jira.common.BaseHandler.createdResponse;

@RestController
@RequestMapping(value = AttachmentController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AttachmentController {
    static final String REST_URL = "/api/attachments";
    private final AttachmentRepository repository;

    @Transactional
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Завантажити файл вкладення",
            description = "Додає нове вкладення до об'єкта певного типу (наприклад, задачі чи проєкту) та зберігає його у файловій системі"
    )
    public ResponseEntity<Attachment> upload(@RequestPart MultipartFile file, @RequestParam ObjectType type,
                                             @RequestParam Long objectId, @AuthenticationPrincipal AuthUser authUser) {
        log.debug("upload file {} to folder {}", file.getOriginalFilename(), type.toString().toLowerCase());
        String path = FileUtil.getPath(type.toString());
        Attachment attachment = new Attachment(null, path, objectId, type, authUser.id(), file.getOriginalFilename());
        Attachment created = repository.save(attachment);
        String fileName = attachment.id() + "_" + file.getOriginalFilename();
        attachment.setFileLink(attachment.getFileLink() + fileName);
        FileUtil.upload(file, path, fileName);
        return createdResponse(REST_URL, created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Видалити файл вкладення",
            description = "Видаляє вкладення з бази даних та фізично з файлової системи за ID"
    )
    public void delete(@PathVariable Long id) {
        log.debug("delete file id = {}", id);
        Attachment attachment = repository.getExisted(id);
        repository.deleteExisted(id);
        FileUtil.delete(attachment.getFileLink());
    }

    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(
            summary = "Завантажити файл вкладення",
            description = "Повертає файл вкладення як ресурс для завантаження за його ID"
    )
    public ResponseEntity<Resource> download(@PathVariable long id) {
        log.debug("download file id = {}", id);
        Attachment attachment = repository.getExisted(id);
        Resource resource = FileUtil.download(attachment.getFileLink());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + resource.getFilename())
                .body(resource);
    }

    @GetMapping("/for-object")
    @Operation(
            summary = "Отримати всі вкладення для об'єкта",
            description = "Повертає список усіх вкладень, прикріплених до вказаного об'єкта певного типу (за objectId та типом)"
    )
    public List<Attachment> getAllForObject(@RequestParam long objectId, @RequestParam ObjectType type) {
        log.info("get all attachment by objectId = {}", objectId);
        return repository.getAllForObject(objectId, type);
    }

    @GetMapping
    @Operation(
            summary = "Отримати всі вкладення",
            description = "Повертає повний список всіх вкладень, збережених у системі"
    )
    public List<Attachment> getAll() {
        log.info("get all attachment");
        return repository.findAll();
    }
}
