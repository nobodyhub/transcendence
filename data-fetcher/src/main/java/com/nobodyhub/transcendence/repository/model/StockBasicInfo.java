package com.nobodyhub.transcendence.repository.model;

import com.nobodyhub.transcendence.repository.model.abstr.Entity;
import com.nobodyhub.transcendence.repository.model.annotation.Column;
import com.nobodyhub.transcendence.repository.model.annotation.ColumnFamily;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Basic Inforamtion of Stock
 * <p>
 * {@link Entity#id} is used for Stock code, e.g. sh000001
 *
 * @author yan_h
 * @since 2018/6/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ColumnFamily("stock_info_basic")
public class StockBasicInfo extends Entity<StockBasicInfo> {
    /**
     * stock name
     */
    @Column
    private String name;

    @Override
    public StockBasicInfo build() {
        return new StockBasicInfo();
    }
}
