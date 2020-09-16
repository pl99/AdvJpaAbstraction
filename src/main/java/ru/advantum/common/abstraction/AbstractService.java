package ru.advantum.common.abstraction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * @param <D> Dto class
 * @param <R> Repository class for entity
 */
@SuppressWarnings({"AbstractClassWithoutAbstractMethods", "unchecked"})
public abstract class AbstractService<D extends AbstractDto, R extends CommonRepository<? extends AbstractEntity>>
        implements CommonService<D> {
    protected final R repository;
    protected final AbstractMapper mapper;

    protected AbstractService(R repository, AbstractMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<D> getAll() {
        List<? extends AbstractEntity> list = repository.findAll();
        return (List<D>) mapper.toDtos(list);
    }

    @Override
    public D findById(Long id) {
        Optional<? extends AbstractEntity> entity = repository.findById(id);
        return entity.map(abstractEntity -> (D) mapper.toDto(abstractEntity)).orElse((D) null);
    }

    @Override
    public Flux<D> getRxAll() {
        return Flux.fromStream(getAll().parallelStream());
    }

    @Override
    public Mono<D> findRxById(Long id) {
        return Mono.justOrEmpty(findById(id));
    }
}
