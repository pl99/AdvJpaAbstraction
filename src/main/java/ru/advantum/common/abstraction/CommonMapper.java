package ru.advantum.common.abstraction;

import java.util.Collection;

public interface CommonMapper<E extends AbstractEntity, D extends AbstractDto> {

    E toEntity(D dto);
    D toDto(E entity);
    Collection<E> toEntities(Collection<D> dtos);
    Collection<D> toDtos(Collection<E> entities);
}
