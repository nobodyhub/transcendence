package com.nobodyhub.transcendence.repository.rowdata;

import com.nobodyhub.transcendence.model.StockBasicInfo;
import com.nobodyhub.transcendence.model.StockIndexInfo;
import com.nobodyhub.transcendence.model.StockIndexSet;
import com.nobodyhub.transcendence.repository.mapper.KeyMapper;
import com.nobodyhub.transcendence.repository.util.ClassHelper;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018/6/15
 */
public class RowDataTest {
    private StockBasicInfo stockBasicInfo;
    private StockIndexInfo stockIndexInfo;
    private StockIndexSet indice;

    @Before
    public void setUp() {
        stockBasicInfo = new StockBasicInfo();
        stockBasicInfo.setId("sh0000001");
        stockBasicInfo.setName("上证指数");

        stockIndexInfo = new StockIndexInfo();
        stockIndexInfo.setId("sh0000001");
        indice = StockIndexSet.of(LocalDate.of(2018, 6, 15));
        indice.setVolume(new BigDecimal("12039.232"));
        indice.setOpen(new BigDecimal("129.22"));
        stockIndexInfo.addPriceIndex(indice);

    }

    @Test
    public void testOf() {
        RowData rowData = RowData.of(stockBasicInfo.getClass());
        assertEquals("stock_info_basic", rowData.getCfName());
        assertEquals("id", rowData.getRowKeyName());
        assertEquals(null, rowData.getRowKey());
        assertEquals(0, rowData.getValues().size());

        rowData = RowData.of(stockBasicInfo);
        assertEquals("stock_info_basic", rowData.getCfName());
        assertEquals("id", rowData.getRowKeyName());
        assertEquals("sh0000001", rowData.getRowKey());
        assertEquals(1, rowData.getValues().size());
        assertEquals("上证指数", rowData.getValue("name"));

        rowData = RowData.of(stockIndexInfo.getClass());
        assertEquals("stock_info_index", rowData.getCfName());
        assertEquals("id", rowData.getRowKeyName());
        assertEquals(null, rowData.getRowKey());
        assertEquals(0, rowData.getValues().size());

        rowData = RowData.of(stockIndexInfo);
        assertEquals("stock_info_index", rowData.getCfName());
        assertEquals("id", rowData.getRowKeyName());
        assertEquals("sh0000001", rowData.getRowKey());
        assertEquals("{\"date\":20180615,\"volume\":12039.232,\"open\":129.22,\"high\":null,\"low\":null,\"close\":null,\"chg\":null,\"percent\":null,\"turnoverrate\":null,\"ma5\":null,\"ma10\":null,\"ma20\":null,\"ma30\":null,\"dea\":null,\"dif\":null,\"macd\":null,\"ub\":null,\"lb\":null,\"mid\":null,\"kdjk\":null,\"kdjd\":null,\"kdjj\":null,\"rsi1\":null,\"rsi2\":null,\"rsi3\":null,\"wr6\":null,\"wr10\":null,\"bias1\":null,\"bias2\":null,\"bias3\":null,\"cci\":null,\"psy\":null,\"psym\":null}", rowData.getValue(
                KeyMapper.to(LocalDate.of(2018, 6, 15))));
    }

    @Test
    public void testFillValue() throws IllegalAccessException {
        RowData rowData = RowData.of(stockBasicInfo);
        StockBasicInfo stockBasicInfoEntity = new StockBasicInfo();
        for (Field field : ClassHelper.getFields(stockBasicInfoEntity.getClass())) {
            rowData.fillField(field, stockBasicInfoEntity);
            assertEquals(field.get(stockBasicInfo), field.get(stockBasicInfoEntity));
        }

        rowData = RowData.of(stockIndexInfo);
        StockIndexInfo stockIndexInfoEntity = new StockIndexInfo();
        stockIndexInfoEntity.addPriceIndex(StockIndexSet.of(LocalDate.of(2018, 6, 15)));
        for (Field field : ClassHelper.getFields(stockIndexInfoEntity.getClass())) {
            rowData.fillField(field, stockIndexInfoEntity);
            assertEquals(field.get(stockIndexInfo), field.get(stockIndexInfoEntity));
        }

    }

}