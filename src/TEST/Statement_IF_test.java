package TEST;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;
import DBMS.*;
import JDBC.*;
import TEST.*;


public class Statement_IF_test {
	@Test
	public void test() throws SQLException {
		String url = System.getenv("ahmed") +"test\\";
		Statement_IF stmt = new Statement_IF(url);
		// -----------------------------------------------------------------------
		// testing method (addBatch).
		String query1 = "create database company";
		String query2 = "create table Persons (FirstName VARCHAR, LastName Varchar, Age varchar)";
		String query3 = "INSERT INTO Persons (FirstName, LastName, Age) VALUES ('Omar', 'Mahmoud',20)";
		String query4 = "INSERT INTO Persons (FirstName, LastName, Age) VALUES ('Ahmed', 'Yosry',20)";
		String query5 = "INSERT INTO Persons (FirstName, LastName, Age) VALUES ('Yahia', 'Hisham',21)";
		String query6 = "SELECT FirstName , LastName ,age FROM Persons WHERE FirstName = omar";
		stmt.addBatch(query1);
		stmt.addBatch(query2);
		stmt.addBatch(query3);
		stmt.addBatch(query4);
		stmt.addBatch(query5);
		ArrayList<String> list = new ArrayList<String>();
		list.add(query1);
		list.add(query2);
		list.add(query3);
		list.add(query4);
		list.add(query5);
		boolean diff = false;
		for (int i = 0; i < list.size(); i++) {
			assertEquals(list.get(i),stmt.getList().get(i)); 
		}
		// -----------------------------------------------------
		// testing method (clearBatch).
		stmt.clearBatch();
		boolean test = true;
		if (stmt.getList().size() > 0) {
			test = false;
		}
		assertEquals(test, true);
		stmt.clearBatch();
		// ------------------------------------------------------------------
		// testing method (executeBatch).
		String s1 = "create database microsoft";
		String s2 = "create table Per (FirstName VARCHAR, LastName Varchar, Age varchar)";
		String s3 = "INSERT INTO Per (FirstName, LastName, Age) VALUES ('Omar', 'Mahmoud',20)";
		String s4 = "INSERT INTO Per (FirstName, LastName, Age) VALUES ('Ahmed', 'Yosry',20)";
		String s5 = "INSERT INTO Per (FirstName, LastName, Age) VALUES ('Yahia', 'Hisham',21)";
		String s6 = "SELECT FirstName , LastName ,Age FROM Per WHERE FirstName = mar";
		stmt.addBatch(s1);
		stmt.addBatch(s2);
		stmt.addBatch(s3);
		stmt.addBatch(s4);
		stmt.addBatch(s5);
		stmt.addBatch(s6);
		int[] result=stmt.executeBatch();
		int [] correct= new int[6];
		correct[0]=0;
		correct[1]=0;
		correct[2]=0;
		correct[3]=0;
		correct[4]=0;
		correct[5]=1;
		for(int i=0;i<6;i++){
			assertEquals(result[0], correct[0]);
		}
		stmt.clearBatch();
		// ------------------------------------------------------------------
		// testing method (execute).
		stmt.addBatch(query1);
		stmt.addBatch(query2);
		stmt.addBatch(query3);
		stmt.executeBatch();
		test = stmt.execute(query3);
		assertEquals(test, false);
		test = stmt.execute(query6);
		assertEquals(true, test);
		stmt.clearBatch();
		// ------------------------------------------------------------------
		// testing method (setQueryTimeout).
		stmt.setQueryTimeout(2);
		assertEquals(stmt.getQueryTimeout(), 2);
		stmt.clearBatch();
		// ------------------------------------------------------------------
		// testing method (executeUpdate).
		String q1 = "create database unilever";
		String q2 = "create table workers (FirstName VARCHAR, LastName Varchar, Age varchar)";
		String q3 = "INSERT INTO workers (FirstName, LastName, Age) VALUES ('Omar', 'Mahmoud',20)";
		String q4 = "INSERT INTO workers (FirstName, LastName, Age) VALUES ('Omar', 'Mahmoud',20)";
		String q5 = "INSERT INTO workers (FirstName, LastName, Age) VALUES ('Yahia', 'Hisham',21)";
		String q6 = "SELECT FirstName , LastName ,Age FROM workers WHERE FirstName = Omar";
		stmt.clearBatch();
		stmt.addBatch(q1);
		stmt.addBatch(q2);
		stmt.addBatch(q3);
		stmt.addBatch(q4);
		stmt.addBatch(q5);
		stmt.executeBatch();
		assertEquals(stmt.executeUpdate(q6), 2);
		stmt.clearBatch();
		// ------------------------------------------------------------------
		// testing method (close).
		stmt.close();
		assertEquals(stmt.getCheckConnection(), false);

	}

}
