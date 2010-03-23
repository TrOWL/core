/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author ed
 */
public class PostgresDB extends DB {
    private static final String DEFAULT_DATABASE = "trowl2_default";
    private String dbURL = "jdbc:postgresql://127.0.0.1:5432/";
    private String dbdriver = "org.postgresql.Driver";
    private static final String username = "ontosearch";
    private static final String password = "zliy7gef";

    /**
     *
     */
    public PostgresDB() {
        super();
    }
    
    /**
     *
     * @param repository
     */
    public PostgresDB(String repository) {
        super(repository);
    }

    /**
     *
     * @return
     */
    public String getDbURL() {
        return this.dbURL + this.database;
    }

    /**
     *
     * @param newValue
     */
    public void setDbURL(String newValue) {
        this.dbURL = newValue;
    }


    /**
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings({"static-access"})
    public boolean connect() throws Exception {
            Class.forName(dbdriver);
            
            dbCon = DriverManager.getConnection(this.getDbURL(), PostgresDB.username, PostgresDB.password);
            stmt = dbCon.createStatement();
                    System.out.println("Checking tables exist...");
this.checkTables();
        return true;
    }
}
