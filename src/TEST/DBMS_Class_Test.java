package TEST;
import static org.junit.Assert.*;
import  DBMS.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class DBMS_Class_Test {
	
	@Test
	public void test() {
		DBMS_Class obj = new DBMS_Class();
		obj.setPathDBMS( System.getenv("ahmed") +"test\\");
		
		boolean t1= obj.createDatabase("omar");
		assertEquals(t1,true);
		//-------------------------------------------------
		String tableName = "wages";
		ArrayList<String>types = new ArrayList<String>();
		types.add("String");
		types.add("String");
		types.add("int");
		ArrayList<String>values = new ArrayList<String>();
		values.add("firstName");
		values.add("lastName");
		values.add("age");
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("autoincrement", false);
		map.put("nullable", true);
		map.put("readable", true);
		map.put("searchable", true);
		map.put("writable", true);
		ArrayList<HashMap<String ,Boolean>> list=new ArrayList<HashMap<String,Boolean>>();
		list.add(map);
		list.add(map);
		list.add(map);
		boolean t2=obj.createTable(tableName , values,types,list);
		assertEquals(t2,true);
		//--------------------------------------------------
		String[]corresValue=new String[3];
		corresValue[0]="firstName";
		corresValue[1]="lastName";
		corresValue[2]="age";
		String[]value=new String[3];
		value[0]="yahia";
		value[1]="hesham";
		value[2]="80";
		boolean t3= obj.insertIntoTable(tableName, corresValue, value);
//		boolean t8= obj.insertIntoTable(tableName, corresValue, value);
//		boolean t9= obj.insertIntoTable(tableName, corresValue, value);
//		boolean t10= obj.insertIntoTable(tableName, corresValue, value);
		assertEquals(t3, true);
		//-----------------------------------------------------
		boolean t4 = obj.checkTableExist("wages");	
		assertEquals(t4, true);
		//-----------------------------------------------------
		value[0]="omar";
		value[1]="mahmoud";
		value[2]="10";
		String condition[][]= new String[2][3];
		condition[0][0]="firstName";
		condition[0][1]="=";
		condition[0][2]="yahia";
		condition[1][0]="age";
		condition[1][1]=">";
		condition[1][2]="50";
		String  operators[]= new String[1];
		operators[0] ="AND";
		//-----------------------------------------------------
		boolean t5=false;
		ArrayList<String>[] list1 = obj.selectFromTable(tableName, corresValue, condition, operators);
		if(list1!=null){
			t5=true;
		}
		assertEquals(t5, true);
		//-----------------------------------------------------
		boolean t6= obj.updateTable(tableName, corresValue, value, condition, operators);
		assertEquals(t6, true);
		//-----------------------------------------------------
		boolean t7=obj.deleteFromTable(tableName, condition, operators);
		assertEquals(t7, true);
	}

}
