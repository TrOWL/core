package eu.trowl.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sttaylor
 */
public class ResultSetMetaData {
    private List<String> columnNames;
    
    /**
     *
     */
    public ResultSetMetaData() {
        columnNames = new ArrayList<String>();
    }
    
    /**
     *
     * @param columnNames
     */
    public ResultSetMetaData(List<String> columnNames) {
        this.columnNames = columnNames;
    }
    
    /**
     *
     * @return
     */
    public int getColumnCount() {
        return columnNames.size();
    }
    
    /**
     *
     * @param index
     * @return
     */
    public String getColumnName(int index) {
        return columnNames.get(index - 1);
    }
    
    private void addColumnName(String columnName) {
        columnNames.add(columnName);
    }
    
    /**
     *
     * @param sqlrsmd
     * @return
     * @throws SQLException
     */
    public static ResultSetMetaData fromSQLResultSetMetaData(java.sql.ResultSetMetaData sqlrsmd) throws SQLException {
        ResultSetMetaData rsmd = new ResultSetMetaData();
  
        for (int i = 1; i <= sqlrsmd.getColumnCount(); i++) {
            rsmd.addColumnName(sqlrsmd.getColumnLabel(i));
        }
        return rsmd;
    }
    
}
