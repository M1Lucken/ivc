package net.sqlitetutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 *
 * @author sqlitetutorial.net
 */
public class InsertApp {

    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C:/sqlite/db/chinook.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Insert a new row into the warehouses table
     *
     * @param name
     * @param capacity
     */
    public void insert(int perm, String name, String major) {
        String sql = "INSERT INTO students(perm,name,major) VALUES(?,?,?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setInt(1, perm);
        	pstmt.setString(2, name);
            pstmt.setString(3, major);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    	InsertApp app = new InsertApp();
        // insert three new rows
        app.insert(1948392,"Kayla Fletcher", "PSTAT");
        //app.insert(6390285,"Anton McDude", "COMM");
        //app.insert(6936635,"Maxim Rusky", "ECON");
    }

}