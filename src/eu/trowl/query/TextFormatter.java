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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.query;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ed
 */
public class TextFormatter extends ResultSetFormatter {

    private StringBuffer out;

    /**
     *
     * @param rs
     */
    public TextFormatter(ResultSet rs) {
        super(rs);
    }

    /**
     *
     * @return
     */
    public String format() {
        out = new StringBuffer();

        int cols = rs.getMetaData().getColumnCount();
        List<Integer> maxWidth = new ArrayList<Integer>();

        for (int i = 1; i <= cols; i++) {
            maxWidth.add(new Integer(rs.getMetaData().getColumnName(i).length()));
        }

        while (rs.next()) {
            for (int i = 1; i <= cols; i++) {
                if (maxWidth.get(i-1) < rs.getString(i).length()) {
                    maxWidth.set(i-1, new Integer(rs.getString(i).length()));
                }
            }
        }

        int total = totalWidth(maxWidth);

        for (int i = 1; i <= cols; i++) {
            buf(rs.getMetaData().getColumnName(i), maxWidth.get(i-1));
            out.append(' ');
                if (i < cols) {
                    sep();
                    out.append(' ');
                }
        }

        out.append('\n');
        repeat('-', total);

        rs.first();

        while (rs.next()) {
            for (int i = 1; i <= cols; i++) {
                buf(rs.getString(i), maxWidth.get(i-1));
                out.append(' ');
                if (i < cols) {
                    sep();
                    out.append(' ');
                }
            }

            out.append('\n');
        }
        repeat('-', total);

        return out.toString();
    }

    private void repeat(char what, int howmany) {
        for (int i = 0; i <
                howmany; i++) {
            out.append(what);
        }
        out.append('\n');
    }

    private int totalWidth(List<Integer> w) {
        int t = 0; //start with 1 for left hand "|"

        for (int i = 0; i <
                w.size(); i++) {
            t += 2; // minimum width 2 for every column, space, then pipe
            t +=
                    w.get(i);
        }

        return t;
    }

    private void sep() {
        out.append("|");
    }

    private void buf(String what, int len) {
        out.append(what);
        for (int i = what.length(); i <
                len; i++) {
            out.append(' ');
        }
    }
}
