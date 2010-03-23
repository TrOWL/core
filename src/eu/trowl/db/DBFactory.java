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
