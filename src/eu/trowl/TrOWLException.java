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

package eu.trowl;

/**
 *
 * @author ed
 */
public class TrOWLException extends RuntimeException {

    /**
     * Creates a new instance of <code>TrOWLException</code> without detail message.
     */
    public TrOWLException() {
    }

    /**
     *
     * @param e
     */
    public TrOWLException(Exception e) {
    }


    /**
     * Constructs an instance of <code>TrOWLException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TrOWLException(String msg) {
        super(msg);
    }
}
