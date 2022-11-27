public class ReadWrite
{   
public static void main(String[] args)    
{           
System.out.print("Enter perm number: ");   
//using console to input data from user   
String perm = System.console().readLine();   
System.out.println("You have entered: "+perm);  
int permno = Integer.valueOf(perm);

System.out.print("Enter name: ");   
String name = System.console().readLine();
System.out.println("You have entered: "+name);

System.out.print("Enter major: ");   
String major = System.console().readLine();
System.out.println("You have entered: "+major);
}   
}  