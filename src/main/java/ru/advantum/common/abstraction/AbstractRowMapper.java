package ru.advantum.common.abstraction;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractRowMapper<T> implements RowMapper<T> {
    private final Map<String, String> columnToFields;
    private final Class<T> tClass;
    protected T mappedObject;

    public AbstractRowMapper(Class<T> tClass) {
        this.tClass = tClass;
        Field[] fields = tClass.getDeclaredFields();
        columnToFields = Arrays.stream(fields)
                .filter(it -> it.isAnnotationPresent(Column.class))
                .collect(Collectors
                        .toMap(it -> it.getAnnotation(Column.class).name(),
                                Field::getName, (x, v) -> v));
    }


    protected void postMapRow(ResultSet resultSet, int i) {
    }

    @Override
    @SneakyThrows
    public T mapRow(@NotNull ResultSet resultSet, int i) {
        mappedObject = BeanUtils.instantiateClass(tClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        bw.setAutoGrowNestedPaths(true);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int j = 1; j <= columnCount; j++) {
            String column = JdbcUtils.lookupColumnName(metaData, j);
            Class<?> clazz = Class.forName(metaData.getColumnClassName(j));
            Object value = JdbcUtils.getResultSetValue(resultSet, j, clazz);
            if (columnToFields.containsKey(column)) {
                bw.setPropertyValue(columnToFields.get(column), value);
            }
        }
        postMapRow(resultSet, i);
        return mappedObject;
    }
}
