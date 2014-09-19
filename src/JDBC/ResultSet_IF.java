package JDBC;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.slf4j.Logger;
import DBMS.*;
import JDBC.*;
import TEST.*;

public class ResultSet_IF implements ResultSet {

	//logging object
	static Logger log	= org.slf4j.LoggerFactory.getLogger( ResultSet_IF.class);
	
	// static variable = table returned
	private ArrayList<String>[] adjList;
	// pointer to point on curent row
	private int row;
	
	//boolean for checking the question
	private boolean checkConnection=false;
	
	//String table name for metadata
	private String tableName;
	
	//String url
	private String url;
	
	//String dbName
	private String dbName; 
	
	// constructor
	public ResultSet_IF(ArrayList<String>[] AL ,String table_Name ,String url_,String dbName_) {
		adjList = AL.clone();
		row = 0;
		checkConnection=true;
		tableName=table_Name;
		url=url_;
		dbName=dbName_;
	}

	//getter for adjList
	public ArrayList<String>[] getList(){
		return adjList;
	}
	
	//getter for the current row
	public int getCurrentRow(){
		return row;
	}
	
	//getter for tablename
	public String getTableName(){
		return tableName;
	}
	
	public String getUrl(){
		return url;
	}
	
	public String getdbName(){
		return dbName;
	}
	
	// ///////////////////////////////////////////////METHODS///////////////////////////////////
	//////////////////////////////////////////////////TArek's methods//////////////////////////
	@Override
	public boolean absolute(int row) throws SQLException {
		
		if (!checkConnection){
			log.info("Error in Connection");
			throw (new SQLException("Error in Connection"));
		}
		
		if (row <1 || row >=adjList[0].size()){
			return false;
		}
		
		
		if(row > 0 && row > this.row)
			this.row = row;
		else if(row < 0 && adjList[0].size() + row > this.row)
			this.row = adjList[0].size() + row;
		
		return true;
	}
	
	@Override
	public void afterLast() throws SQLException {
		
		if (!checkConnection){
			log.info("Error in Connection");
			throw (new SQLException());
		}
		
		row = adjList[0].size();
	}

	@Override
	public void beforeFirst() throws SQLException {
		if (!checkConnection){
			log.info("Error in Connection");
			throw (new SQLException());
		}
		
		row = 0;
	}
	
	@Override
	public void close() throws SQLException {
		
		if (!checkConnection){
			log.info("Error in Connection");
			throw (new SQLException());
		}
		url="";
		dbName="";
		row=0;
		log.info("ResultSet Closed !");
		checkConnection=false;
	}

	@Override
	public int findColumn(String columnLabel) throws SQLException {
		if (!checkConnection){
			log.info("Error in Connection");
			throw (new SQLException());
		}
		int column = -1;
		for (int i = 0; i < adjList.length; i++) {
			if(adjList[i].get(0).equals(columnLabel)){
				column = i+1;
				break;
			}
		}
		if(column == -1){
			log.info("there's no column named " + columnLabel);
			throw(new SQLException());
		}
		return column;
	}

	@Override
	public boolean first() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		if(adjList[0].size() == 0)
			return false;
		row = 1;
		return true;
	}
	
	@Override
	// msh 3aref a3mlha
	///////////// msh kamla
	public Array getArray(int columnIndex) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		try{
		columnIndex--;
		String a = adjList[columnIndex].get(row);
//		Connection con;
//		Array array =con.createArrayOf("VARCHAR", a.split("-"));
		ResultSet_IF rs=new ResultSet_IF(adjList, tableName, url,dbName);
		Array_IF arr=new Array_IF(a);
		
			return (Array) arr.getArray();
		}catch(Exception e){
			log.info("Error in get array !");
			throw (new SQLException());
		}
	}
	
	@Override
	public boolean getBoolean(int columnIndex) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		columnIndex--;
		if(!get_Type(columnIndex).equalsIgnoreCase("boolean")){
			log.info("the type of this column isn't boolean !!");
			throw(new SQLException());
		}
		try{
			if (adjList[columnIndex].get(row).equals("true"))return true;
			return false;
			}catch(Exception e){
				log.info("Error in boolean array out of index !");
				throw (new SQLException());
			}
		
	}

	@Override
	public boolean getBoolean(String columnName) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		int columnIndex = findColumn(columnName);
		if(!get_Type(columnIndex).equalsIgnoreCase("boolean")){
			log.info("the type of this column isn't boolean !!");
				throw(new SQLException());
		}
		
		columnIndex--;
		try{
		if (adjList[columnIndex].get(row).equals("true"))return true;
		return false;
		}catch(Exception e){
			log.info("Error in boolean array out of index !");
			throw (new SQLException());
		}
	}
	
	//check
	@Override
	public Date getDate(int columnIndex) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		columnIndex --;
		Date d = (Date) new java.util.Date();
		try {
			long l = d.parse(adjList[columnIndex].get(row));
		} catch (Exception e) {
			log.info("the value at this index isn't of type DATE !!");
			throw(new SQLException());
		}
		return d;
	}

	@Override
	// lessa msh mota2aked menha
	public Date getDate(String columnName) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		int columnIndex = findColumn(columnName);
		Date d = (Date) new java.util.Date();
		try {
			long l = d.parse(adjList[columnIndex].get(row));
		} catch (Exception e) {
			log.info("the value at this index isn't of type DATE !!");
			throw(new SQLException());
		}
		return d;
	}
	@Override
	public double getDouble(int columnIndex) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		columnIndex --;
		if(get_Type(columnIndex).equalsIgnoreCase("double") 
			|| get_Type(columnIndex).equalsIgnoreCase("int")
			|| get_Type(columnIndex).equalsIgnoreCase("float")){
			try{
				columnIndex--;
				double d=Double.parseDouble(adjList[columnIndex].get(row));
			return d;
			}catch(Exception e){
				log.info("the column index is out of bounds ! in get double()");
				throw(new SQLException());
			}
		}
		log.info("the value at this index isn't of type DOUBLE !!");
		throw(new SQLException());
	}

	@Override
	public double getDouble(String columnName) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		int columnIndex = findColumn(columnName);
		if(get_Type(columnIndex).equalsIgnoreCase("double") 
			|| get_Type(columnIndex).equalsIgnoreCase("int")){
			columnIndex--;
			try{
			double d= Double.parseDouble(adjList[columnIndex].get(row));
			return d;
			}catch(Exception e){
				log.info("the column index is out of bounds ! in get double(String)");
				throw(new SQLException());
			}
			
		}
		log.info("the value at this index isn't of type DOUBLE !!");
		throw(new SQLException());
	}

	//check
	@Override
	public int getFetchDirection() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		return ResultSet.TYPE_SCROLL_INSENSITIVE;
	}
	
	@Override
	public float getFloat(int columnIndex) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		columnIndex --;
		if(get_Type(columnIndex).equalsIgnoreCase("float") 
				|| get_Type(columnIndex).equalsIgnoreCase("int")){
			try{
				float f=Float.parseFloat(adjList[columnIndex].get(row));
				return f;
			}catch(Exception e){
				log.info("the column index is out of bounds ! in get float()");
				throw(new SQLException());
			}
				
			}
			throw(new SQLException("the value at this index isn't of type FLOAT !!"));
	}

	@Override
	public float getFloat(String columnName) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		int columnIndex = findColumn(columnName);
		if(get_Type(columnIndex).equalsIgnoreCase("float") 
				|| get_Type(columnIndex).equalsIgnoreCase("int")){
			columnIndex--;
			try{
				float d= Float.parseFloat(adjList[columnIndex].get(row));
				return d;
			}catch(Exception e){
				log.info("the column index is out of bounds ! in get float(String)");
				throw(new SQLException());
			}
			}
			throw(new SQLException("the value at this index isn't of type FLOAT !!"));
	}
	
	public String get_Type(int columnIndex) throws SQLException{
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		columnIndex --;
		Parser p = new Parser();
		p.setPathParser( url );
		String type = p.getType( tableName , columnIndex , adjList );
		
		return type;
	}
	
	
	
	
	/////////////////////////////////////////My Methods//////////////////////////
	@Override
	public int getInt(int columnIndex) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}		
		columnIndex--;
		try {
			if (adjList[columnIndex].get(row).equals(""))return 0;
			
			int result = Integer.parseInt(adjList[columnIndex].get(row));
			
			return result;
		} catch (Exception e) {
			log.info("getInt parsing Error !");
			throw (new SQLException());
		}

	}

	@Override
	public int getInt(String columnLabel) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		try{
			int column=-1;
			for (int i=0;i<adjList.length;i++){
				if (adjList[i].get(0).equals(columnLabel)){
					column=i;
				}
			}
			if (adjList[column].get(row).equals(""))return 0;
			
			int result= Integer.parseInt(adjList[column].get(row));
			return result;
		}catch (Exception e){
			log.info("getInt(String) parsing Error !");
			throw (new SQLException());
		}
		
	}

	@Override
	public long getLong(int columnIndex) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		columnIndex--;
		try {
			if (adjList[columnIndex].get(row).equals(""))return 0;
			
			long result = Long.parseLong(adjList[columnIndex].get(row));
			return result;
		} catch (Exception e) {
			log.info("getLong parsing Error !");
			throw (new SQLException());
		}

		
		
	}

	@Override
	public long getLong(String columnLabel) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		try{
			int column=-1;
			for (int i=0;i<adjList.length;i++){
				if (adjList[i].get(0).equals(columnLabel)){
					column=i;
				}
			}
			if (adjList[column].get(row).equals(""))return 0;
			long result= Long.parseLong(adjList[column].get(row));
			return result;
		}catch (Exception e){
			log.info("getInt(String) parsing Error !");
			throw (new SQLException());
			
		}	
	}
	
	@Override
	public ResultSetMetaData_IF getMetaData() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		ResultSet_IF ob=new ResultSet_IF(adjList, tableName, url,dbName);
		ResultSetMetaData_IF obj=new ResultSetMetaData_IF(ob);
		
		return obj;
	}

	@Override
	public Object getObject(int columnIndex) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		columnIndex--;
		
		return adjList[columnIndex].get(row);
		
	}

	//check again
	@Override
	public Statement getStatement() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		Statement_IF ob=new Statement_IF(url);
		
		return ob;
	}

	@Override
	public String getString(int columnIndex) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		columnIndex--;
		try{
			if (adjList[columnIndex].get(row).equals(""))return null;
			
			String result=adjList[columnIndex].get(row);
			return result;
		}catch(Exception e){
			log.info("getString() parsing Error !");
			throw (new SQLException());
		}
	}

	@Override
	public String getString(String columnLabel) throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		try{
			int column=-1;
			for (int i=0;i<adjList.length;i++){
				if (adjList[i].get(0).equals(columnLabel)){
					column=i;
				}
			}
			if (adjList[column].get(row).equals(""))return null;
			
			String result= adjList[column].get(row);
			return result;
		}catch (Exception e){
			log.info("getString(String) parsing Error !");
			throw (new SQLException());
			
		}	
	}

	@Override
	public boolean isAfterLast() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		if (row==adjList.length){
			return true;
		}
		return false;
		
	}

	@Override
	public boolean isBeforeFirst() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		if (row==0)
			return true;
		return false;

	}

	//// check
	@Override
	public boolean isClosed() throws SQLException {
		if (!checkConnection){
			return true;
		}
		return false;
	}

	@Override
	public boolean isFirst() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		if (row==1)return true;
		
		return false;
	}

	@Override
	public boolean isLast() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		if (row==adjList[0].size()-1)return true;
		
		return false;
	}

	@Override
	public boolean last() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		if (adjList[0].size() <= 1)return false;
		
		row=adjList[0].size()-1;
		
		return true;
	}

	@Override
	public boolean next() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
	
		if ( row >= adjList[0].size() - 1 )
			return false;
		
		row = row + 1;
		
		return true;
	}

	@Override
	public boolean previous() throws SQLException {
		if (!checkConnection){
			log.info("Error in connection !");
			throw (new SQLException());
		}
		
		if ( row == 0 )return false;
		
		row--;
		
		
		return true;
	}

	// ///////////////////////////END//////////////////////////////////
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
	public void cancelRowUpdates() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}




	@Override
	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	

	
	

	@Override
	public Array getArray(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel, int scale)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public byte getByte(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getByte(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCursorName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String columnLabel, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob getNClob(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNString(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getObject(String columnLabel, Class<T> type)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ref getRef(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRow() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RowId getRowId(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RowId getRowId(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Time getTime(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String columnLabel, Calendar cal) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar cal)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public URL getURL(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getURL(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getUnicodeStream(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean relative(int rows) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(int columnIndex, Array x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateArray(String columnLabel, Array x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String columnLabel, Blob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBoolean(String columnLabel, boolean x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(int columnIndex, byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateByte(String columnLabel, byte x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateBytes(String columnLabel, byte[] x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, int length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			int length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int columnIndex, Clob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String columnLabel, Clob x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(int columnIndex, Date x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDate(String columnLabel, Date x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(int columnIndex, double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDouble(String columnLabel, double x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(int columnIndex, float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFloat(String columnLabel, float x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(int columnIndex, int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateInt(String columnLabel, int x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(int columnIndex, long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLong(String columnLabel, long x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String columnLabel, NClob nClob)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(int columnIndex, String nString)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNString(String columnLabel, String nString)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(int columnIndex) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNull(String columnLabel) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int columnIndex, Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String columnLabel, Object x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(int columnIndex, Object x, int scaleOrLength)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObject(String columnLabel, Object x, int scaleOrLength)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(int columnIndex, Ref x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRef(String columnLabel, Ref x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(int columnIndex, short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateShort(String columnLabel, short x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(int columnIndex, String x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateString(String columnLabel, String x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(int columnIndex, Time x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTime(String columnLabel, Time x) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp x)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
