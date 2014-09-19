package TEST;
import static org.junit.Assert.*;
import DBMS.*;
import JDBC.*;
import TEST.*;


import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

public class ResultSet_IF_Test {

	@Test
	public void test() throws SQLException {
		// creating database.
		Parser parsObject = new Parser();
		parsObject.setPathParser( System.getenv("ahmed") +"test\\" );
		parsObject.splitAll("CREATE DATABASE google");
		parsObject
				.splitAll("create table Persons (FirstName VARCHAR, LastName Varchar, Age int)");
		parsObject
				.splitAll("INSERT INTO Persons (FirstName, LastName, Age) VALUES ('Omar', 'Mahmoud',32)");
		parsObject
				.splitAll("INSERT INTO Persons (FirstName, LastName, Age) VALUES ('Omar', 'Mahmojhlud',32)");
		parsObject
				.splitAll("INSERT INTO Persons (FirstName, LastName, Age) VALUES ('yahia', 'hisham',21)");
		parsObject
				.splitAll("INSERT INTO Persons (FirstName, LastName, Age) VALUES ('ahmed', 'yousry',20)");
		parsObject
				.splitAll("INSERT INTO Persons (FirstName, LastName, Age) VALUES ('tarek', 'eltalawy',20)");
		parsObject
				.splitAll("SELECT FirstName , LastName ,Age FROM Persons WHERE Age = 32");
		// --------------------------------------------------
		String tableName = "Persons";
		String dbName = "microsoft";
		String url = "C:\\Users\\Omar\\Desktop\\Server\\";
		ArrayList<String>[] list = parsObject.getArrayList();
		ResultSet_IF rsTest = new ResultSet_IF(list, tableName, url, dbName);
		// ---------------------------------------------------------
		// Testing method(getList)----------------------------------
		ArrayList<String>[] listGet = rsTest.getList();
		for (int i = 0; i < list.length; i++) {
			for (int j = 0; j < listGet[0].size(); j++) {
				assertEquals(listGet[i].get(j), list[i].get(j));
			}
		}
		// -------------------------------------------------------------------
		// Testing method(absolute(int row))----------------------------------
		assertEquals(false, rsTest.absolute(5));
		assertEquals(true, rsTest.absolute(1));
		assertEquals(false, rsTest.absolute(0));
		assertEquals(false, rsTest.absolute(rsTest.getList()[0].size()));
		// -------------------------------------------------------------------
		// Testing method(afterLast())----------------------------------------
		rsTest.afterLast();
		assertEquals(rsTest.getList()[0].size(), rsTest.getCurrentRow());
		// -------------------------------------------------------------------
		// Testing method(beforeFirst())-------------------------------------
		rsTest.beforeFirst();
		assertEquals(0, rsTest.getCurrentRow());
		// -------------------------------------------------------------------
		// Testing method(getColumn())----------------------------------------
		assertEquals(1, rsTest.findColumn("FirstName"));
		assertEquals(2, rsTest.findColumn("LastName"));
		assertEquals(3, rsTest.findColumn("Age"));
		// -------------------------------------------------------------------
		// Testing method(next())-----------------------------------------
		rsTest.next();
		assertEquals(1, rsTest.getCurrentRow());
		rsTest.next();
		assertEquals(2, rsTest.getCurrentRow());
		// -------------------------------------------------------------------
		// Testing method(previous())-----------------------------------------
		rsTest.previous();
		assertEquals(1, rsTest.getCurrentRow());
		// -------------------------------------------------------------------
		// Testing method(getString(int columnIndex))-------------------------
		String name = "Omar";
		assertEquals(name, rsTest.getString(1));
		// -------------------------------------------------------------------
		// Testing method(getInt(String columnLabel))------------------------
		name = "Omar";
		assertEquals(name, rsTest.getString("FirstName"));
		// -------------------------------------------------------------------
		// Testing method(getInt(int columnIndex))----------------------------
		int age = 32;
		rsTest.next();
		System.out.println(rsTest.getCurrentRow());
		assertEquals(age, rsTest.getInt(3));
		// -------------------------------------------------------------------
		// Testing method(getInt(String columnLabel))-------------------------
		age = 32;
		assertEquals(age, rsTest.getInt("Age"));
		// -------------------------------------------------------------------
		// Testing method(getLong(int columnIndex))---------------------------
		long age1 = 32;  
		assertEquals(age1, rsTest.getInt(3));
		// -------------------------------------------------------------------
		// Testing method(getLong(String columnLabel))------------------------
		age1 = 32;
		assertEquals(age1, rsTest.getInt("Age"));
		// -------------------------------------------------------------------
		// Testing method(isClosed())-----------------------------------------
		rsTest.close();
		assertEquals(true, rsTest.isClosed());

	}

}
