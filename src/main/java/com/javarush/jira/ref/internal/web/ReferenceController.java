package com.javarush.jira.ref.internal.web;

import com.javarush.jira.ref.RefDTO;
import com.javarush.jira.ref.RefType;
import com.javarush.jira.ref.ReferenceService;
import com.javarush.jira.ref.internal.Reference;
import com.javarush.jira.ref.internal.ReferenceMapper;
import com.javarush.jira.ref.internal.ReferenceRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.javarush.jira.common.BaseHandler.createdResponse;
import static com.javarush.jira.common.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = ReferenceController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j

public class ReferenceController {
    static final String REST_URL = "/api/admin/refs";
    private ReferenceMapper mapper;
    private ReferenceService service;
    private ReferenceRepository repository;

    @GetMapping("/{type}")
    public Map<String, RefDTO> getRefsByType(@PathVariable RefType type) {
        return ReferenceService.getRefs(type);
    }

    @GetMapping("/{type}/{code}")
    public RefDTO getRefByTypeByCode(@PathVariable RefType type, @PathVariable String code) {
        return ReferenceService.getRefDTO(type, code);
    }

    @DeleteMapping("/{type}/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable RefType type, @PathVariable String code) {
        log.debug("delete with type {}, code {}", type, code);
        RefDTO ref = ReferenceService.getRefDTO(type, code);
        repository.deleteExisted(ref.id());
        service.updateRefs(type);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RefDTO> create(@Valid @RequestBody RefDTO refDTO) {
        log.debug("create {}", refDTO);
        checkNew(refDTO);
        Reference ref = repository.save(mapper.toEntity(refDTO));
        refDTO.setId(ref.id());
        RefType refType = refDTO.getRefType();
        service.updateRefs(refType);
        return createdResponse(REST_URL + "/{type}/{code}", refDTO, refType, refDTO.getCode());
    }

    @PutMapping("/{type}/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateTitle(@PathVariable RefType type, @PathVariable String code, @RequestParam String title) {
        log.debug("update Ref with type={}, code={} with title={}", title, code, title);
        Reference ref = getExisted(type, code);
        ref.setTitle(title);
        repository.save(ref);
        ReferenceService.getRefDTO(type, code).setTitle(title);
    }

    @PatchMapping("/{type}/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void enable(@PathVariable RefType type, @PathVariable String code, @RequestParam boolean enabled) {
        log.debug("enable Ref with type={}, code={} with enabled={}", type, code, enabled);
        Reference ref = getExisted(type, code);
        ref.setEnabled(enabled);
        repository.save(ref);
        ReferenceService.getRefDTO(type, code).setEnabled(enabled);
    }

    private Reference getExisted(RefType type, String code) {
        return repository.getExistedByTypeAndCode(type, code);
    }
}
