package TEST;
import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;
import DBMS.*;
import JDBC.*;
import TEST.*;
import DBMS.*;
import JDBC.*;
import TEST.*;


public class ConnectionTest {

	@Test
	public void test() {
//		fail("Not yet implemented");
		
		String url = System.getenv("ahmed") + "test\\";
		
		try{
			//first Scenario.
			Connection_IF con = new Connection_IF( url );
			boolean t1 = con.checkClose();
			assertEquals( t1 , true );
			Statement_IF st = con.createStatement();
			assertEquals(st , new Statement_IF("url"));
			
			con = new Connection_IF("asfhasfh");
			boolean t2 = con.checkClose();
			assertEquals( t2 , false );
			
			st = con.createStatement();
			
			System.out.println("Done");
		}catch( SQLException e ){
			System.out.println("SQl Exception : " +  e.getMessage() );
		}
		
		
	}//end test()
	
}//end class.
