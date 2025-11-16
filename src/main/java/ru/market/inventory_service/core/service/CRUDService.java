package ru.market.inventory_service.core.service;

import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.exception.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class CRUDService<Entity, Dto> {

    private final Class<Entity> entityClass;
    private final JpaRepository<Entity, Integer> entityRepository;
    private final EntityMapper<Entity, Dto> entityMapper;

    @SuppressWarnings("unchecked")
    public CRUDService(JpaRepository<Entity, Integer> entityRepository, EntityMapper<Entity, Dto> entityMapper) {
        this.entityRepository = entityRepository;
        this.entityMapper = entityMapper;
        
        ResolvableType resolvableType = ResolvableType.forClass(JpaRepository.class, entityRepository.getClass());
        ResolvableType entityType = resolvableType.getGeneric(0);
        this.entityClass = (Class<Entity>) entityType.resolve();
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<Dto>> getAll() {
        return CompletableFuture.completedFuture(
                entityRepository.findAll().parallelStream().map(entityMapper::toDto).toList()
        ).handle((result, throwable) -> {
            if (throwable != null)
                throw new EntitiesRetrieveException(entityClass.getSimpleName());
            else return result;
        });
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<Dto> getById(Integer id) {
        return CompletableFuture.completedFuture(
                entityRepository.findById(id).map(entityMapper::toDto)
                        .orElseThrow(() -> new EntityNotFoundException(entityClass.getSimpleName(), id))
        ).handle((result, throwable) -> {
            if (throwable != null)
                throw new EntityRetrieveException(entityClass.getSimpleName(), id);
            else return result;
        });
    }

    @Async
    @Transactional
    public CompletableFuture<Dto> add(Dto entity) {
        return CompletableFuture.completedFuture(
                entityRepository.save(entityMapper.toEntity(entity))
        ).handle((result, throwable) -> {
            if (throwable != null)
                throw new FailedToSaveEntityException(entityClass.getSimpleName());
            else return entityMapper.toDto(result);
        });
    }

    @Async
    @Transactional
    public CompletableFuture<Void> deleteById(Integer id) {
        try {
            if (entityRepository.existsById(id))
                entityRepository.deleteById(id);
            else throw new EntityNotFoundException(entityClass.getSimpleName(), id);
            return CompletableFuture.completedFuture(null);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new FailedToDeleteEntityException(entityClass.getSimpleName(), id);
        }
    }

    @Async
    @Transactional
    public CompletableFuture<Dto> updateById(Integer id, Dto entity) {
        try {
            boolean entityExists = entityRepository.existsById(id);
            if (entityExists) {
                Entity entityEntity = entityMapper.toEntity(entity);
                entityEntity = entityRepository.save(entityEntity);
                return CompletableFuture.completedFuture(entityMapper.toDto(entityEntity));
            } else throw new EntityNotFoundException(entityClass.getSimpleName(), id);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new FailedToSaveEntityException(entityClass.getSimpleName());
        }
    }
}
