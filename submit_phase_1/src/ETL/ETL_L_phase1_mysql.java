import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	static final String URL="jdbc:mysql://localhost/"; 
	static final String USER="simida";
	static final String PASSWORD="password";
	static final String filename="tmp-result.txt";

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		Connection conn=null;
		
		try {
			conn = DriverManager.getConnection(URL,USER,PASSWORD);
			Statement myStatement = conn.createStatement();
			myStatement.executeUpdate("DROP DATABASE twitter");
			myStatement.executeUpdate("CREATE DATABASE twitter");
			myStatement.executeUpdate("USE twitter");
			myStatement.executeUpdate("CREATE Table " +
					"twitters ( id INT AUTO_INCREMENT, " +
							   "userid VARCHAR(20), " +
							   "timestamp VARCHAR(20), " +
							   "twitterid VARCHAR(20), PRIMARY KEY (id) )");
			
			String userid,time,twitterid;
			
			File file = new File(filename);
			BufferedReader reader = null;
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			int count=0;
			while ((line = reader.readLine())!=null) {
				Scanner s = new Scanner(line);
				s.useDelimiter("\\s*\t\\s*");
				time=s.next();
				userid=s.next();
				twitterid=s.next();
				s.close();
				myStatement.executeUpdate("INSERT INTO twitters (userid, timestamp, twitterid) " +
						  "values ('"+userid+"','"+time+"','"+twitterid+"')");
				
				count++;
				if(count%5000==0) System.out.println(count);
			}
			System.out.println(count);
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
