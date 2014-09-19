package TEST;
import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;
import DBMS.*;
import JDBC.*;
import TEST.*;


public class ResultSetMetaDataTest {

	@Test
	public void test() {
//		fail("Not yet implemented");
		
		String url = System.getenv("ahmed") + "test\\";
		
		Statement_IF st = new Statement_IF(url);
		
		try {
			//FirstScenario.
			st.execute("Create database test");
			st.execute("create table testTable(id int ,name varchar)");
			st.execute("insert into testTable(id,name) values(1,ahmed)");
			st.execute("insert into testTable(id,name) values(2,omar)");
			st.execute("insert into testTable(id,name) values(3,yahia)");
			st.execute("insert into testTable(id,name) values(4,mohamed)");
			
			ResultSet_IF rs = st.executeQuery("Select * From testTable where id > 0");
			
			ResultSetMetaData_IF rsmd = new ResultSetMetaData_IF( rs );	
			
			int colCount = rsmd.getColumnCount();
			assertEquals( colCount , rs.getList().length );
			
			String colName = rsmd.getColumnLabel( 1 );
			assertEquals( "id" ,colName );
			
			 colName = rsmd.getColumnName( 1 );
			assertEquals( "id" ,colName );
			
			int type = rsmd.getColumnType(1);
			assertEquals( type , 4 );
			
			String table = rsmd.getTableName( 1 );
			assertEquals( "testTable" , table );
			
			int nullStatus = rsmd.isNullable( 1 );
			assertEquals( 1 , nullStatus );
			
			boolean ai = rsmd.isAutoIncrement( 1 );
			assertEquals( false , ai );
			
			boolean w = rsmd.isWritable( 1 );
			assertEquals( true , w );
			
			boolean r = rsmd.isReadOnly( 1 );
			assertEquals( true , r );
			
			boolean s = rsmd.isSearchable( 1 );
			assertEquals( true , s );
			
			//SecondScenario.
			System.out.println("Second Scenario");
			st.execute("create table testTable2(id int autoincrement ,name varchar unsearchable )");
			st.execute("insert into testTable2(name) values(ahmed)");
			st.execute("insert into testTable2(name) values(omar)");
			st.execute("insert into testTable2(name) values(yahia)");
			st.execute("insert into testTable2(name) values(mohamed)");
			
			rs = st.executeQuery("Select * From testTable2 where id > 0");
			rsmd = new ResultSetMetaData_IF( rs );
			
			colCount = rsmd.getColumnCount();
			assertEquals( colCount , rs.getList().length );
			
			colName = rsmd.getColumnLabel( 1 );
			assertEquals( "id" ,colName );
			
			colName = rsmd.getColumnName( 1 );
			assertEquals( "id" ,colName );
			
			type = rsmd.getColumnType(2);
			assertEquals( type , 12 );
			
			table = rsmd.getTableName( 2 );
			assertEquals( "testTable2" , table );
			
			nullStatus = rsmd.isNullable( 1 );
			assertEquals( 1 , nullStatus );
			
			ai = rsmd.isAutoIncrement( 1 );
			assertEquals( true , ai );
			
			w = rsmd.isWritable( 1 );
			assertEquals( true , w );
			
			r = rsmd.isReadOnly( 1 );
			assertEquals( true , r );
			
			s = rsmd.isSearchable( 2 );
			assertEquals( false , s );
			
			System.out.println("Done");
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage() );
		}
		
		
		
	}//end test.

}//end class.
