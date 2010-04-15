/*
 * This file is part of TrOWL.
 *
 * TrOWL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrOWL is distributed in the hope that it will be useful,
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

import eu.trowl.util.Types;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * it's not a java.sql.ResultSet, but it looks a bit like it.
 * @author sttaylor
 */
public class ResultSet {
    
    private List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

    private Map<Map<String, Object>, Float> rowRanks = new HashMap<Map<String, Object>, Float>();
    
    private ResultSetMetaData rsmd;
    
    private int cursor = -1;
    
    /**
     *
     */
    public ResultSet() {
        rsmd = new ResultSetMetaData();
    }

    /**
     *
     * @param rsmd
     */
    public ResultSet(ResultSetMetaData rsmd) {
        this.rsmd = rsmd;
    }
    
    private ResultSet(java.sql.ResultSetMetaData sqlrsmd) throws SQLException {
        this.rsmd = ResultSetMetaData.fromSQLResultSetMetaData(sqlrsmd);
    }
    
    /**
     *
     */
    public void beforeFirst() {
        cursor = -1;
    }
    
    /**
     *
     * @return
     */
    public boolean first() {
        if (!hasResult()) {
            beforeFirst();
            return false;
        } else {
            cursor = 0;
            return true;
        }
    }
    
    /**
     *
     * @return
     */
    public boolean next() {
        try {
            return (rows.get(++cursor) != null);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
    
    /**
     *
     * @return
     */
    public int getRow() {
        return (cursor + 1);
    }
    
    /**
     *
     * @return
     */
    public ResultSetMetaData getMetaData() {
        return rsmd;
    }
    
    /**
     *
     * @param o
     * @param row
     * @param column
     */
    public void addValue(Object o, int row, int column) {
        if (row > rows.size()) {
            rows.add(new LinkedHashMap<String, Object>());
            addValue(o, row, column);
        }
        else {
            rows.get(row - 1).put(rsmd.getColumnName(column), o);
        }
    }
    
    /**
     *
     * @param rank
     */
    public void setRank(float rank) {
        rowRanks.put(rows.get(cursor), rank);
    }
    
    /**
     *
     * @return
     */
    public float getRank() {
        return rowRanks.get(rows.get(cursor));
    }
    
    /**
     *
     * @return
     */
    public boolean hasRowRanks() {
        return (!rowRanks.isEmpty());
    }
    
    /**
     *
     */
    public void sortByRank() {
        Collections.sort(rows, new RankComparator());
    }
    
    /**
     *
     * @param columnName
     * @return
     */
    public Object getObject(String columnName) {
        return hasResult() ? rows.get(cursor).get(columnName) : null;
    }
    
    /**
     *
     * @param index
     * @return
     */
    public Object getObject(int index) {
        return getObject(rsmd.getColumnName(index));
    }
    
    /**
     *
     * @param columnName
     * @return
     */
    public String getString(String columnName) {
        return String.valueOf(getObject(columnName));
    }
    
    /**
     *
     * @param index
     * @return
     */
    public String getString(int index) {
        return getString(rsmd.getColumnName(index));
    }
    
    /**
     *
     * @param columnName
     * @return
     */
    public Double getDouble(String columnName) {
        return (Double)getObject(columnName);
    }
    
    /**
     *
     * @param index
     * @return
     */
    public Double getDouble(int index) {
        return getDouble(rsmd.getColumnName(index));
    }
    
    /**
     *
     * @return
     */
    public int size() {
	return rows.size();
    }
    
    private boolean hasResult() {
        return (rows.size() > 0);
    }
    
    private void addRow(Map<String, Object> row) {
        rows.add(row);
    }

    /**
     *
     * @param sqlrs
     * @return
     * @throws SQLException
     */
    public static ResultSet fromSQLResultSet(java.sql.ResultSet sqlrs) throws SQLException {
        ResultSet rs = new ResultSet(sqlrs.getMetaData());
        ResultSetMetaData rsmd = rs.getMetaData();
 
        int cols = rsmd.getColumnCount();
        while (sqlrs.next()) {
            Map<String, Object> row = new LinkedHashMap<String, Object>();
 
            for (int i = 1; i <= cols; i++)
                row.put(rsmd.getColumnName(i), sqlrs.getObject(i));

            rs.addRow(row);
        }
 
        return rs;
    }

    public Set<URI> getRowSources() {
        return Types.newSet();
    }
    
    private class RankComparator implements Comparator<Map<String, Object>> {
        public int compare(Map<String, Object> r1, Map<String, Object> r2) {
            return (- rowRanks.get(r1).compareTo(rowRanks.get(r2)));
        }
    }

}
