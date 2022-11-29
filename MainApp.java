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
    
    public void addCourseData() {
    	String splitBy = ",";
    	int split = 0;
    	int check = 0;
    	
    	try {
    	BufferedReader reader2 = new BufferedReader(new FileReader("CourseHistory.csv"));
    	List<String> lines2 = new ArrayList<>();
    	String line2 = null;
    	int listCount2 = 0;
    	while ((line2 = reader2.readLine()) != null) {
    	 	if(!line2.contains("Course Number")) lines2.add(line2);
    	 	listCount2++;
    	}	
    	
    	
    	
    	for(int i = 0; i < listCount2-1; i++) {
    		String[] insertVal = new String[9];
        	int ctr = 0;    	
    		String[] course = lines2.get(i).split(splitBy);
    		    		
    		for(int j = 0; j < course.length; j++) {
    			if(course[j].contains("\"")) {   				
    				
    				 for(int k = j+1; k < course.length; k++) {
    					if(k < course.length - 1) course[j] = course[j].concat(",");
    					course[j] = course[j].concat(course[k]);
    					
    					split++;
    					
    					if(course[k].contains("\"")) check = 1;
    					if(check == 1) break;
    				 }
    			}
    			
    			insertVal[ctr] = course[j];
    			ctr++;
    			
    			j = j + split;
    			split = 0;
    			check = 0;
    			
    		}
    		
    		for(int c = 0; c < 9; c++) {
    			//if(insertVal != null) System.out.println(insertVal[c] + "\t");
    		}
    		
    		          
    		
    		String sql = "INSERT INTO courses(cnum,enroll,qyear,ctitle,prof,loc,time,max,prereq) VALUES(?,?,?,?,?,?,?,?,?)";

            try (Connection conn = this.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setString(1, insertVal[0]);
            	pstmt.setString(2, insertVal[1]);
                pstmt.setString(3, insertVal[2]);
                pstmt.setString(4, insertVal[3]);
                pstmt.setString(5, insertVal[4]);
                pstmt.setString(6, insertVal[5]);
                pstmt.setString(7, insertVal[6]);
                pstmt.setInt(8, Integer.valueOf(insertVal[7]));
                pstmt.setString(9, insertVal[8]);
                pstmt.executeUpdate();
                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    		
    	} 
    	} catch (Exception b) {
        		b.printStackTrace();
        	
    	}
    	
    }
    

    public void addMajorData() {
    	String splitBy = ",";
    	int split = 0;
    	int check = 0;
    	
    	try {
    	BufferedReader reader3 = new BufferedReader(new FileReader("Quarter_Major.csv"));
    	List<String> lines3 = new ArrayList<>();
    	String line3 = null;
    	int listCount3 = 0;
    	while ((line3 = reader3.readLine()) != null) {
    	 	if(!line3.contains("Current Quarter")) lines3.add(line3);
    	 	listCount3++;
    	}
    	for(int i = 0; i < listCount3-1; i++) {
    		String[] insertVal = new String[5];
        	int ctr = 0;    	
    		String[] major = lines3.get(i).split(splitBy);
    		    		
    		for(int j = 0; j < major.length; j++) {
    			if(major[j].contains("\"")) {   				
    				
    				 for(int k = j+1; k < major.length; k++) {
    					//if(k < major.length - 1) 
    					major[j] = major[j].concat(",");
    					major[j] = major[j].concat(major[k]);
    					major[j] = major[j].substring(0,major[j].length()-1);
    					
    					split++;
    					
    					if(major[k].contains("\"")) check = 1;
    					if(check == 1) break;
    				 }
    			}
    			
    			insertVal[ctr] = major[j];
    			ctr++;
    			
    			j = j + split;
    			split = 0;
    			check = 0;    			
    		}    		
    		for(int c = 0; c < 5; c++) {
    			//if(insertVal != null) System.out.println(insertVal[c] + "\t");
    		} 
    		String sql = "INSERT INTO majors(mname,dname,mandatory,electives,min) VALUES(?,?,?,?,?)";

            try (Connection conn = this.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setString(1, insertVal[0]);
            	pstmt.setString(2, insertVal[1]);
                pstmt.setString(3, insertVal[2]);
                pstmt.setString(4, insertVal[3]);
                pstmt.setInt(5, Integer.valueOf(insertVal[4]));                
                pstmt.executeUpdate();
                
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    		
    	} 
    	} catch (Exception b) {
        		b.printStackTrace();
        	
    	}
    	
    }
    
    public void addCourse(String perm, String enroll) {
    	String taken = null;
    	String prereq = null;
    	String cnum = null;
    	String qyear = null;
      	
    	//get current courses taken for student
    	String sql1 = "SELECT taken FROM students WHERE perm = ?";    	
    	try (Connection conn = this.connect();    			
    			PreparedStatement pstmt  = conn.prepareStatement(sql1)){    		
    		pstmt.setString(1,perm);
    		ResultSet rs  = pstmt.executeQuery();
    		taken = rs.getString("taken");
    		   		   		   		
    	}	catch (SQLException e) {
            	System.out.println(e.getMessage());
        }
    	
    	//lookup course info with enroll code from courses
    	//format "2021 Fall: COMM1A: A, 2022 Fall: ASTR1: IP"
    	String sql2 = "SELECT qyear, cnum, prereq FROM courses WHERE enroll = ?";    	
    	try (Connection conn = this.connect();    			
    			PreparedStatement pstmt  = conn.prepareStatement(sql2)){    		
    		pstmt.setString(1,enroll);
    		ResultSet rs  = pstmt.executeQuery();
    		cnum = rs.getString("cnum");
    		qyear = rs.getString("qyear");
    		prereq = rs.getString("prereq");    		   		   		   		
    	}	catch (SQLException e) {
            	System.out.println(e.getMessage());
        }
    	
    	//parse prereq
    	String splitBy = ", ";
    	String[] res = prereq.split(splitBy);
    	int takenReq = res.length;
    	//verify prerequisites satisfied
    	for(int i=0; i<res.length; i++) {    		
    		if(taken.contains(res[i])) {
    			takenReq--;
    		}
    		if(taken.contains(res[i] + ": IP")){
    			System.out.print("Cannot add course for which you are currently taking a prerequisite for!");
    			return;
    		}
     	}
    	if(takenReq > 0) {
    		System.out.print("Prerequisite not satisfied!");
    		return;
    	}
    	
    	//update courses taken for student
    	String addC = qyear + ": " + cnum + ": IP\"";
    	//remove " from end of taken
    	taken = taken.substring(0,taken.length()-1);
    	taken = taken.concat(", ");
    	taken = taken.concat(addC);
    	System.out.print("\n" + taken + "\n");
    	//String sql3 = "UPDATE students SET taken = ?,"
    			//+ "WHERE perm = ?";
    	
    }
    
    
    public static void main(String[] args) throws Exception {
    	System.out.println("Running MainApp.java for IVC DBMS...\n\n");
    	
    	MainApp app = new MainApp();
    	
    	System.out.print("<<< SELECT INTERFACE >>>\n\n");    	
    	System.out.print("Enter \"g\" for GOLD, \"r\" for Registrar\n");    	
    	String inter = System.console().readLine();
    	    	
    	//if(inter == "g" || inter == "r") {     	    	
    		switch(inter) {
    			case "g":
    				//GOLD
    				System.out.print("Starting GOLD... \n\n");
    				
    				System.out.print("Enter Perm Number: ");
    				String perm = System.console().readLine();
    				System.out.print("\nEnter PIN: ");
    				String pin = System.console().readLine();
    				
    				//verify pin matches perm, exit if false
    				
    				System.out.print("\nOPERATIONS\n");
    				System.out.print("1 | Add course \n2 | Drop course \n3 | List currently enrolled courses \n4 | List grades from "
    						+ "previous quarter\n5 | Requirements check\n6 | Generate study plan\n7 | Change PIN\n");
    				System.out.print("Choose the operation desired by entering its corresponding number from the above list: ");
    				int choice = Integer.valueOf(System.console().readLine());
    				
    				switch(choice) {
    				case 1:
    					System.out.print("Enter enrollment code: ");
    					String enrollcode = System.console().readLine();
    					app.addCourse(perm, enrollcode);
    					break;
    				case 2:
    					break;
    				case 3:
    					break;
    				case 4:
    					break;
    				case 5:
    					break;
    				case 6:
    					break;
    				case 7:
    					System.out.print("\nChange PIN\n");
    					break;    				   				
    				}
    				
    				break;
    			case "r":
    				//Registrar
    				System.out.print("Starting Registrar... \n\n");
    				   				
    				System.out.print("\nOPERATIONS\n");
    				System.out.print("1 | Add student to course \n2 | Drop student from course \n3 | List current student courses \n4 | List student grades from "
    						+ "previous quarter\n5 | Generate class list\n6 | Enter course grades\n7 | Print student transcript\n8 | Generate grade mailer for all students\n");
    				System.out.print("Choose the operation desired by entering its corresponding number from the above list: ");
    				int choice2 = Integer.valueOf(System.console().readLine());
    				switch(choice2) {
    				case 1:
    					break;
    				case 2:
    					break;
    				case 3:
    					break;
    				case 4:
    					break;
    				case 5:
    					break;
    				case 6:
    					break;
    				case 7:
    					break;
    				case 8:
    					System.out.print("\nEmail everyone!\n");
    					break; 
    				}
    				break;
    		}
    	//}
    	/**
    	else {
    		System.out.print("Invalid Input");
    		System.exit(0);
    	}
    	*/    	   	
    	
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
    	
       	for(int i = 0; i < listCount-1; i++) {
    		String[] insertVal = new String[7];
        	int ctr = 0;    		
    		String[] student = lines.get(i).split(splitBy);    		
    		for(int j = 0; j < student.length; j++) {
    			
    			if(student[j].contains("\"")) {    				
    				 for(int k = j+1; k < student.length; k++) {
    					if(k < student.length - 1) student[j] = student[j].concat(",");
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
    			
    		}
    		
    		for(int c = 0; c < 7; c++) {
    			//if(insertVal != null) System.out.println(insertVal[c] + "\t");
    		}
    		
    		//insert student roster into ivc.db via esql
    		//app.sInsert(insertVal[0],insertVal[1],insertVal[2],insertVal[3],insertVal[4],insertVal[5],insertVal[6]);
    		
    	}
    	    	
       	app.addCourseData();
    	app.addMajorData();
    	       
                
        //use to reset students table 
    	//app.sDeleteAll();
    	
        //app.sSelectAll();
       	            
    }

}