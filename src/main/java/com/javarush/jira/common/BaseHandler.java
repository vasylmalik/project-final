package com.javarush.jira.common;

import com.javarush.jira.common.model.TimestampEntry;
import com.javarush.jira.common.to.BaseTo;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.javarush.jira.common.util.validation.ValidationUtil.assureIdConsistent;
import static com.javarush.jira.common.util.validation.ValidationUtil.checkNew;

public class BaseHandler<E extends HasId, T extends BaseTo, R extends BaseRepository<E>, M extends BaseMapper<E, T>> {
    public static final String REST_URL = "/api";
    public static final String UI_URL = "/ui";

    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Getter
    private final R repository;
    @Getter
    private final M mapper;
    private final Function<E, E> prepareForSave;
    private final BiFunction<E, E, E> prepareForUpdate;
    public BaseHandler(R repository, M mapper) {
        this(repository, mapper, null, null);
    }
    public BaseHandler(R repository, M mapper,
                       Function<E, E> prepareForSave, BiFunction<E, E, E> prepareForUpdate) {
        this.repository = repository;
        this.mapper = mapper;
        this.prepareForSave = prepareForSave;
        this.prepareForUpdate = prepareForUpdate;
    }

    public static <T extends HasId> ResponseEntity<T> createdResponse(String url, T created) {
        return createdResponse(url + "/{id}", created, created.getId());
    }

    public static <T extends HasId> ResponseEntity<T> createdResponse(String url, T created, Object... params) {
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(url).buildAndExpand(params).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    public T getTo(long id) {
        log.info("get by id={}", id);
        return mapper.toTo(repository.getExisted(id));
    }

    public E get(long id) {
        log.info("get by id={}", id);
        return repository.getExisted(id);
    }

    public List<E> getAll() {
        return getAll(Sort.unsorted());
    }

    public List<E> getAll(Sort sort) {
        log.info("get all ");
        return repository.findAll(sort);
    }

    public List<T> getAllTos() {
        return getAllTos(Sort.unsorted());
    }

    public List<T> getAllTos(Sort sort) {
        log.info("get all TOs");
        return mapper.toToList(repository.findAll(sort));
    }

    public E createFromTo(T to) {
        log.info("createFromTo {}", to);
        checkNew(to);
        E entity = mapper.toEntity(to);
        if (prepareForSave != null) entity = prepareForSave.apply(entity);
        return repository.save(entity);
    }

    public E create(E entity) {
        log.info("create {}", entity);
        checkNew(entity);
        if (prepareForSave != null) entity = prepareForSave.apply(entity);
        return repository.save(entity);
    }

    public void delete(long id) {
        log.info("delete by id={}", id);
        repository.deleteExisted(id);
    }

    @Transactional
    public E update(E entity, long id) {
        log.info("update {} with id={}", entity, id);
        assureIdConsistent(entity, id);
        if (prepareForUpdate != null) {
            E dbEntity = repository.getExisted(entity.id());
            entity = prepareForUpdate.apply(entity, dbEntity);
        }
        return repository.save(entity);
    }

    @Transactional
    public E updateFromTo(T to, long id) {
        log.info("updateFromTo {} with id={}", to, id);
        assureIdConsistent(to, id);
        E dbEntity = repository.getExisted(to.id());
        return repository.save(mapper.updateFromTo(to, dbEntity));
    }

    @Transactional
    public void enable(long id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        E entity = repository.getExisted(id);
        if (entity instanceof TimestampEntry te) {
            te.setEnabled(enabled);
        } else {
            throw new UnsupportedOperationException("enabling for " + entity + " is not supported");
        }
    }
}
