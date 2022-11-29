package net.sqlitetutorial;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;  
import java.util.*;



public class MainApp {
/**
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
    	String url = "jdbc:sqlite:C:/sqlite/db/ivc.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    
    /**
     * select all rows in a fixed table
     */
    public void sSelectAll(){
        String sql = "SELECT name,address,major,dept,pin,taken,perm FROM students";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("name") +  "\t" + 
                                   rs.getString("address") + "\t" +
                                   rs.getString("major") + "\t" +
                                   rs.getString("dept") + "\t" +
                                   rs.getString("pin") + "\t" +
                                   rs.getString("taken") + "\t" +
                                   rs.getString("perm"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
   
    /**
     * basic select query
     
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
    */
    
    
    public void sInsert(String name, String address, String major, String dept, String pin, String taken, String perm) {
        String sql = "INSERT INTO students(name,address,major,dept,pin,taken,perm) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setString(1, name);
        	pstmt.setString(2, address);
            pstmt.setString(3, major);
            pstmt.setString(4, dept);
            pstmt.setString(5, pin);
            pstmt.setString(6, taken);
            pstmt.setString(7, perm);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void sUpdate(String name, String address, String major, String dept, String pin, String taken, String perm) {
        String sql = "UPDATE students SET name = ?,"
        		+ "address = ?"
        		+ "major = ?"
        		+ "dept = ?"
        		+ "pin = ?"
        		+ "taken = ?"
        		+ "WHERE perm = ?";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
        	pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, major);
            pstmt.setString(4, dept);
            pstmt.setString(5, pin);
            pstmt.setString(6, taken);
            pstmt.setString(7, perm);
            
            pstmt.executeUpdate();
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public void sDeleteAll() {
        String sql = "DELETE FROM students WHERE perm > ?";
        
        int perm = 0;
        
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setInt(1, perm);
        	//pstmt.setString(2, name);
            //pstmt.setString(3, major);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public static void main(String[] args) throws Exception {
    	System.out.println("Running MainApp.java for IVC DBMS...\t");
    	MainApp app = new MainApp();
    	
    	BufferedReader reader = new BufferedReader(new FileReader("StudentRoster.csv"));
    	List<String> lines = new ArrayList<>();
    	String line = null;
    	int listCount = 0;
    	while ((line = reader.readLine()) != null) {
    	 	if(!line.contains("Student Name")) lines.add(line);
    	 	listCount++;
    	}

    	
    	String splitBy = ",";
    	int split = 0;
    	int check = 0;
    	
    	
    	//for(int i = 0; i < listCount-1; i++) {
    	for(int i = 0; i < 12; i++) {
    		String[] insertVal = new String[7];
        	int ctr = 0;
    		//System.out.println("original line: " + lines.get(i));
    		String[] student = lines.get(i).split(splitBy);
    		
    		for(int j = 0; j < student.length; j++) {
    			//System.out.println(j + " index \t");

    			if(student[j].contains("\"")) {
    				
    				//System.out.println("concat start w/: " + student[j]);
    				
    				 for(int k = j+1; k < student.length; k++) {
    					
    					student[j] = student[j].concat(student[k]);
    					
    					split++;
    					
    					if(student[k].contains("\"")) check = 1;
    					if(check == 1) break;
    				 }
    			}
    			
    			insertVal[ctr] = student[j];
    			ctr++;
    			
    			j = j + split;
    			split = 0;
    			check = 0;
    			    			
    			
    			
    			//System.out.println("fixed member: " + student[j]);
    		}
    		
    		for(int c = 0; c < 7; c++) {
    			//if(insertVal != null) System.out.println(insertVal[c] + "\t");
    		}
    		app.sInsert(insertVal[0],insertVal[1],insertVal[2],insertVal[3],insertVal[4],insertVal[5],insertVal[6]);
    		
    	}
    	
    	
    	/**
    	//read in test CSV datasets
    	String line = "";
    	String splitBy = ",";
    	
    	//load student roster
    	try {
    		BufferedReader br = new BufferedReader(new FileReader("simple.csv"));
    		int tLine = 0;
    		while((line = br.readLine())!= null){
    			
    			String[] student = line.split(splitBy);
    			
    			for(int i = 0; i < student.length; i++) {
    				System.out.println(student[i] + " index = " + i + "\t");
    			}
    			
    			//fix "" issue for strings in CSV as using comma for separator does not produce intended results
    			for(int i = 0; i < 7; i++) {
    				
    				if(student[i].contains("\"")) {
    					
    					int index = 0;
    					int breaknext = 0;
    					for(int j = i+1; j < student.length; j++) {
    						if(breaknext == 1) break;
    						student[i] = student[i].concat(student[j]);
    						index = j;
    						
    						
    						if(student[j+1].contains("\"")) breaknext = 1;
    					}
    					
    					for(int k = i+1; k < (student.length - index -  3); k++) {
    						System.out.println("debug " + student[k] + " set to " + student[k+1]);
    						student[k] = student [k+1];
    					}
    					
    					//System.out.println(student[index] + " index is " + index + "\t");	
    				}
   
    				System.out.println(student[i] + " index = " + i + "\t");
    			}
    			
    			//debug print string
    			//System.out.println("Name: " + student[0] + "|" + "Address: " + student[1] + "|"  + "Major: " + student[2] + "|"  + "Department: " + student[3] + "|"  + "PIN: " + student[4] + "|"  + "Taken: " + student[5] + "|"  + "Perm Number: " + student[6] + "|"  + "\t");
    			
    			//verify header line is skipped
    			if(tLine == 1 && student.length == 7) {
    				app.sInsert(student[0], student[1], student[2], student[3], student[4], student[5], student[6]);
    			}
    			
    			tLine = 1;
    		}
    	}
    	
    	catch (IOException e) {
    		e.printStackTrace();
    	}
        
        */
        
        //app.sInsert("Ted Willis","474 Mayflower Avenue Bemidji, MN 56601","Astronomy","Natural Sciences","3532","2021 Fall: COMM1A: A, 2022 Winter: COMM1B: A, 2022 Spring: COMM1C: A, 2022 Fall: ASTR1: IP","2803084");
        //app.sInsert("Edna Schwartz","44 Acacia St. Central Islip, NY 11722","Astronomy","Natural Sciences","4031","2022 Fall: ASTR1: IP","3524808");
        //app.sInsert("Patricia Peters","7335 Arnold Street West Haven, CT 06516","Astronomy","Natural Sciences","6624","2022 Fall: ASTR1: IP","3888454");
        
    	//use to reset students table 
    	//app.sDeleteAll();
    	
        //app.sSelectAll();
        
        
    }

}