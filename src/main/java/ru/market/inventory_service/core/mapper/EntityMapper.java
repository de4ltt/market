package ru.market.inventory_service.core.mapper;

public interface EntityMapper<Entity, Dto> {
    Dto toDto(Entity entity);
    Entity toEntity(Dto dto);
}
