package com.nobodyhub.transcendence.repository.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nobodyhub.transcendence.repository.annotation.Column;
import com.nobodyhub.transcendence.repository.annotation.ColumnFamily;
import com.nobodyhub.transcendence.repository.annotation.Id;
import com.nobodyhub.transcendence.repository.entity.mapper.KeyMapper;
import com.nobodyhub.transcendence.repository.entity.mapper.ValueMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.List;
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
     * Column Family
     */
    @Getter
    private final String cfName;
    /**
     * Row Key
     */
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private String id;
    /**
     * Column key/value
     */
    private Map<String, String> values = Maps.newHashMap();

    /**
     * Create from an entity
     *
     * @param entity
     * @return
     */
    public static RowData of(Entity entity) {
        ColumnFamily anno = entity.getClass().getAnnotation(ColumnFamily.class);
        if (anno == null) {
            throw new InvalidEntityException(entity.getClass());
        }
        RowData rowData = new RowData(anno.value());

        // can have at most 1 map field
        int nMapField = 0;
        for (Field field : entity.getClass().getFields()) {
            Class<?> fldClass = field.getType();
            try {
                Id idAnno = field.getAnnotation(Id.class);
                if (idAnno != null) {
                    rowData.setId(ValueMapper.to(field.get(entity)));
                    continue;
                }
                Column colAnno = field.getAnnotation(Column.class);
                if (colAnno != null) {
                    if (fldClass.isAssignableFrom(Set.class)) {
                        parseSetField(rowData, entity, field);
                    } else if (fldClass.isAssignableFrom(List.class)) {
                        parseListField(rowData, entity, field);
                    } else if (fldClass.isAssignableFrom(Map.class)) {
                        nMapField++;
                        if (nMapField > 1) {
                            throw new InvalidEntityException(entity.getClass(),
                                    "Can have at most one @ColumnMap field!");
                        }
                        parseMapField(rowData, entity, field);
                    } else {
                        parseField(rowData, entity, field);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new InvalidEntityException(entity.getClass(),
                        e.getMessage());
            }
        }
        if (rowData.getId() == null) {
            throw new InvalidEntityException(entity.getClass(),
                    "Missing @Id fidld!");
        }
        return rowData;
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
     * Get values of Set field
     *
     * @param fieldName field name
     * @param eCls      element Class
     * @param <T>       element type
     * @return
     */
    public <T> Set<T> getSetField(String fieldName, Class<T> eCls) {
        Set<T> result = Sets.newHashSet();
        String colNm = KeyMapper.to(fieldName);
        Pattern patter = Pattern.compile("^" + colNm + "_\\d+$");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (patter.matcher(entry.getKey()).matches()) {
                result.add(ValueMapper.from(entry.getValue(), eCls));
            }
        }
        return result;
    }

    /**
     * Get values of List field
     *
     * @param fieldName field name
     * @param eCls      element class
     * @param <T>       element type
     * @return
     */
    public <T> List<T> getListField(String fieldName, Class<T> eCls) {
        List<T> result = Lists.newArrayList();
        String colNm = KeyMapper.to(fieldName);
        Pattern patter = Pattern.compile("^" + colNm + "_\\d+$");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (patter.matcher(entry.getKey()).matches()) {
                result.add(ValueMapper.from(entry.getValue(), eCls));
            }
        }
        return result;
    }

    private static void parseSetField(RowData rowData,
                                      Object object,
                                      Field field) throws IllegalAccessException {
        Set values = (Set) field.get(object);
        String colNm = KeyMapper.to(field.getName());
        int idx = 1;
        for (Object value : values) {
            rowData.addValue(
                    field.getType(),
                    String.format("%s_%d", colNm, idx),
                    ValueMapper.to(value));
        }
    }

    private static void parseListField(RowData rowData,
                                       Object object,
                                       Field field) throws IllegalAccessException {
        List values = (List) field.get(object);
        String colNm = KeyMapper.to(field.getName());
        int idx = 1;
        for (Object value : values) {
            rowData.addValue(
                    field.getType(),
                    String.format("%s_%d", colNm, idx),
                    ValueMapper.to(value));
        }
    }

    private static void parseMapField(RowData rowData,
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

    private static void parseField(RowData rowData,
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
