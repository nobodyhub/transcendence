package com.nobodyhub.transcendence.repository.model;

import com.google.common.collect.Maps;
import com.nobodyhub.transcendence.repository.model.abstr.Entity;
import com.nobodyhub.transcendence.repository.model.annotation.ColumnFamily;
import com.nobodyhub.transcendence.repository.model.annotation.ColumnMap;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stock price information
 * <p>
 * {@link Entity#id} is used for Stock code, e.g. sh000001
 *
 * @author yan_h
 * @since 2018/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ColumnFamily("stock_info_index")
public class StockIndexInfo extends Entity<StockIndexInfo> {
    /**
     * daily index information
     */
    @ColumnMap(keyCls = LocalDate.class, valCls = StockIndexSet.class)
    protected Map<LocalDate, StockIndexSet> indices = Maps.newHashMap();

    public void addPriceIndex(StockIndexSet indice) {
        indices.put(indice.getDate(), indice);
    }

    public void merge(List<StockIndexInfo> others) {
        others.forEach(other -> {
            for (StockIndexSet indexSet : other.getIndices().values()) {
                if (indexSet != null) {
                    addPriceIndex(indexSet);
                }
            }
        });
    }

    /**
     * Get {@link this#indices} as a list from latest to the ealiest
     *
     * @return sorted list of index
     */
    public List<StockIndexSet> getIndexList() {
        return indices.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }


    @Override
    public StockIndexInfo build() {
        StockIndexInfo indexInfo = new StockIndexInfo();
        indices.keySet().forEach(
                key -> indexInfo.addPriceIndex(StockIndexSet.of(key)));
        return indexInfo;
    }
}
