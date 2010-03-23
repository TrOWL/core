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
public class H2DB extends DB {
    /**
     *
     */
    protected String dbURL = "jdbc:h2:~/";
    private static final String DEFAULT_DATABASE = "trowl2_default";
    private String dbdriver = "org.h2.Driver";

    /**
     *
     */
    public H2DB() {
        super();
    }
    
    /**
     *
     * @param repository
     */
    public H2DB(String repository) {
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
    public boolean connect() throws Exception {
            Class.forName(dbdriver);
            dbCon = DriverManager.getConnection(this.getDbURL());
            stmt = dbCon.createStatement();
        

        this.checkTables();

        return true;
    }
}
