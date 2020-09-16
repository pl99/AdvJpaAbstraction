package ru.advantum.common.abstraction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CommonService <D extends AbstractDto>{
    List<D> getAll();
    D findById(Long id);
    Flux<D> getRxAll();
    Mono<D> findRxById(Long id);
}
