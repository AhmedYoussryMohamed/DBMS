package DBMS;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import DBMS.*;
import JDBC.*;
import TEST.*;

import org.jdom2.internal.ArrayCopy;

 public class Parser {

	DBMS obj = new DBMS_Class();

	ArrayList<String>[] list = null;

	public boolean splitAll(String query) {
		boolean check = false;
		String w1 = "", w2 = "";
		String[] type = split(query);
		try{
		w1 = type[0];
		w2 = type[1];
		if (w1.equalsIgnoreCase("SELECT")) {
			list = null;
			check = checkSelect(type);
		} else if (w1.equalsIgnoreCase("INSERT")) {
			list = null;
			check = checkInsert(query);
		} else if (w1.equalsIgnoreCase("UPDATE")) {
			list = null;
			check = checkUpdate(query);
		} else if (w1.equalsIgnoreCase("DELETE")) {
			list = null;
			check = checkDelete(query);
		} else if ((w1.equalsIgnoreCase("CREATE"))&& w2.equalsIgnoreCase("TABLE")) {
			list = null;
			check = checkCreateTB(query);
		} else if ((w1.equalsIgnoreCase("CREATE"))&& w2.equalsIgnoreCase("DATABASE")) {
			list = null;
			check = checkCreateDB(query);
		} else if (w1.equalsIgnoreCase("USE")) {
			list = null;
			check = checkUse(query);
		}
		if(check ==false){System.out.println("Error");}
		return check;
		}catch (Exception e){
			System.out.println("wrong query");
			return false;
		}
	}

	public boolean checkCreateDB(String query) {
		String name;
		try {
			String[] split = split(query);
			// checking if the length of the sentence;
			if (split.length > 3) {
				return false;
			}
			name = split[2];
			// checking if the name is valid
			if (!validName(name)) {
				return false;
				}
		} catch (Exception e) {
			return false;
			}
		if (!obj.createDatabase(name))
			return false;
		return true;
	}// end method.

	public boolean checkDelete(String query) {
		// checking the basic words.
		String tableName = "",arrayCondition[][] = null ,arrayOperators[] = null ,split[]=split(query);
		try {
			if (split.length < 4) {return false;}
			if (split[1].equals("*")) {
				tableName = split[3];
				if (!split[2].equalsIgnoreCase("FROM")||(!validName(tableName))) {return false;}
				arrayCondition = new String[1][3];
				// MODIFIED: 
				arrayCondition[0][0] = (arrayCondition[0][1] = (arrayCondition[0][2] = "*"));
			} else {
				if (!split[1].equalsIgnoreCase("FROM")|| !split[3].equalsIgnoreCase("WHERE")) {return false;}
				tableName = pureString(split[2]);
				if (!validName(tableName)) {return false;}
				arrayCondition =getConditionArray(split, 4);
				arrayOperators =getOperators(split, 4);
				for(int i = 0 ; i<arrayCondition.length ; i++){
						if (!validName(arrayCondition[i][0])||!validName(arrayCondition[i][2])){return false;}
				}
			}
		} catch (Exception e) { return false;}
		if (!obj.deleteFromTable(tableName, arrayCondition,arrayOperators)){ return false;}
		return true;
	}// end method.

	public boolean checkUse(String query) {
		String name;
		try {
			StringTokenizer st = new StringTokenizer(query);
			String[] split = new String[st.countTokens()];
			int c = 0;
			while (st.hasMoreTokens()) {
				split[c] = st.nextToken();
				c++;
			}
			// checking if the length of the sentence;
			if (split.length > 3 || split.length < 2) {
				System.out.println("Incorrect enrty, Enter query:");
				return false;
			}
			name = split[2].replace(";", "");
			// checking if the name is valid
			if (!validName(name))return false;
		} catch (Exception e) {	return false;}
		if (!obj.useDatabase(name))return false;
		return true;
	}// end method.

	public boolean checkSelect(String[] query) {
		String tableName = "" , conditionArray[][] , columnsArray[], operators[];
		try {
			// btgeeb el index bta3 kelmet "where" w kelmet " from" fl query
			int whereIndex = getWhereIndex(query);
			int fromIndex = getFromIndex(query);
			if (fromIndex == -1)return false;
			
			if (query[1].equalsIgnoreCase(pureString("*"))) {
				columnsArray = new String[1];
				columnsArray[0] = "*";
			} else{
				// lw select column name
				columnsArray = getColumnsArray(query, fromIndex);
			}
			if(whereIndex == -1){
				tableName = query[fromIndex + 1];
				conditionArray = null;
				operators = null;
			}
			else{
				tableName = query[(fromIndex + whereIndex) / 2];
				try {
					conditionArray = getConditionArray(query, whereIndex + 1);
					operators = getOperators(query, whereIndex + 1);
					if (conditionArray.equals(null))return false;
				} catch (Exception e) {return false;}
			}

		} catch (Exception e) {	return false;}
		boolean h = printSelect(tableName, columnsArray, conditionArray, operators);
		if (!h) return false;
		return true;
	}

	public boolean printSelect(String tableName, String[] columnsArray,String[][] conditionArray, String[] operators) {
		
		ArrayList<String>[] adjList = obj.selectFromTable(tableName,columnsArray, conditionArray, operators);
		list = adjList.clone();
		if (adjList == null){
			return false;
		}
//		for (int i = 0; i < adjList[0].size(); i++) {
//			for (int j = 0; j < adjList.length; j++) {
//				System.out.print(adjList[j].get(i) +"       ");
//			}// end for j.
//			System.out.println();
//		}// end for i.
		return true;
	}

	public boolean checkInsert(String query) {
		String tableName = "", columns[] = null, values[] = null;
		try {
			if (!query.split(" ")[1].equalsIgnoreCase("INTO"))return false;
			int i = 11;
			String temp = "";
			while (i < query.length()) {
				if (query.charAt(i) != '(') {
					temp += query.charAt(i);
					i++;
				} else {
					tableName = pureString(temp);
					i++;
					break;
				}
			}
			temp = "";
			while (i < query.length()) {
				if (query.charAt(i) != ')') {
					temp += query.charAt(i);
					i++;
				} else {
					columns = temp.split(",");
					i++;
					break;
				}
			}
			temp = "";
			while (i < query.length()) {
				if (query.charAt(i) != '(') {
					temp += query.charAt(i);
					i++;
				} else {
					if (!pureString(temp).equalsIgnoreCase("VALUES")) return false;
					i++;
					break;
				}
			}
			temp = "";
			while (i < query.length()) {
				if (query.charAt(i) != ')') {
					temp += query.charAt(i);
					i++;
				} else {
					values = temp.split(",");
					i++;
					break;
				}
			}
			if (values.length != columns.length)return false;
			for (int j = 0; j < values.length; j++) {
				values[j] = pureString(values[j]);
				columns[j] = pureString(columns[j]);
			}
			for (int j = 0; j < columns.length; j++) {
				for (int j2 = j + 1; j2 < columns.length; j2++) {
					if (columns[j].equalsIgnoreCase(columns[j2]))return false;
				}
			}
		} catch (Exception e) {return false;}
		if (!obj.insertIntoTable(tableName, columns, values))
			return false;

		System.out.println("1 row inserted successfully");
		return true;

	}

	public boolean checkUpdate(String query) {
		String columns[], values[], arrayCondition[][],arrayOperators[],tableName = "", setLine = "";
		int whereIndex = 0;
		try {
			String[] split = split(query);
			// checking the basic words
			tableName = split[1];
			if (!validName(tableName) || (!split[2].equalsIgnoreCase("SET"))|| (!validName(split[1]))){return false;}
			// getting condition_1.
			ArrayList<String[]> list = new ArrayList<String[]>();
			for (int i = 3; i < split.length; i++) {
				if (split[i].equalsIgnoreCase("WHERE")) {
					whereIndex = i;
					break;
				}
				setLine += split[i];
			}// end for(i).
			String[] tempSet = setLine.split(",");
			for (int i = 0; i < tempSet.length; i++) {
				if (!isValidCondition(tempSet[i]))return false;
				String[] arraySet = divideCondition(tempSet[i]);
				if (!arraySet[1].equals("=")) return false;
				list.add(arraySet);
				setLine = "";
			}// for(i).
			columns = new String[list.size()];
			values = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				columns[i] = list.get(i)[0];
				values[i] = list.get(i)[2];
			}// end for(i).
			// getting condition.
			arrayCondition =getConditionArray(split, whereIndex+1);
			arrayOperators =getOperators(split, whereIndex+1);
			for (int i = 0; i < values.length; i++) {
				if (!validName(values[i])) return false;
			}
		} catch (Exception e) {return false;}
		if (!obj.updateTable(tableName, columns, values, arrayCondition,arrayOperators)){return false;}
		System.out.println("Updated successfully");
		return true;
	}// end method.
		
	public boolean checkCreateTB(String query) {
		String attributes[], types[], tableName = "", split[] = split(query);
		ArrayList<HashMap<String, Boolean>> list = new ArrayList<HashMap<String, Boolean>>();
		try {
			if (!split[1].equalsIgnoreCase("TABLE"))return false;
			int i = query.indexOf('('), j = query.indexOf(')');
			tableName = getTableName(query);
			String content = query.substring(i + 1, j);
			attributes = content.split(",");
			types = new String[attributes.length];
			for (int k = 0; k < attributes.length; k++) {
				HashMap<String, Boolean> map = new HashMap<String, Boolean>();
				String[] col = split(attributes[k]);
				attributes[k] = pureString(col[0]);
				types[k] = pureString(col[1]);
				if (!checkCol(col))return false;
				map = extraFeaturesHandle(col);
				list.add(map);
			}
			if (!checkType(types))return false;
			types = typeHandle(types);
			if( types == null ){
				System.out.println("error in types!!");
			}
		} catch (Exception e) {return false;}
		ArrayList<String> a = new ArrayList<String>();
		ArrayList<String> b = new ArrayList<String>();
		for (int m = 0; m < types.length; m++) {
			a.add(types[m]);
			b.add(attributes[m]);
		}// end for m.
		if (!obj.createTable(tableName, b, a, list))
			return false;
		System.out.println("table created successfully");
		return true;
	}

	public boolean isValidCondition(String condition) {
		int numOfSigns = 0;
		int signIndex = 0;
		for (int i = 0; i < condition.length(); i++) {
			if (condition.charAt(i) == '<' || condition.charAt(i) == '>'
					|| condition.charAt(i) == '=') {
				numOfSigns++;
				signIndex = i;
			}
		}
		if (numOfSigns != 1) {
			if(numOfSigns>2){
				return false;
			}
			if(condition.charAt(signIndex)!='>' || condition.charAt(signIndex-1)!='<')
				return false;
		}
		if(signIndex!=condition.length()){
		}
		if (signIndex == 0 || signIndex == condition.length()) {
			return false;
		}

		return true;
	}

	public String[] divideCondition(String conditionLine) {
		String sign = "";
		for (int i = 0; i < conditionLine.length(); i++) {
			int signIndex=0;
			if (conditionLine.charAt(i) == '<'|| conditionLine.charAt(i) == '>'
					|| conditionLine.charAt(i) == '=') {
				signIndex=i;
				sign += conditionLine.charAt(i);
			}
			if(conditionLine.charAt(i)=='<'){
				if(conditionLine.charAt(i+1)=='>'){
					sign+=conditionLine.charAt(signIndex+1);
				}
			}
		}
		
		conditionLine = pureString(conditionLine);

		String arrayCondition[] = new String[3];
		String s[] = conditionLine.split(sign);
		arrayCondition[0] = s[0];
		arrayCondition[1] = sign;
		arrayCondition[2] = s[1];

		return arrayCondition;
	}

	public boolean validName(String name) {
		String valid = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-";
		for (int i = 0; i < name.length(); i++) {
			boolean check = false;
			for (int j = 0; j < valid.length(); j++) {
				if (name.charAt(i) == valid.charAt(j)) {
					check = true;
				}// end if check.
			}// end if (j).
			if (!check) {
				return false;
			}
		}// end for(i).
		return true;
	}// end method.

	public String pureString(String s) {
		String temp = "";
		temp = s.replace(";", "");
		temp = temp.replace(",", "");
		temp = temp.replace(" ", "");
		temp = temp.replace(".", "");
		temp = temp.replace("`", "");
		temp = temp.replace("\'", "");
		temp = temp.replace("\"", "");
		temp = temp.replace("\n", "");

		return temp;
	}

	public int getWhereIndex(String[] query) {
		for (int i = 1; i < query.length; i++) {
			if (query[i].equalsIgnoreCase("WHERE")) {
				return i;
			}
		}
		return -1;
	}

	public int getFromIndex(String[] query) {
		for (int i = 1; i < query.length; i++) {
			if (query[i].equalsIgnoreCase("FROM")) {
				return i;
			}
		}
		return -1;
	}

	public String getTableName(String query) {
		String split[] = split(query);
		String tableName = "";
		String temp = pureString(split[2]);
		if (temp.contains("(")) {
			int k = temp.indexOf('(');
			tableName = temp.substring(0, k);
		} else {
			tableName = temp;
		}
		return tableName;
	}

	public boolean checkCol(String col[]) {
		for (int i = 2; i < col.length; i++) {
			if (	!(col[i].equalsIgnoreCase("autoincrement"))
					&& !(col[i].equalsIgnoreCase("unreadable"))
					&& !(col[i].equalsIgnoreCase("unsearchable"))
					&& !(col[i].equalsIgnoreCase("unwritable"))
					&& !(col[i].equalsIgnoreCase("not"))
					&& !(col[i].equalsIgnoreCase("readable"))
					&& !(col[i].equalsIgnoreCase("searchable"))
					&& !(col[i].equalsIgnoreCase("writable"))
					&& !(col[i].equalsIgnoreCase("null"))
					) {
				return false;
			}
			if (col[i].equalsIgnoreCase("not")) {
				try {
					if (!col[i + 1].equalsIgnoreCase("NULL")) {
						return false;
					} else {
						i++;
					}
				} catch (Exception e) {
					return false;
				}
			}
		}// end for(i).
		return true;
	}

	public boolean checkType(String types[]) {
		for (int k = 0; k < types.length; k++) {
			if (!types[k].equalsIgnoreCase("INT")
					&& !types[k].equalsIgnoreCase("BIGINT")
					&& !types[k].equalsIgnoreCase("VARCHAR")
					&& !types[k].equalsIgnoreCase("FLOAT")
					&& !types[k].equalsIgnoreCase("DOUBLE")) {
				return false;
			}
		}
		return true;
	}

	public String[] typeHandle(String types[]) {
		for (int k = 0; k < types.length; k++) {
			if (!types[k].equalsIgnoreCase("INT")
					&& !types[k].equalsIgnoreCase("BIGINT")
					&& !types[k].equalsIgnoreCase("VARCHAR")
					&& !types[k].equalsIgnoreCase("Float")
					&& !types[k].equalsIgnoreCase("double")
					)
				return null;
			else if (types[k].equalsIgnoreCase("BIGINT"))
				types[k] = "long";
			else if (types[k].equalsIgnoreCase("VARCHAR"))
				types[k] = "String";
			else if (types[k].equalsIgnoreCase("INT"))
				types[k] = "int";
			else if (types[k].equalsIgnoreCase("DOUBLE"))
				types[k] = "double";
			else if (types[k].equalsIgnoreCase("float"))
				types[k] = "float";
			
		}
		return types;
	}

	public HashMap<String, Boolean> extraFeaturesHandle(String col[]) {
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("autoincrement", false);
		map.put("nullable", true);
		map.put("readable", true);
		map.put("searchable", true);
		map.put("writable", true);
		for (int i = 2; i < col.length; i++) {
			if (col[i].equalsIgnoreCase("autoincrement")){
				map.put("autoincrement", true);
			}else if (col[i].equalsIgnoreCase("unreadable")){
				map.put("readable", false);
			}else if (col[i].equalsIgnoreCase("unsearchable")){
				map.put("searchable", false);
			}else if (col[i].equalsIgnoreCase("unwritable")){
				map.put("writable", false);
			}
			else if (col[i].equalsIgnoreCase("not")) {
				i++;
				//needs checking.
				map.put("nullable", false);
			} 
			else if (col[i].equalsIgnoreCase("readable")
					|| col[i].equalsIgnoreCase("writable")
					|| col[i].equalsIgnoreCase("searchable")) {
				
			} else{
				return null;
			}
		}
		return map;
	}

	private String[] getColumnsArray(String[] query, int fromIndex) {
		String columns = "";
		for (int i = 1; i < fromIndex; i++) {
			columns += query[i];
		}
//		System.out.println(columns+"SDF");
//		StringTokenizer st = new StringTokenizer(columns);
//		String[] columnsArray = new String[st.countTokens()];
//		for (int i = 0; st.hasMoreTokens(); i++) {
//			columnsArray[i] = pureString(st.nextToken());
//		}
		String[] columnsArray=columns.split(",");
		return columnsArray;
	}
	
	public String[] getOperators(String[] query, int index)
	{
		String[] operationsTemp = new String[query.length - index];
		int count = 0;
		for (int i = index; i < query.length; i++) {
			if(query[i].equalsIgnoreCase("AND") || query[i].equalsIgnoreCase("OR") 
				|| query[i].equalsIgnoreCase("NOT")){
				operationsTemp[count] = query[i];
				count++;
			}
		}
		String[] operators = new String[count];
		System.arraycopy(operationsTemp, 0, operators, 0, count );
		return operators;
	}

	public String[][] getConditionArray(String[] query, int index) {
		String[] conditionArrayTemp = new String[query.length - index];
		conditionArrayTemp[0] = "";
		int count = 0;
		for (int i = index; i < query.length; i++) {
			if(query[i].equalsIgnoreCase("AND") || query[i].equalsIgnoreCase("OR") || query[i].equalsIgnoreCase("NOT")){
				count++;
				conditionArrayTemp[count] = "";
			}
			else{
				conditionArrayTemp[count] +=  query[i];
			}
		}
		count++;
		String[][] conditionArray = new String[count][3];
		for (int i = 0; i < count; i++) {
			if(!isValidCondition(conditionArrayTemp[i])){return null;}
			conditionArray[i]=divideCondition(conditionArrayTemp[i]);
		}
		return conditionArray;
	}

	public String[] conditionHandle(String conditionLine) {
		if (!isValidCondition(conditionLine)) {
			return null;
		}
		String[] arrayCondition = divideCondition(conditionLine);

		if (!validName(arrayCondition[0]) || !validName(arrayCondition[2])) {
			return null;
		}
		return arrayCondition;
	}

	public String[] split(String query) {
		int c = 0;
		StringTokenizer st = new StringTokenizer(query);
		String[] split = new String[st.countTokens()];
		while (st.hasMoreTokens()) {
			split[c] = st.nextToken();
			c++;
		}
		return split;
	}

	public void setPathParser(String name) {
		obj.setPathDBMS(name);
	}

	public void releaseDatabase() {
		obj.releaseDatabase();
	}

	public int getCounter() {
		return obj.getCounter();
	}

	public String getType(String tableName, int colIndex,
			ArrayList<String>[] adjList) {
		return obj.getType(tableName, colIndex, adjList);
	}

	public String getTableName() {
		return obj.getTableName();
	}

	public ArrayList<String>[] getArrayList() {
		return list;
	}

	public String getStatus(String tableName, String colName, String valueWanted) {
		return obj.getStatus( tableName, colName, valueWanted );
	}// end method.
	
	public String getPath(){
		return obj.getPath();
	}//end method
	
	public String getDatabaseName(){
		return obj.getDatabaseName();
	}
	
	public void useDatabase( String dbName ){
		obj.useDatabase( dbName );
	}
	
	public void setDb(String name){
		obj.setDb(name);
	}
	
}// end class
