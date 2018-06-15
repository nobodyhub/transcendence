package com.nobodyhub.transcendence.repository.rowdata;

import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.nobodyhub.transcendence.model.abstr.Entity;
import com.nobodyhub.transcendence.repository.abstr.AbstractRepository;
import com.nobodyhub.transcendence.repository.util.ClassHelper;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;

/**
 * @author yan_h
 * @since 2018/6/12
 */
@Repository
public class RowDataRepository extends AbstractRepository {

    @Override
    public void createTable(Class<? extends Entity> entityCls) {
        RowData rowData = RowData.of(entityCls);
        this.session.execute(createTableCql(rowData.getCfName(), rowData.getRowKeyName()));
    }

    @Override
    public void dropTable(Class<? extends Entity> entityCls) {
        RowData rowData = RowData.of(entityCls);
        this.session.execute(dropTableCql(rowData.getCfName()));
    }

    @Override
    public void update(Entity entity) {
        RowData rowData = RowData.of(entity);
        addColumns(rowData.getCfName(), rowData.getColumnNames());
        if (!rowData.getValues().isEmpty()) {
            this.session.execute(updateCql(rowData.getCfName(),
                    rowData.getRowKeyName(),
                    rowData.getRowKey(),
                    rowData.getValues())
            );
        }
    }

    @Override
    public void query(Entity entity) {
        RowData rowData = RowData.of(entity);
        addColumns(rowData.getCfName(), rowData.getColumnNames());
        ResultSet resultSet = this.session.execute(selectCql(rowData.getCfName(),
                rowData.getRowKeyName(),
                rowData.getRowKey(),
                rowData.getColumnNames())
        );
        for (Row row : resultSet) {
            ColumnDefinitions colDefs = row.getColumnDefinitions();
            for (ColumnDefinitions.Definition colDef : colDefs) {
                String colName = colDef.getName();
                rowData.addValue(colName, row.getString(colName));
            }
        }
        for (Field field : ClassHelper.getFields(entity.getClass())) {
            rowData.fillField(field, entity);
        }
    }
}
