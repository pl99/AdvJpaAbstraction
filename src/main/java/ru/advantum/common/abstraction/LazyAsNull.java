package ru.advantum.common.abstraction;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ManyToAny;
import org.modelmapper.Condition;
import org.modelmapper.MappingException;
import org.modelmapper.internal.Errors;
import org.modelmapper.spi.MappingContext;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.springframework.context.annotation.Lazy;

@Slf4j
public class LazyAsNull<S, D> implements Condition<S, D> {

    private final EntityManager entityManager;

    public LazyAsNull(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean applies(MappingContext<S, D> context) {
        PersistenceUnitUtil unitUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        Object source = context.getSource();
        setLazyAsNull(source, unitUtil);
        return true;
    }

    private void setLazyAsNull(Object source, PersistenceUnitUtil unitUtil) {
        for (Field field : source.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                Object fieldValue = field.get(source);

                boolean isLazy = getFetchingStrategyIsLazy(field);

                if (!unitUtil.isLoaded(fieldValue) | isLazy) {
                    fieldValue = null;
                    field.set(source, fieldValue);
                }

                if (fieldValue != null) {

                    boolean isEntity = Arrays.asList(field.getType().getAnnotations())
                            .stream()
                            .filter(x -> x.annotationType().equals(Entity.class))
                            .count() > 0;

                    if (isEntity) {
                        setLazyAsNull(fieldValue, unitUtil);
                    }
                }
            } catch (IllegalAccessException e) {
                Errors errors = new Errors();
                errors.addMessage(e, "Failed to map field %s ", field.getType());
                throw new MappingException(errors.getMessages());
            }

        }
    }

    // org.hibernate.cfg.annotations.CollectionBinder#defineFetchingStrategy
    private boolean getFetchingStrategyIsLazy(Field field) {

        FetchType fetchType = FetchType.EAGER;

        Lazy lazy = field.getAnnotation(Lazy.class);

        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        OneToOne oneToOne = field.getAnnotation(OneToOne.class);
        ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        ManyToAny manyToAny = field.getAnnotation(ManyToAny.class);
        ElementCollection elementCollection = field.getAnnotation(ElementCollection.class);

        if (lazy != null ){
            fetchType = FetchType.LAZY;
        }

        if (oneToMany != null) {
            fetchType = oneToMany.fetch();
        } else if (oneToOne != null) {
            fetchType = oneToOne.fetch();
        } else if (manyToMany != null) {
            fetchType = manyToMany.fetch();
        } else if (manyToOne != null) {
            fetchType = manyToOne.fetch();
        } else if (manyToAny != null) {
            fetchType = manyToAny.fetch();
        } else if (elementCollection != null) {
            fetchType = elementCollection.fetch();
        }

        return (fetchType == FetchType.LAZY);
    }
}
