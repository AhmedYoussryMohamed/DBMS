package JDBC;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;


public class Driver_IF implements java.sql.Driver {
	private Connection_IF connect = null;
	static org.slf4j.Logger log=LoggerFactory.getLogger(Driver_IF.class);
	
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		// TODO Auto-generated method stub
		File f = new File( url );
		
		if( !f.exists() ){
			log.info("Database access error occurred.");
			throw ( new SQLException() );
		}
		
		return true;
	}

	@Override
	public Connection_IF connect(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub
		File f = new File( url );
		SQLException e = new SQLException( "Database access error in Driver class Driver" );
		
		if( !f.exists() ){
			log.info("Database access error in Driver class.");	
			throw e;
		}
		connect = new Connection_IF( url );
		if( !connect.configurationFile(url) ){
//			log.info(e.getStackTrace()+"");
			log.info("Database access error in Driver class.");
			throw e;
		}
		if( !info.get("username").equals( connect.getStandaradUserName() ) || !info.get("password").equals(connect.getStandaradPassWord()) ){
			log.info("Database access error in Driver class.");
			throw e;
		}
		return connect;
	}
	
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		// TODO Auto-generated method stub
		File f = new File( url );
		if( connect == null || !f.exists() ){
			log.info("connection is not established.");
			throw ( new SQLException() );
		}
		DriverPropertyInfo dp1 = new DriverPropertyInfo( "username" ,  connect.getStandaradUserName() );
		DriverPropertyInfo dp2 = new DriverPropertyInfo("password",  connect.getStandaradPassWord() );
		DriverPropertyInfo dp3 = new DriverPropertyInfo("url",  connect.getUrl() );
		
		DriverPropertyInfo[] arr = new DriverPropertyInfo[3];
		
		arr[0] = dp1;
		arr[1] = dp2;
		arr[2] = dp3;
		
		return arr;
	}
	
	//end----------------------------------------------------------------------------------------------
	@Override
	public int getMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean jdbcCompliant() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}//end class.
