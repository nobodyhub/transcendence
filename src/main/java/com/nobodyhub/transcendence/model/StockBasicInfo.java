package com.nobodyhub.transcendence.model;

import com.nobodyhub.transcendence.model.abstr.Entity;
import com.nobodyhub.transcendence.model.annotation.Column;
import com.nobodyhub.transcendence.model.annotation.ColumnFamily;
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
public class StockBasicInfo extends Entity {
    /**
     * stock name
     */
    @Column
    private String name;
}
