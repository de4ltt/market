package ru.market.inventory_service.core.service;

import lombok.Getter;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.market.inventory_service.core.mapper.EntityMapper;
import ru.market.inventory_service.exception.*;

import java.util.List;

public abstract class MarketInventoryCRUDService<Entity, Dto> {

    private final Class<Entity> entityClass;

    @Getter
    private final JpaRepository<Entity, Integer> entityRepository;

    @Getter
    private final EntityMapper<Entity, Dto> entityMapper;

    @SuppressWarnings("unchecked")
    public MarketInventoryCRUDService(JpaRepository<Entity, Integer> entityRepository, EntityMapper<Entity, Dto> entityMapper) {
        this.entityRepository = entityRepository;
        this.entityMapper = entityMapper;

        ResolvableType resolvableType = ResolvableType.forClass(JpaRepository.class, entityRepository.getClass());
        ResolvableType entityType = resolvableType.getGeneric(0);
        this.entityClass = (Class<Entity>) entityType.resolve();
    }

    @Transactional(readOnly = true)
    public List<Dto> getAll() {
        try {
            return entityRepository.findAll().stream().map(entityMapper::toDto).toList();
        } catch (Exception e) {
            throw new EntitiesRetrieveException(entityClass.getSimpleName());
        }
    }

    @Transactional(readOnly = true)
    public Dto getById(Integer id) {
        try {
            return entityRepository.findById(id).map(entityMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException(entityClass.getSimpleName(), id));
        } catch (EntityNotFoundException e) {
            throw new EntityRetrieveException(entityClass.getSimpleName(), id);
        }
    }

    @Transactional
    public Dto add(Dto entity) {
        try {
            return entityMapper.toDto(entityRepository.save(entityMapper.toEntity(entity)));
        } catch (Exception e) {
            throw new FailedToSaveEntityException(entityClass.getSimpleName());
        }
    }

    @Transactional
    public List<Dto> addAll(List<Dto> entityList) {
        try {
            return entityRepository
                    .saveAll(entityList.stream().map(entityMapper::toEntity).toList()).stream().map(entityMapper::toDto).toList();
        } catch (Exception e) {
            throw new FailedToSaveEntitiesException(entityClass.getSimpleName());
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        try {
            if (entityRepository.existsById(id))
                entityRepository.deleteById(id);
            else throw new EntityNotFoundException(entityClass.getSimpleName(), id);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new FailedToDeleteEntityException(entityClass.getSimpleName(), id);
        }
    }

    @Transactional
    public Dto updateById(Integer id, Dto entity) {
        try {
            boolean entityExists = entityRepository.existsById(id);
            if (entityExists) {
                Entity entityEntity = entityMapper.toEntity(entity);
                entityEntity = entityRepository.save(entityEntity);
                return entityMapper.toDto(entityEntity);
            } else throw new EntityNotFoundException(entityClass.getSimpleName(), id);
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new FailedToSaveEntityException(entityClass.getSimpleName());
        }
    }
}
