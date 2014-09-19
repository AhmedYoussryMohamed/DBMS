package TEST;
import static org.junit.Assert.*;
import DBMS.*;
import JDBC.*;
import TEST.*;

import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import com.sun.xml.internal.ws.addressing.ProblemAction;


public class DriverTest {

	@Test
	public void test() {
//		fail("Not yet implemented");
		
		Driver_IF drive = new Driver_IF();
		try{
			String url = System.getenv("ahmed") + "test\\";
			String url2 = "aujshdsah";
			
			boolean t1 = drive.acceptsURL(url);
			assertEquals( t1 , true );
			
			Connection_IF check = new Connection_IF(url);
			
			Properties info = new Properties();
			info.put( "username" ,"admin" );
			info.put( "password" , "admin" );
			
			Connection_IF con = drive.connect( url , info);
			assertEquals( con.getUrl() , check.getUrl() );
			assertEquals( con.getStandaradPassWord() , con.getStandaradPassWord() );
			assertEquals( con.getStandaradUserName() , check.getStandaradUserName() );
			
			System.out.println("Done");
			
		}catch( SQLException e ){
			
		}
		
		
	}//end test()

}//end class.
