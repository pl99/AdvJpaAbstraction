package ru.advantum.common.abstraction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface CommonJpaRepository<E extends AbstractEntity>  extends CommonRepository<E> , JpaRepository<E, Long> {
    @Override
    Optional<E> findById(Long id);
}
