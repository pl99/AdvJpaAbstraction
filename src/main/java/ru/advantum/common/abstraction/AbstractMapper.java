package ru.advantum.common.abstraction;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractMapper<E extends AbstractEntity, D extends AbstractDto> implements CommonMapper<E, D> {

    @Autowired
    protected ModelMapper mapper;

    private final Class<E> entityClass;
    private final Class<D> dtoClass;

    public AbstractMapper(Class<E> entityClass, Class<D> dtoClass) {
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public synchronized E toEntity(D dto) {
        return Objects.isNull(dto)
                ? null
                : mapper.map(dto, entityClass);
    }

    @Override
    public synchronized D toDto(E entity) {
        return Objects.isNull(entity)
                ? null
                : mapper.map(entity, dtoClass);
    }

    @Override
    public Collection<D> toDtos(Collection<E> entities){
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }
    @Override
    public Collection<E> toEntities(Collection<D> dtos){
        if (Objects.isNull(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    protected Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    protected Converter<D, E> toEntityConverter() {
        return context -> {
            D source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    /**
     * @param source - entity class
     * @param destination - dto class
     */
    protected void mapSpecificFields(E source, D destination) {
        // Do nothing because for overriding
    }

    /**
     *
     * @param source - dto class
     * @param destination - entity class
     */
    protected void mapSpecificFields(D source, E destination) {
        // Do nothing for overriding
    }

}
