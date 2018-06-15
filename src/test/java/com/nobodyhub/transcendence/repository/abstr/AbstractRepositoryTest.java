package com.nobodyhub.transcendence.repository.abstr;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.nobodyhub.transcendence.repository.rowdata.RowDataRepository;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author yan_h
 * @since 2018/6/15
 */
public class AbstractRepositoryTest {
    @Test
    public void testCql() {
        RowDataRepository repository = new RowDataRepository();
        assertEquals(" ALTER TABLE tbl  ADD col text ",
                repository.addColumnCql("tbl", "col"));
        assertEquals(" CREATE TABLE IF NOT EXISTS tbl (  idCol text PRIMARY KEY  ) ",
                repository.createTableCql("tbl", "idCol"));
        assertEquals(" DROP TABLE tbl ",
                repository.dropTableCql("tbl"));
        Map<String, String> values = Maps.newTreeMap();
        values.put("val1", "20180615");
        values.put("DEC123", "20180615");
        values.put("DATE20180615", "{{}}");
        assertEquals(" UPDATE cfName   SET DATE20180615='{{}}' ,  SET DEC123='20180615' ,  SET val1='20180615'  WHERE rowKeyName='rowKey'",
                repository.updateCql("cfName",
                        "rowKeyName",
                        "rowKey",
                        values));

        assertEquals(" SELECT col2, col1  FROM cfName  WHERE rowKeyName = 'rowKey' ",
                repository.selectCql("cfName",
                        "rowKeyName",
                        "rowKey",
                        Sets.newHashSet("col1", "col2")));
    }

}