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

/*
 * QueryDatabaseException.java
 *
 * Created on 14 August 2006, 13:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package eu.trowl.query;

/**
 *
 * @author ethomas
 */
public class QueryDatabaseException extends QueryException {
    
    /**
     * Creates a new instance of <code>QueryDatabaseException</code> without detail message.
     */
    public QueryDatabaseException() {
    }
    
    
    /**
     * Constructs an instance of <code>QueryDatabaseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public QueryDatabaseException(String msg) {
        super(msg);
    }
    
    /**
     *
     * @param cause
     */
    public QueryDatabaseException(Throwable cause) {
        super(cause);
    }
    
    /**
     *
     * @param message
     * @param cause
     */
    public QueryDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
