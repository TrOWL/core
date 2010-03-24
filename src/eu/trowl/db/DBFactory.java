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

package eu.trowl.db;

/**
 *
 * @author ed
 */
public class DBFactory {
    /**
     *
     */
    public static final int POSTGRES = 1;
    /**
     *
     */
    public static final int H2 = 1;

    /**
     *
     * @param repository
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static DB construct(String repository) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
//        if (System.getProperty("Database.Driver") != null) {
//            Class driver = Class.forName(System.getProperty("Database.Driver"));
//            DB database = (DB) driver.newInstance();
//           database.setRepository(repository);
//            return database;
//        }
        return new H2DB(repository);
    }

    /**
     *
     * @return
     * @throws InstantiationException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    public static DB construct() throws InstantiationException, ClassNotFoundException, IllegalAccessException {
        //if (System.getProperty("Database.Driver") != null) {
        //    Class driver = Class.forName(System.getProperty("Database.Driver"));
        //    DB database = (DB) driver.newInstance();
        //    database.setRepository(System.getProperty("Database.Repository"));
        //    return database;
       // }
        return new H2DB();
    }
}
