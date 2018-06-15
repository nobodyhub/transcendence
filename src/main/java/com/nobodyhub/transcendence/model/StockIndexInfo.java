package com.nobodyhub.transcendence.model;

import com.google.common.collect.Maps;
import com.nobodyhub.transcendence.model.abstr.Entity;
import com.nobodyhub.transcendence.model.annotation.ColumnFamily;
import com.nobodyhub.transcendence.model.annotation.ColumnMap;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

/**
 * Stock price information
 * <p>
 * {@link Entity#id} is used for Stock code, e.g. sh000001
 *
 * @author yan_h
 * @since 2018/6/15
 */
@Data
@ColumnFamily("stock_info_index")
public class StockIndexInfo extends Entity {
    @ColumnMap(keyCls = LocalDate.class, valCls = StockIndexSet.class)
    protected Map<LocalDate, StockIndexSet> indices = Maps.newHashMap();

    public void addPriceIndex(StockIndexSet indice) {
        indices.put(indice.getDate(), indice);
    }
}
