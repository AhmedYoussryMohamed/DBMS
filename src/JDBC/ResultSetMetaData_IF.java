package JDBC;
import java.sql.SQLException;
import DBMS.*;
import JDBC.*;
import TEST.*;

import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ResultSetMetaData_IF implements java.sql.ResultSetMetaData {
	static Logger log=LoggerFactory.getLogger(ResultSetMetaData_IF.class);
	private ResultSet_IF resultSet = null;
	private boolean checkConnection = false;
	private String databaseName = "";
	private Parser p = new Parser();

	public ResultSetMetaData_IF( ResultSet_IF  obj ){
		this.resultSet = obj;
		checkConnection = true;
		databaseName = obj.getdbName();
		p.setPathParser( obj.getUrl() );
		p.setDb( databaseName );
	}//end method.
	
	@Override
	public int getColumnCount() throws SQLException {
		// TODO Auto-generated method stub	
		if( checkConnection == false || resultSet == null ){
			log.info("database access error occurs in getColumnCount");
			throw ( new SQLException() );
		}
		
		return resultSet.getList().length;
	}//end method.
	
	@Override
	public String getColumnLabel(int col) throws SQLException {
		// TODO Auto-generated method stub
		return getColumnName( col );
	}//end method.
	
	@Override
	public String getColumnName(int col) throws SQLException {
		// TODO Auto-generated method stub
		
		if( checkConnection == false || col <= 0 || col > resultSet.getList().length ){
			log.info("database access error occurs in getColumnName.");
			throw ( new SQLException("database access error occurs in getColumnName.") );
		}
		col--;
		return resultSet.getList()[col].get( 0 );
	}//end method.
	
	@Override
	public int getColumnType(int column) throws SQLException {
		// TODO Auto-generated method stub
		
		if( checkConnection == false || column <= 0 || column > resultSet.getList().length ){
			log.info("Error in getColumnType");
			throw ( new SQLException() );
		}
		
		String url = resultSet.getUrl();
		column--;
		String type = "";
		
		 type = p.getType( resultSet.getTableName() ,column , resultSet.getList() );
		Types t = null;
		if( type.equalsIgnoreCase("int")  ){
			return t.INTEGER;
		}else if( type.equalsIgnoreCase("double") ){
			return t.DOUBLE;
		}else if( type.equalsIgnoreCase("float") ){
			return t.FLOAT;
		}else if( type.equalsIgnoreCase("String") ){
			return t.VARCHAR;
		}else if( type.equalsIgnoreCase("long") ){
			//msh 3aref
			return t.BIGINT;
		}else if( type.equalsIgnoreCase("boolean") ){
			return t.BOOLEAN;
		}
		
		return -1;	
	}//end method.
	
	@Override
	public String getTableName(int col) throws SQLException {
		// TODO Auto-generated method stub
		if( checkConnection == false ){
			log.info("Error in getTableName");
			throw ( new SQLException(  ) );
		}
		return resultSet.getTableName();
	}
	
	//????? why int??
	@Override
	public int isNullable(int column) throws SQLException {
		// TODO Auto-generated method stub
		if( !checkConnection ){
			log.info("Error in isNullable");
			throw( new SQLException("Database access error.") );
		}
		
		String status = null;
		status = p.getStatus( resultSet.getTableName() , getColumnName(column) , "nullable" );
		
		if( status == null ){
			throw ( new SQLException("ERROR is Nullable.") );
		}
		if( status.equals("true") ){
			return 1;
		}
		
		return 0;
	}//end method.
	
	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		// TODO Auto-generated method stub
		if( !checkConnection ){
			log.info("Error in isAutoIncrement");
			throw( new SQLException() );
		}
		String status = null;
		status = p.getStatus( resultSet.getTableName() ,getColumnName(column) , "autoincrement" );
		if( status == null ){
			throw ( new SQLException("ERROR is Autoincrement.") );
		}
		if( status.equals("true") ){
			return true;
		}
		
		return false;
	}//end method
	
	@Override
	public boolean isReadOnly(int column) throws SQLException {
		// TODO Auto-generated method stub
		if( !checkConnection ){
			log.info("Error in isReadOnly");
			throw( new SQLException() );
		}
		String status = null;
		 status = p.getStatus( resultSet.getTableName() ,getColumnName(column) , "readable" );
		
		if( status.equals("true") ){
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isSearchable(int column) throws SQLException {
		// TODO Auto-generated method stub
		if( !checkConnection ){
			log.info("Error in isSearchable");
			throw( new SQLException() );
		}
		String status = null;
		 status = p.getStatus( resultSet.getTableName() ,getColumnName(column) , "searchable" );
		
		if( status.equals("true") ){
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isWritable(int column) throws SQLException {
		// TODO Auto-generated method stub
		if( !checkConnection ){
			log.info("Error in isWritable");
			throw( new SQLException() );
		}
		String status = null;
		 status = p.getStatus( resultSet.getTableName() ,getColumnName(column) , "writable" );
		
		if( status.equals("true") ){
			return true;
		}
		
		return false;
	}
	
	
	//--------------------------------------------------------------------------------Not Needed--------------------------------------------------
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCatalogName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnClassName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getColumnDisplaySize(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public String getColumnTypeName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPrecision(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScale(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSchemaName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCaseSensitive(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCurrency(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDefinitelyWritable(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isSigned(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	
}//end class.
