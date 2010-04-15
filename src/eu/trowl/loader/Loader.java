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

package eu.trowl.loader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import eu.trowl.db.SQLBuilder;
import eu.trowl.db.DB;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ed
 */
public abstract class Loader implements Runnable {
    /**
     *
     */
    public static final String ID = "LOADER";
    /**
     *
     */
    public static final boolean THREAD_SAFE = false;

    /**
     *
     */
    protected BufferedReader in;
    /**
     *
     */
    protected DB db;
    /**
     *
     */
    protected SQLBuilder out;
    /**
     *
     */
    protected static Set<Loader> workers = new HashSet<Loader>();
    private Long elapsed;
    private Long start;

    /**
     *
     * @param in
     * @param out
     */
    public Loader(Reader in, SQLBuilder out) {
        this.in = new BufferedReader(in);
        this.out = out;
    }

    /**
     *
     * @param input
     * @return
     * @throws IOException
     */
    protected static String readToString(BufferedReader input) throws IOException {
        StringBuilder data = new StringBuilder(1000);
        char[] buf = new char[1024];
        int readCount;
        while ((readCount = input.read(buf)) != -1) {
            data.append(buf, 0, readCount);
        }
        return data.toString();
    }

    /**
     *
     */
    public Loader() {
    }

    /**
     *
     * @param in
     */
    public void setIn(BufferedReader in) {
        this.in = in;
    }

    /**
     *
     * @param out
     */
    public void setOut(SQLBuilder out) {
        this.out = out;
    }

    /**
     *
     * @throws LoaderInitException
     */
    public abstract void init() throws LoaderInitException;
    public abstract void run();
    /**
     *
     */
    public abstract void finish();

    /**
     *
     * @return
     */
    public Long getElapsedTime() {
        if (elapsed != null) {
            return elapsed;
        } else if (start != null) {
            return new Long (System.currentTimeMillis() - start);
        }
        return new Long(0);
    }

    /**
     *
     */
    public void startTimer() {
        start = System.currentTimeMillis();
    }

    /**
     *
     */
    public void stopTimer() {
        elapsed = System.currentTimeMillis() - start;
    }
}
