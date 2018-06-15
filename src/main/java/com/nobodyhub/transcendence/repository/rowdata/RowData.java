package com.nobodyhub.transcendence.repository.rowdata;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nobodyhub.transcendence.model.abstr.Entity;
import com.nobodyhub.transcendence.model.annotation.Column;
import com.nobodyhub.transcendence.model.annotation.ColumnFamily;
import com.nobodyhub.transcendence.model.annotation.ColumnMap;
import com.nobodyhub.transcendence.model.annotation.Id;
import com.nobodyhub.transcendence.repository.mapper.KeyMapper;
import com.nobodyhub.transcendence.repository.mapper.ValueMapper;
import com.nobodyhub.transcendence.repository.util.ClassHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

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
    @Setter
    private String rowKey;
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
     * Fill the field of entity with corresponding values in {@link this#values}
     * <p>
     * in case of the Field is annotated with {@link ColumnMap}
     * keys should be included in the result entity in order to get values from {@link this#values}
     *
     * @param field
     * @param entity
     */
    public void fillField(Field field, Entity entity) {
        try {
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {
                field.set(entity,
                        ValueMapper.from(rowKey, field.getType()));
                return;
            }

            Column colAnno = field.getAnnotation(Column.class);
            if (colAnno != null) {
                String colNm = KeyMapper.to(field.getName());
                field.set(entity,
                        ValueMapper.from(values.get(colNm), field.getType()));
                return;
            }
            ColumnMap colMapAnno = field.getAnnotation(ColumnMap.class);
            if (colMapAnno != null) {
                //get empty map, with only keys but null values
                Map mapField = (Map) field.get(entity);
                // convert keyset to column names
                Set<String> colNames = Sets.newHashSet();
                for (Object key : mapField.keySet()) {
                    colNames.add(KeyMapper.to(key));
                }
                //read values and fill the map
                for (Map.Entry<String, String> entry : values.entrySet()) {
                    if (colNames.contains(entry.getKey())) {
                        mapField.put(
                                KeyMapper.from(entry.getKey(), colMapAnno.keyCls()),
                                ValueMapper.from(entry.getValue(), colMapAnno.valCls()));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new InvalidEntityException(entity.getClass(),
                    e.getMessage());
        }
    }

    /**
     * Add new value to values
     *
     * @param key
     * @param value
     */
    public void addValue(String key, String value) {
        values.put(key, value);
    }

    /**
     * get value from values for given key
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        return values.get(key);
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
        for (Field field : ClassHelper.getFields(entityCls)) {
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
                    KeyMapper.to(key),
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
                    KeyMapper.to(rowkey.getName()));
            if (entity != null) {
                rowData.setRowKey(ValueMapper.to(rowkey.get(entity)));
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
        for (Field field : ClassHelper.getFields(cls)) {
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
