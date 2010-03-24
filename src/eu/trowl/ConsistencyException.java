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

package eu.trowl;

/**
 *
 * @author ethomas
 */
public class ConsistencyException extends Exception {

    /**
     * Creates a new instance of <code>ConsistencyException</code> without detail message.
     */
    public ConsistencyException() {
    }


    /**
     * Constructs an instance of <code>ConsistencyException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConsistencyException(String msg) {
        super(msg);
    }
}
