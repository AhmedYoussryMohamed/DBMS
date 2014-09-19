import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;
import DBMS.*;
import JDBC.*;
import TEST.*;

public class Main {

	public static void main(String[] args) {
		String url = System.getenv("ahmed");
		Connection_IF con;
		Statement_IF stmt;
		Driver_IF driver = new Driver_IF();
		Properties info = new Properties();
		info.put("username", "admin");
		info.put("password", "admin");
		try {
			con = driver.connect(url, info);
			stmt = con.createStatement();
			getInput(stmt);
			stmt.close();
			con.close();
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	}// end main.

	private static void getInput(Statement_IF stmt) throws SQLException {
		System.out.println("Enter 'Exit' to quit");
		Scanner sc = new Scanner(System.in);
		String query = "";
		while ( !query.equalsIgnoreCase( "exit" ) ) {
			try{
			query = sc.nextLine();
			if( query == null ){ query = ""; }
			if( query.equalsIgnoreCase("exit") ){ break; }
			if (isSelect(query)) {
				ResultSet_IF rs = stmt.executeQuery(query);
				printResultSet(rs);
			} else {
				stmt.execute(query);
			}
			}catch(SQLException e){  System.err.println("SQLException: " + e.getMessage()); };
		}//end while.
	}// end method.

	private static void printResultSet(ResultSet_IF rs) throws SQLException {
		int column = 1;
		ArrayList<String>[] list = rs.getList();
		ResultSetMetaData_IF rsmd = rs.getMetaData();
		int numOfCols = list.length;
		for( int i = 0 ;i < list.length ;i++){ System.out.print( rsmd.getColumnName( i + 1 ) ); if( i != (numOfCols-1) ){ System.out.print("  -  ");} }
		System.out.println();
		while ( rs.next() ) {
			for (int i = 0; i < numOfCols; i++) {
				System.out.print( rs.getString(column + i) );
				if( i != (numOfCols-1) ){
				System.out.print("  ,  ");	
				}
			}//end for i.
			System.out.println();
		}

	}// end method

	private static boolean isSelect(String query) {
		try{
			StringTokenizer st = new StringTokenizer(query);
			String first = st.nextToken();
			if (first.equalsIgnoreCase("Select")) {
				return true;
			}
		}catch( Exception e ){
			return false;
		}
		return false;
	}// end method.

}
