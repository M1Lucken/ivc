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
    
    public void resetTables() {
    	String sql = "DELETE FROM students";
    	try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	String sql2 = "DELETE FROM majors";
    	try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql2)) {
    		pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	String sql3 = "DELETE FROM courses";
    	try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql3)) {
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
    					if(k < course.length) course[j] = course[j].concat(",");
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
    
    public void addStudentData() {
    	String splitBy = ",";
    	int split = 0;
    	int check = 0;  
    	
    	try {
       	BufferedReader reader = new BufferedReader(new FileReader("StudentRoster.csv"));
    	List<String> lines = new ArrayList<>();
    	String line = null;
    	int listCount = 0;
    	while ((line = reader.readLine()) != null) {
    	 	if(!line.contains("Student Name")) lines.add(line);
    	 	listCount++;
    	}    	
    		
    	
       	for(int i = 0; i < listCount-1; i++) {
    		String[] insertVal = new String[7];
        	int ctr = 0;    		
    		String[] student = lines.get(i).split(splitBy);    		
    		for(int j = 0; j < student.length; j++) {
    			
    			if(student[j].contains("\"")) {    				
    				 for(int k = j+1; k < student.length; k++) {
    					if(k < student.length) student[j] = student[j].concat(",");
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
    		//replaced ^ with this method to follow other table insert syntax
    		String sql = "INSERT INTO students(name,address,major,dept,pin,taken,perm) VALUES(?,?,?,?,?,?,?)";

            try (Connection conn = this.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setString(1, insertVal[0]); //name
            	pstmt.setString(2, insertVal[1]); //address
                pstmt.setString(3, insertVal[2]); //major
                pstmt.setString(4, insertVal[3]); //department
                pstmt.setString(5, insertVal[4]); //pin
                pstmt.setString(6, insertVal[5]); //taken
                pstmt.setString(7, insertVal[6]); //perm
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
    					if(k < major.length) major[j] = major[j].concat(",");     					
    					major[j] = major[j].concat(major[k]);
    					//major[j] = major[j].substring(0,major[j].length()-1);
    					
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
    		String sql = "INSERT INTO majors(qyear,mname,mandatory,electives,min) VALUES(?,?,?,?,?)";

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
    
    public void listAllCourses(String perm) {
    	String sql = "SELECT taken FROM students WHERE perm = ?";
    	try (Connection conn = this.connect();
                
        		PreparedStatement pstmt  = conn.prepareStatement(sql)){            
            // set the value
            pstmt.setString(1,perm);           
            ResultSet rs  = pstmt.executeQuery();
            System.out.println(rs.getString("taken") + "\n");
    	} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	
    }
    
    
    public void generateCourseListing(String permNumber){
        String sql = "SELECT taken FROM students WHERE perm = " + permNumber;
        String taken_courses = "";
        String[] Courses = null;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             
                while(rs.next()) { // Note there will only be one result
                    String s = rs.getString("taken");
                    taken_courses = s.replace("\"", ""); // Remove the quotes
                    Courses = taken_courses.split(", ");
                }
             }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        int counter = 0;
        for(int i = 0; i < Courses.length; i++){
            if(Courses[i].contains(": IP")){
                counter++;
            }
        }

        int counter2 = 0;
        String str = "";
        for(int i = 0; i < Courses.length; i++){
            if(Courses[i].contains(": IP")){
                str += Courses[i];
                counter2++;
                if(counter2 != counter){
                    str += ", ";
                }
            }
        }

        System.out.println(str);
    }
    
    
    public void dropCourse(String permNumber, String enrollCode){

        String sql = "SELECT taken FROM students WHERE perm = " + permNumber;
        // Look up the taken courses field. Count the number of courses that are being taken in Fall 2022. Ensure that at least 2
        // courses like that exist for this student.
        // Then, update the student info in the students field by remove the correct course info from the taken field.

        String taken_courses = "";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             
                while(rs.next()) { // Note there will only be one result
                    String s = rs.getString("taken");
                    taken_courses = s.replace("\"", ""); // Remove the quotes
                    taken_courses = taken_courses.replace(",", "");
                }
             }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String[] Courses = taken_courses.split(" ");
       
        int counter = 0;
        for(int i = 0; i < Courses.length; i++){
            if(Courses[i].equals("IP")){
                counter++;
            }
        }

        boolean current = false;
        String tester = enrollCode + ":";
        if(counter >= 2){
            int start = -1;
            int end = -1;
            int lastValidIndex = -1;
            for(int i = 0; i < Courses.length; i++){
                if(Courses[i].equals(tester)){
                    if(i != 0 && Courses[i-1].substring(Courses[i-1].length()-1).equals(":")){
                            // If the string before this ends in a colon then I know for a fact that it is the Year and Quarter information
                            // for this course. If not, then it's the grade of the previous course.
                            start = i-2;
                    }
                    else{
                            start = i;
                    }
                    end = i + 1;
                    if(Courses[i+1].equals("IP")){
                        current = true;
                    }
                }
            }

            lastValidIndex = Courses.length - 1;
            if(end == Courses.length - 1){
                lastValidIndex = start - 1;
            }

            if(current){ // This course is a course that is being taken currently.
                String str = "\"";
                for(int i = 0; i < Courses.length; i++){
                    if(i < start || i > end){
                        str += Courses[i];
                        if(Courses[i].length() <= 3 && i < lastValidIndex){ // This is the grade of the course and the next course starts after this
                            str += ", ";
                        }
                        else if (i < lastValidIndex){
                            str += " ";
                        }
                        else{}
                    }
                }
                str += "\"";

                String sql2 = "UPDATE students SET taken = ?" + "WHERE perm = ?";
                try (Connection conn = this.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql2)){
                    pstmt.setString(1, str);
                    pstmt.setString(2, permNumber);
                   
                    pstmt.executeUpdate();

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
    public boolean verifyPin(String perm, String upin) {
    	String pinno = null;
    	//get pin from student, get pin from db, compare
    	String sql = "SELECT pin FROM students WHERE perm = ?";    	
    	try (Connection conn = this.connect();    			
    			PreparedStatement pstmt  = conn.prepareStatement(sql)){    		
    		pstmt.setString(1,perm);
    		ResultSet rs  = pstmt.executeQuery();
    		pinno = rs.getString("pin");    		    		   		   		   		
    	}	catch (SQLException e) {
            	System.out.println(e.getMessage());
        }
    	if(pinno == null) return false;
    	if((Integer.compare(Integer.valueOf(upin),Integer.valueOf(pinno))) == 0) return true;
    	else return false; 
    	
    }
    
    public String getName(String perm) {
    	String name = null;
    	String sql = "SELECT name FROM students WHERE perm = ?";    	
    	try (Connection conn = this.connect();    			
    			PreparedStatement pstmt  = conn.prepareStatement(sql)){    		
    		pstmt.setString(1,perm);
    		ResultSet rs  = pstmt.executeQuery();
    		name = rs.getString("name");
    		   		   		   		
    	}	catch (SQLException e) {
            	System.out.println(e.getMessage());
        }
       	return name;
    }
    
    public void changePin(String perm, String newP) {
    	if(!(perm.length() == 4)) return;
    	String sql = "UPDATE students SET pin = ?"
    			+ "WHERE perm = ?";
    	    	   	
    	try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
        	pstmt.setString(1, newP);
            pstmt.setString(2, perm);
            
            pstmt.executeUpdate();
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    	
    	
    }
    
    public int currCourseNum(String perm) {
    	int count = 0;
    	String ip = "IP";
    	String taken = null;
    	
    	//get current courses taken for student
    	String sql = "SELECT taken FROM students WHERE perm = ?";    	
    	try (Connection conn = this.connect();    			
    			PreparedStatement pstmt  = conn.prepareStatement(sql)){    		
    		pstmt.setString(1,perm);
    		ResultSet rs  = pstmt.executeQuery();
    		taken = rs.getString("taken");
    		   		   		   		
    	}	catch (SQLException e) {
            	System.out.println(e.getMessage());
        }    	
    	if(taken.contains(ip)) {
    		String[] words = taken.replaceAll("\\p{Punct}", "").split(" ");
    		for (int i=0; i < words.length; i++)
    		    if (words[i].equals(ip))
    		        count++;
        	}    	
    	return count;
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
    	
    	if(currCourseNum(perm) > 4) {
			System.out.print("Already enrolled in five courses currently!");
			return;
		}
    	
    	if(taken.contains(cnum)){
			System.out.print("Cannot add course you've already enrolled in!");
			return;    		
		}
    	
    	//parse prereq
    	if(prereq != null) {
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
    	}
    	//update courses taken for student
    	String addC = qyear + ": " + cnum + ": IP\"";
    	//remove " from end of taken
    	taken = taken.substring(0,taken.length()-1);
    	taken = taken.concat(", ");
    	taken = taken.concat(addC);
    	//System.out.print("\n New taken attri.: " + taken + "\n");
    	
    	//make sure there's a " at the beginning of multi-item list
    	String var = "\"";
    	if(taken.substring(0,1) != var) {
    		taken = var.concat(taken);
    	}
    	
    	String sql3 = "UPDATE students SET taken = ?"
    			+ "WHERE perm = ?";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql3)){
        	
        	pstmt.setString(1, taken);
            pstmt.setString(2, perm);
            
            pstmt.executeUpdate();
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        
        }
    	
    }
    
    public void previousQuarterGrades(String permNumber){
        String sql = "SELECT taken FROM students WHERE perm = " + permNumber;
        String taken_courses = "";
        String[] Courses = null;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             
                while(rs.next()) { // Note there will only be one result
                    String s = rs.getString("taken");
                    taken_courses = s.replace("\"", ""); // Remove the quotes
                    Courses = taken_courses.split(", ");
                }
             }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        List<String> grades = new ArrayList<String>();
        for(int i = 0; i < Courses.length; i++){
            String[] info = Courses[i].split(": ");
            if(info[0].equals("2022 Spring")){
                grades.add(Courses[i]);
            }
        }

        System.out.println("");
        for(int i = 0; i < grades.size(); i++){
            System.out.println(grades.get(i));
        }

    }
    
    public void generateStudentListForCourse(String enrollCode){

        String s1 = "";
        String s2 = "";

        String sql = "SELECT qyear, cnum FROM courses WHERE enroll = " + enrollCode;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
              
                while(rs.next()) { 
                    s1 = rs.getString("qyear");
                    s2 = rs.getString("cnum");
                }
             }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        String temp = s1 + ": " + s2 + ":";

        String sql2 = "SELECT name, taken FROM students";
        String taken_courses = "";
        String n = "";
        String[] Courses = null;
        List<String> students = new ArrayList<String>();

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql2)){
              
                while(rs.next()) { // Note there will only be one result
                    n = rs.getString("name");
                    taken_courses = rs.getString("taken");
                    if(taken_courses.contains(temp)){ // This line determines which values I pick.
                        students.add(n);
                    }
                }
             }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("");
        for(int i = 0; i < students.size(); i++){
            System.out.println(students.get(i));
            System.out.println("");
        }
    }
    
    public void emailEveryone() {
    	
    	int rs2 = 0;
      	String sql = "SELECT COUNT(*) FROM students";
    	try (Connection conn = this.connect();
                Statement stmt  = conn.createStatement();
                ResultSet rs    = stmt.executeQuery(sql)){
    			
    		//rs.getInt()
    		rs.next();
    	    rs2 = rs.getInt(1);
    	    
     	}catch(SQLException e){
    		System.out.println(e.getMessage());
    }
    	
    	for(int i = 0; i < rs2; i++) {
    		String sql2 = "SELECT name, perm, taken FROM students";
        	try (Connection conn = this.connect();
                    Statement stmt  = conn.createStatement();
                    ResultSet rs    = stmt.executeQuery(sql)){
        		
        		while(rs.next()) {
        			String perm = null;
        			System.out.print("\n");
        			System.out.println("Generating grade email for " + rs.getString("taken"));
        			perm = rs.getString("perm");
        					
        		}  
        	
        	} catch (SQLException e) {
	            System.out.println(e.getMessage());   			        
        	
        	}
    	
    	}
    	String[] perms = new String[rs2];
    	
    }
    
    //GOLD use
    public void reqCheck(String permNumber){
    	String sql = "SELECT major, taken FROM students WHERE perm = " + permNumber;
        String taken_courses = "";
        String[] Courses = null;
        String Major = "";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
              
                while(rs.next()) { // Note there will only be one result
                    Major = rs.getString("major");
                    String s = rs.getString("taken");
                    taken_courses = s.replace("\"", ""); // Remove the quotes
                    Courses = taken_courses.split(", ");
                }
             }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        List<String> courses = new ArrayList<String>();
        for(int i = 0; i < Courses.length; i++){
            String[] info = Courses[i].split(": ");
            if(info.length == 3){
                courses.add(info[1].replaceAll("\\s+", ""));
            }
            else{ // info.length == 2
                courses.add(info[0].replaceAll("\\s+", ""));
            }
        }

        

        String sql2 = "SELECT mandatory, electives, min FROM majors WHERE mname = \"" + Major + "\"";

        String mcourses = "";
        String[] mandatory_courses = null;
        String electives = "";
        String[] elective_courses = null;
        int minimum = 0;

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql2)){

                while(rs.next()) {
                    String s = rs.getString("mandatory");
                    mcourses = s.replace("\"", ""); // Remove the quotes
                    mandatory_courses = mcourses.split(", ");
                    

                    s = rs.getString("electives");
                    electives = s.replace("\"", "");
                    elective_courses = electives.split(", ");
                    

                    minimum = rs.getInt("min");
                }
             }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        List<String> mandatoryCoursesToComplete = new ArrayList<String>();
        for(int i = 0; i < mandatory_courses.length; i++){
            if(courses.contains(mandatory_courses[i])){
                // Do nothing.
            }
            else{
                mandatoryCoursesToComplete.add(mandatory_courses[i]);
            }
        }

        int counter = minimum;
        for(int i = 0; i < elective_courses.length; i++){
            if(courses.contains(elective_courses[i])){
                counter--;
            }
        }

        System.out.println("");
        if(mandatoryCoursesToComplete.size() == 0 && counter <= 0){
            System.out.println("Yes");
        }
        else{
            System.out.println("List of mandatory courses to complete:");
            for(int i = 0; i < mandatoryCoursesToComplete.size(); i++){
                System.out.println(mandatoryCoursesToComplete.get(i));
            }
            System.out.println("Number of electives to complete:");
            if(counter <= 0){
                System.out.println(0);
            }
            else{
                System.out.println(counter);
            }
        }
    }
    
    //registrar use
    public void requestTranscript(){
    	System.out.println("Transcript for student goes here");
    }
    
    //registrar use
    public void enterGrades(){
    	System.out.println("Enter grades prompt goes here");
    }
    
    //GOLD use
    public void makePlan(){
    	System.out.println("Study plan goes here");
    }
        
    public static void main(String[] args) throws Exception {
    	System.out.println("Starting main application for IVC DBMS...\n\n");   	    	
    	
    	MainApp app = new MainApp();    	
       	
    	System.out.print("<<< SELECT INTERFACE >>>\n");    	
    	System.out.print("Enter \"g\" for GOLD, \"r\" for Registrar: ");    	
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
    				while(!(pin.length() == 4)) {
    					System.out.print("\nInvalid Pin. ");
    					System.out.print("\nEnter PIN: ");
    					pin = System.console().readLine();
    				}
    				//verify pin matches perm, exit if false
    				
    				if(app.verifyPin(perm,pin)) {
    					String stuname = app.getName(perm);
    					System.out.print("\nLogin Successful. Welcome back, " + stuname + ".\n");
    				}
    				else {  
    					System.out.print("\nIncorrect Login!");
    					System.exit(0);    					
    				}
    				
    				while(true) {
    				System.out.print("\nOPERATIONS\n");
    				System.out.print("0 | Exit GOLD \n1 | Add course \n2 | Drop course \n3 | List currently enrolled courses \n4 | List grades from "
    						+ "previous quarter\n5 | Requirements check\n6 | Generate study plan\n7 | Change PIN\n");
    				System.out.print("Choose the operation desired by entering its corresponding number from the list above: ");
    				int choice = Integer.valueOf(System.console().readLine());
    				
    				switch(choice) {
    				case 0:
    					System.out.print("\nExiting.");
    					System.exit(0);    					
    				case 1:
    					System.out.print("\nEnter enrollment code for course to add: ");
    					String enrollcode = System.console().readLine();
    					app.addCourse(perm, enrollcode);
    					break;
    				case 2:
    					System.out.print("\nEnter course number for class to drop: ");
    					enrollcode = System.console().readLine();
    					//enrollcode var name but actually uses "cnum"
    					app.dropCourse(perm, enrollcode);
    					break;
    				case 3:
    					System.out.print("\nCurrently enrolled courses: \n");
    					app.generateCourseListing(perm);    					
    					break;
    				case 4:
    					System.out.print("\nList grades from last quarter");    					
    					app.previousQuarterGrades(perm);
    					break;
    				case 5:
    					System.out.print("\nRequirements check");
    					app.reqCheck(perm);
    					break;
    				case 6:
    					System.out.print("\nMake a study plan");
    					app.makePlan();
    					break;
    				case 7:
    					System.out.print("\nEnter new PIN: ");
    					String newP = System.console().readLine();
    					app.changePin(perm, newP);
    					break;    				   				
    				}
    				System.out.print("\n<<=======================================================>>\n");
    				}
    				
    			case "r":
    				//Registrar
    				System.out.print("Starting Registrar... \n\n");
    				   				
    				while(true) {
    				System.out.print("\nOPERATIONS\n");
    				System.out.print("0 | Exit Registrar \n1 | Add student to course \n2 | Drop student from course \n3 | List all courses a student has taken\n4 | List student grades from "
    						+ "previous quarter\n5 | Generate class list\n6 | Enter course grades\n7 | Print student transcript\n8 | Generate grade mailer for all students\n9 | Load course, major, student data from CSV\n10 | Delete all database data\n");
    				System.out.print("Choose the operation desired by entering its corresponding number from the list above: ");
    				int choice2 = Integer.valueOf(System.console().readLine());
    				switch(choice2) {
    				case 0:
    					System.out.print("\nExiting.");
    					System.exit(0);
    				case 1:
    					System.out.print("\nEnter enroll code for course: ");
    					String enrollcode = System.console().readLine();
    					System.out.print("\nEnter perm number of student to add: ");
    					String sPerm = System.console().readLine();
    					app.addCourse(sPerm, enrollcode);
    					break;
    				case 2:
    					System.out.print("\nEnter course number for class: ");
    					enrollcode = System.console().readLine();
    					System.out.print("\nEnter perm number of student to drop: ");
    					sPerm = System.console().readLine();
    					app.dropCourse(sPerm, enrollcode);
    					break;
    				case 3:
    					System.out.print("\nEnter perm number of student to list courses taken: ");
    					sPerm = System.console().readLine();
    					app.listAllCourses(sPerm);
    					break;
    				case 4:
    					System.out.print("\nEnter perm number of student to list grades of previous quarter: ");
    					sPerm = System.console().readLine();
    					app.previousQuarterGrades(sPerm); 				
    					break;
    				case 5:
    					System.out.print("\nEnter enroll code of course to generate class list: ");
    					enrollcode = System.console().readLine();
    					app.generateStudentListForCourse(enrollcode);
    					break;
    				case 6:
    					System.out.print("\nEnter enroll code for class to add grades to: ");
    					System.out.print("\nEnter file name: ");
    					app.enterGrades();
    					break;
    				case 7:
    					System.out.print("\nEnter perm number of student to print transcript for: ");
    					app.requestTranscript();
    					break;
    				case 8:
    					System.out.print("\nEmailing everyone their grades!");
    					app.emailEveryone();
    					break; 
    				case 9:
    					System.out.print("\nLoading course, major, and student data into database...");
    					app.addMajorData();
    			      	app.addCourseData();
    			      	app.addStudentData();
    			      	break;
    				case 10:
    					System.out.print("\nDeleting course, major, and student data from database...");
    					app.resetTables();
    				}
    				System.out.print("\n<<--------------------------------------------------->>\n");
    				}
    				
    		}
    	//}
    	/**
    	else {
    		System.out.print("Invalid Input");
    		System.exit(0);
    	}
    	*/        
    }

}