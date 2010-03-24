/*
 * This file is part of TrOWL.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

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
