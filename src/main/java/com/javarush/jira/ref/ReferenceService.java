package com.javarush.jira.ref;

import com.javarush.jira.bugtracking.ObjectType;
import com.javarush.jira.ref.internal.ReferenceMapper;
import com.javarush.jira.ref.internal.ReferenceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.javarush.jira.common.util.Util.getExisted;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReferenceService {
    static Map<RefType, Map<String, RefDTO>> refSelect;
    private final ReferenceRepository repository;
    private final ReferenceMapper mapper;

    public static Map<String, RefDTO> getRefs(RefType refType) {
        log.debug("get by type {}", refType);
        return getExisted(refSelect, refType);
    }

    public static Map<String, RefDTO> getRefsByTypeStartWithObjectType(RefType refType, @NonNull ObjectType objectType) {
        log.debug("get by type {} start with objectType {}", refType, objectType);
        String type = objectType.name().toLowerCase();
        return getRefs(refType).entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(type))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static RefDTO getRefDTO(RefType refType, String code) {
        log.debug("get by type {} and code {}", refType, code);
        return getExisted(getRefs(refType), code);
    }

    public static Map<String, RefDTO> filterEnabled(Map<String, RefDTO> unfilteredRefs) {
        log.debug("filterEnabled");
        return unfilteredRefs.entrySet().stream()
                .filter(ref -> ref.getValue().isEnabled())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @PostConstruct
    void initialize() {
        log.info("init loading");
        List<RefDTO> references = mapper.toDTOList(repository.findAllByOrderByIdAsc());
        refSelect = references.stream()
                .collect(Collectors.groupingBy(RefDTO::getRefType,
                        Collectors.collectingAndThen(Collectors.toMap(RefDTO::getCode, Function.identity(), (ref1, ref2) -> ref1, LinkedHashMap::new), Collections::unmodifiableMap)));

    }

    public void updateRefs(RefType type) {
        log.debug("update by type {}", type);
        List<RefDTO> refDTOs = mapper.toDTOList(repository.getByType(type));
        Map<String, RefDTO> refDTOMap = refDTOs.stream()
                .collect(Collectors.toMap(RefDTO::getCode, Function.identity()));
        refSelect.put(type, refDTOMap);
    }
}
