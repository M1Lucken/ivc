# Isla Vista College
A command-line interface and database system for students and 
faculty for a fictional university in Isla Vista, CA. 
Implemented with Java & SQLite using JDBC. 


#Dependencies

Java JDK-19

Java libraries:  
Java.SQL 
Java.io


# Setup

Install SQLite3

Install JAVA JDK

Download the latest JDBC driver, place with CSV's in /sqlite/java/connect/ directory.

Place MainApp.java in /sqlite/java/connect/net/sqlitetutorial

Create the ivc database, ivc.db, in sqlite/db/ directory
Create three tables:

CREATE TABLE students ( name TEXT NOT NULL, 
address TEXT,
major TEXT NOT NULL,
dept TEXT,
pin CHAR(4) NOT NULL UNIQUE,
taken TEXT,
perm CHAR(7) PRIMARY KEY NOT NULL UNIQUE,
FOREIGN KEY(major) REFERENCES majors(mname) );

CREATE TABLE courses ( cnum CHAR(7) NOT NULL,
enroll TEXT PRIMARY KEY NOT NULL UNIQUE,
qyear TEXT NOT NULL,
ctitle CHAR(20) NOT NULL,
prof CHAR(31),
loc CHAR(9),
time TEXT,
max INT,
prereq TEXT );

CREATE TABLE majors ( qyear TEXT, 
mname TEXT PRIMARY KEY NOT NULL UNIQUE,
mandatory TEXT,
electives TEXT,
min INT);


# Usage

Use following command in terminal in the sqlite/java/connect directory:
java -cp ".;sqlite-jdbc-3.40.0.0.jar" net.sqlitetutorial.MainApp

Follow instructions in command-line, use '0' to exit. 

