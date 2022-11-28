package net.sqlitetutorial;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 *
 * @author sqlitetutorial.net
 */
public class SelectApp {

    /**
     * Connect to the test.db database
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
     * select all rows in the warehouses table
     */
    public void selectAll(){
        String sql = "SELECT perm, name, major FROM students";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("perm") +  "\t" + 
                                   rs.getString("name") + "\t" +
                                   rs.getString("major"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
   
    /**
     * @param args the command line arguments
     */
    public void getPermGreaterThan(int perm){
               String sql = "SELECT perm, name, major "
                          + "FROM students WHERE perm > ?";
        
        try (Connection conn = this.connect();
             
        		PreparedStatement pstmt  = conn.prepareStatement(sql)){
            
            // set the value
            pstmt.setInt(1,perm);
            //
            ResultSet rs  = pstmt.executeQuery();
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("perm") +  "\t" + 
                                   rs.getString("name") + "\t" +
                                   rs.getString("major"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void main(String[] args) {
    	System.out.println("Select ALL tuples in students \t");
    	
    	SelectApp app = new SelectApp();
        app.selectAll();
        
        System.out.println("Select all perms greater than 1948392 \t");
        
        SelectApp app2 = new SelectApp();
        app2.getPermGreaterThan(1948392);
    }

}