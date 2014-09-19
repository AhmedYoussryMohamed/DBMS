package DBMS;
import java.util.ArrayList;
import java.util.HashMap;
import DBMS.*;
import JDBC.*;
import TEST.*;

public interface DBMS {
	
	public boolean createDatabase(String dbName);
	public boolean createTable( String tableName ,ArrayList<String> types , ArrayList<String> values ,ArrayList<HashMap<String , Boolean> > list );
	public boolean insertIntoTable( String tableName , String[] corresValue ,String[] value );
	public ArrayList<String>[] selectFromTable( String tableName ,String[] corresValue ,String[][] condition ,String[] operators);
	public boolean deleteFromTable( String tableName ,String[][] condition ,String[] operators );
	public boolean updateTable( String tableName , String[] corresValue ,String[] values ,String[][] condition ,String[] operators);
	public boolean useDatabase(String dbName);
	public void setPathDBMS(String name);
	public void releaseDatabase();
	public int getCounter();
	public String getType( String tableName ,int colIndex ,ArrayList<String>[] adjList );
	public String getTableName();
	public String getStatus( String tableName ,String colName ,String valueWanted );
	public String getPath();
	public String getDatabaseName();
	public void setDb(String name);
	
}//end interface.
