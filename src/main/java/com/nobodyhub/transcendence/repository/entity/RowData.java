package com.nobodyhub.transcendence.repository.entity;

import com.google.common.collect.Maps;
import com.nobodyhub.transcendence.repository.annotation.Column;
import com.nobodyhub.transcendence.repository.annotation.ColumnFamily;
import com.nobodyhub.transcendence.repository.annotation.ColumnMap;
import com.nobodyhub.transcendence.repository.annotation.Id;
import com.nobodyhub.transcendence.repository.entity.mapper.KeyMapper;
import com.nobodyhub.transcendence.repository.entity.mapper.ValueMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An intermediate structure converting {@link Entity} to Cassandra row
 *
 * @author yan_h
 * @since 2018/6/11
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class RowData {
    /**
     * Column Family Name
     */
    @Getter
    private final String cfName;
    /**
     * Row key column name
     */
    @Getter
    private final String rowKeyName;
    /**
     * Row Key
     */
    @Getter
    private final String rowKey;
    /**
     * Column key/value
     */
    @Getter
    private Map<String, String> values = Maps.newHashMap();

    /**
     * Create RowData <b>without</b> rowKey/values from Entity Class
     *
     * @param entityCls
     * @return
     */
    public static <T extends Entity> RowData of(Class<T> entityCls) {
        return of(entityCls, null);
    }

    /**
     * Create RowData <b>with</b> rowKey/values from Entity instance
     *
     * @param entity
     * @return
     */
    public static <T extends Entity> RowData of(T entity) {
        return of(entity.getClass(), entity);
    }

    /**
     * Get field value
     *
     * @param fieldName field name
     * @param fieldCls  field value class
     * @param <T>       type of field value
     * @return
     * @throws IllegalAccessException
     */
    public <T> T getField(String fieldName, Class<T> fieldCls) {
        String colNm = KeyMapper.to(fieldName);
        return ValueMapper.from(values.get(colNm), fieldCls);
    }

    /**
     * Get values of Map field
     *
     * @param fieldName field name
     * @param kCls      Class of map key
     * @param vCls      Class of map value
     * @param <K>       type of map key
     * @param <V>       type of map value
     * @return
     * @throws IllegalAccessException
     */
    public <K, V> Map<K, V> getMapField(String fieldName,
                                        Class<K> kCls,
                                        Class<V> vCls) {
        Map<K, V> result = Maps.newHashMap();
        String colNm = KeyMapper.to(fieldName);
        Pattern patter = Pattern.compile("^" + colNm + "_(.+)$");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            Matcher matcher = patter.matcher(entry.getKey());
            if (matcher.matches()) {
                result.put(
                        KeyMapper.from(matcher.group(1), kCls),
                        ValueMapper.from(entry.getValue(), vCls));
            }
        }
        return result;
    }

    /**
     * Get the normal column names(except the primary key column)
     *
     * @return
     */
    public Set<String> getColumnNames() {
        return values.keySet();
    }


    private static void parseValues(Entity entity, RowData rowData) {
        Class<? extends Entity> entityCls = entity.getClass();
        // can have at most 1 map field
        int nMapField = 0;
        for (Field field : entityCls.getFields()) {
            try {
                Column colAnno = field.getAnnotation(Column.class);
                if (colAnno != null) {
                    parseNormalFieldValue(rowData, entity, field);
                    continue;
                }
                ColumnMap colMapAnno = field.getAnnotation(ColumnMap.class);
                if (colMapAnno != null) {
                    nMapField++;
                    if (nMapField > 1) {
                        throw new InvalidEntityException(entityCls,
                                "Can have at most one @ColumnMap field!");
                    }
                    parseMapFieldValue(rowData, entity, field);
                }
            } catch (IllegalAccessException e) {
                throw new InvalidEntityException(entityCls,
                        e.getMessage());
            }
        }
    }

    private static void parseMapFieldValue(RowData rowData,
                                           Object object,
                                           Field field) throws IllegalAccessException {
        Map values = (Map) field.get(object);
        String colNm = KeyMapper.to(field.getName());
        for (Object key : values.keySet()) {
            rowData.addValue(
                    field.getType(),
                    String.format("%s_%s", colNm, KeyMapper.to(key)),
                    ValueMapper.to(values.get(key)));
        }
    }

    private static void parseNormalFieldValue(RowData rowData,
                                              Object object,
                                              Field field) throws IllegalAccessException {
        rowData.addValue(
                field.getType(),
                KeyMapper.to(field.getName()),
                ValueMapper.to(field.get(object))
        );
    }


    private void addValue(Class cls, String key, String value) {
        String existed = values.get(key);
        if (existed != null) {
            throw new InvalidEntityException(cls,
                    String.format("Duplicated Key found in Column Family: [ %s ]",
                            cls.getName()));
        }
        values.put(key, value);
    }

    private static <T extends Entity> RowData of(Class<? extends Entity> entityCls, T entity) {
        String cfName = getColumnFamilyName(entityCls);
        Field rowkey = getRowKeyColumn(entityCls);
        RowData rowData = null;
        try {
            rowData = new RowData(cfName,
                    KeyMapper.to(rowkey.getName()),
                    ValueMapper.to(rowkey.get(entity))
            );
            if (entity != null) {
                parseValues(entity, rowData);
            }

        } catch (IllegalAccessException e) {
            throw new InvalidEntityException(entityCls,
                    e.getMessage());
        }
        return rowData;
    }

    private static String getColumnFamilyName(Class<? extends Entity> cls) {
        ColumnFamily anno = cls.getAnnotation(ColumnFamily.class);
        if (anno == null) {
            throw new InvalidEntityException(cls);
        }
        return anno.value();
    }

    private static Field getRowKeyColumn(Class<? extends Entity> cls) {
        for (Field field : cls.getFields()) {
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {
                return field;
            }
        }
        throw new InvalidEntityException(cls,
                "Missing @Id fidld!");
    }

}

class InvalidEntityException extends RuntimeException {
    @Getter
    private final Class cls;

    InvalidEntityException(Class cls) {
        super(String.format("Invalid RowData: [ %s ]!", cls.getName()));
        this.cls = cls;
    }

    InvalidEntityException(Class cls, String msg) {
        super(msg);
        this.cls = cls;
    }
}
