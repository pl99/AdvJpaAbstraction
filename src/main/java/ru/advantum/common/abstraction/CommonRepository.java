package ru.advantum.common.abstraction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface CommonRepository<E extends AbstractEntity> extends Repository<E, Long> {
    List<E> findAll();
    Optional<E> findById(Long id);
}
