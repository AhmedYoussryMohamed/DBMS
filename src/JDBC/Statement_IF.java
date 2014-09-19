package JDBC;
import java.sql.*;
import DBMS.*;
import JDBC.*;
import TEST.*;

import java.util.ArrayList;

import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;

public class Statement_IF implements Statement {
	static Logger log=org.slf4j.LoggerFactory.getLogger(Statement_IF.class);
	private ArrayList<String> list;
	private Parser parsObject;
	private String url;
	private boolean checkConnection;
	private int time;
	private ResultSet_IF rst = null;

	public Statement_IF(String name) {
		list = new ArrayList<String>();
		parsObject = new Parser();
		url = name;
		parsObject.setPathParser(url);
		checkConnection = true;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		if (checkConnection == false) {
			log.info("could not add batch");
			throw (new SQLException());
		}
		list.add(sql);
	}

	@Override
	public void clearBatch() throws SQLException {
		if (checkConnection == false) {
			log.info("could not clear batch");
			throw (new SQLException());
		}
		list.clear();
		
	}

	@Override
	public void close() throws SQLException {
		
		if (checkConnection == false) {
			log.info("can not close .Connection already closed");
			throw (new SQLException());
		}
		parsObject.releaseDatabase();
		checkConnection = false;
		if( rst != null ){
			rst.close();
			rst = null;
		}
		
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		long begin = System.currentTimeMillis();
		if (checkConnection == false) {
			log.info("Cannot execute batch.Connection is closed");
			throw (new SQLException());
		}
		parsObject.splitAll(sql);
		ArrayList<String>[] list = parsObject.getArrayList();
		if (list == null) {
			return false;
		}
		long end = System.currentTimeMillis();
		if (time != 0 && (end - begin) > time) {
			log.info("Time exceeded");
			throw (new SQLException());
		}
		return true;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		long begin = System.currentTimeMillis();
		if (checkConnection == false) {
			log.info("could not execute batch.Connection is closed");
			throw (new SQLException());
		}

		int[] array = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			boolean valid = parsObject.splitAll(list.get(i));
			if (valid) {
				array[i] = parsObject.getCounter();
			} else {
				array[i] = 0;
			}
		}// end for(i).
		long end = System.currentTimeMillis();
		if (time != 0 && (end - begin) > time) {
			log.info("could not execute batch.Connection is closed");
			throw (new SQLException());
		}
		return array;
	}

	@Override
	public ResultSet_IF executeQuery(String sql) throws SQLException {
		long begin = System.currentTimeMillis();
		if (checkConnection == false) {
			log.info("could not execute batch.Connection is closed");
			throw (new SQLException());
		}
		parsObject.splitAll(sql);
		ArrayList<String>[] list = parsObject.getArrayList();

		if (list == null) {
			throw (new SQLException("could not return ResultSet"));
		}
		rst = new ResultSet_IF(list, parsObject.getTableName(), url ,parsObject.getDatabaseName() );
		long end = System.currentTimeMillis();
		if (time != 0 && (end - begin) > time) {
			throw (new SQLException("time exceeded"));
		}
		return rst;
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		long begin = System.currentTimeMillis();
		if (checkConnection == false) {
			log.info("could not execute.Connection is closed");
			throw (new SQLException());
		}
		boolean valid = parsObject.splitAll(sql);
		if (valid) {
			long end = System.currentTimeMillis();
			if (time != 0 && (end - begin) > time) {
				throw (new SQLException("time exceeded"));
			}
			return parsObject.getCounter();
		}
//		long end = System.currentTimeMillis();
//		if (time != 0 && (end - begin) > time) {
//			throw (new SQLException("time exceeded"));
//		}
		return 0;
	}

	@Override
	public Connection_IF getConnection() throws SQLException {
		long begin = System.currentTimeMillis();
		if (checkConnection == false) {
			log.info("could not get Connection.Connection is closed");
			throw (new SQLException());
		}
		Connection_IF con = new Connection_IF(getUrl());
		long end = System.currentTimeMillis();
		if (time != 0 && (end - begin) > time) {
			throw (new SQLException("time exceeded"));
		}
		return con;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		if (checkConnection == false) {
			log.info("could not connect query.Connection is closed");
			throw (new SQLException());
		}

		return (time/1000000000);
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		if (checkConnection == false) {
			log.info("could not set query timeout.Connection is closed");
			throw (new SQLException("could not add batch"));
		}
		time = seconds*1000000000;

	}

	// -----------------------------------------------------------------------------
	//---------------More methods for jUnit test------------------------------------
	
	public ArrayList<String> getList(){
		return list;
	}
	
	public boolean getCheckConnection(){
		return checkConnection;
	}
	public String getUrl() {
		return url;
	}

	
	
	// -----------------------------------------------------------------------------
	@Override
	public void cancel() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean execute(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getMoreResults(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCursorName(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxFieldSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMaxRows(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPoolable(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
