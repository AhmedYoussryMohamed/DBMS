package DBMS;
import DBMS.*;
import JDBC.*;
import TEST.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class DBMS_Class implements DBMS {
	private final Exception Exception = null;
	private  String path = "";
	private String url;
	private String tableNameSelect = "";
	private int counter = -1;
	//"C:\\Users\\Fush\\Desktop\\Server\\"
	private  String databaseName = "";
	
	public boolean useDatabase(String dbName){
		String dir = path + dbName + ".xml";
		counter = -1;
//		dir = "C:\\Users\\Fush\\Desktop\\Server\\db.xml";
		try {
			FileReader fr = new FileReader(dir);
			databaseName = dbName;
		} catch (FileNotFoundException e) {
			System.out.println("Database Not Found.");
			databaseName = "";
			return false;
		}
		counter = 0;
		System.out.println("Database " + databaseName + " used.");
		return true;
	}//end method.
	
	@Override
	public boolean createDatabase(String dbName) {
		counter = -1;
		try{
			String dir = path + dbName+ ".xml";
			File f = new File( dir );
			if( f.exists() ){
				System.out.println("Database already exists.");
				return false;
			}
			databaseName = dbName;
			Element tables = new Element("tables");
			Document doc = new Document();
			doc.setRootElement(tables);
		
			XMLOutputter xmlOut=new XMLOutputter();
			 
			xmlOut.setFormat( Format.getPrettyFormat() );
			xmlOut.output(doc, new FileWriter( dir ));
			counter = 0;
			System.out.println("Database " + databaseName +" Created.");
			return true;
		}catch(IOException e){
			System.out.println("Error in file in create database.");
			return false;
		}
		
	}//end method createDB.

	@Override
	public boolean createTable( String tableName, ArrayList<String> values , ArrayList<String> types ,ArrayList<HashMap<String , Boolean>> list ) {
		// TODO Auto-generated method stub
		counter = -1;
		
		if( updateDatabaseFile( tableName ,values,types , list) ){
			 try {
				 	String directory = path + tableName + "_" + databaseName +".xml";
					
					Element table = new Element( tableName );
					Document doc = new Document();
					doc.setRootElement(table);
					
					XMLOutputter xmlOutput = new XMLOutputter();
					
					// display nice nice
					xmlOutput.setFormat(Format.getPrettyFormat());
					xmlOutput.output(doc, new FileWriter( directory ));
					
					counter = 0;
					return true;
				  } catch (IOException io) {
					  System.out.println("File ERROR in creating table.");
					return false;
				  }

		}//end if .
		else{
//			System.out.println("table exists!!");
			return false;
		}
				 	
		
	}//end method createTable.

	@Override
	public boolean insertIntoTable( String tableName , String[] corresValue ,String[] value ) {
		// TODO Auto-generated method stub
		// corresValue : firstName ,age ,.....
		// value : ahmed , 20 ,.....
		
		if( checkTableExist(tableName) ){
			try {
				HashSet<String> set = new HashSet<String>();
				for( int i = 0 ;i < corresValue.length ;i++){
					if( !check( tableName ,corresValue[i] ) ){  System.out.println("Invalid Col : " + corresValue[i] +".");	 return false;}
					set.add( corresValue[i] );
					String t = getType( tableName ,corresValue[i] );
					if( !checkStatus(tableName, corresValue[i], "nullable" ) && value[i].equalsIgnoreCase("null") ){
						System.out.println("The attribute " + corresValue[i] + " is not nullable,You cannot insert.");
						return false;
					}
					if(  !checkStatus(tableName, corresValue[i], "writable" ) ){
						System.out.println("The attribute " + corresValue[i] + " is not Writtable,You cannot insert.");
						return false;
					}
					if( checkStatus(tableName, corresValue[i], "autoincrement" ) ){
						System.out.println("The attribute " + corresValue[i] + " is autoincremented,You cannot insert.");
						return false;
					}
					
					if( t == null ){
						System.out.println("The attribute " + corresValue[i] + " doesnot exist.");
						return false;
						//throw(Exception);
					}else{
						
						if( t.equalsIgnoreCase("int") ){
								int x = Integer.parseInt( value[i] );
						}else if( t.equalsIgnoreCase( "double" ) ){
								double x = Double.parseDouble( value[i] );
						}//end else if.
						else if( t.equalsIgnoreCase("float") ){
							float x = Float.parseFloat( value[i] );
						}
						else if( t.equalsIgnoreCase("long") ){
							long x = Long.parseLong( value[i] );
						}
					}//end else.
				
				}//end for i.
				
				// 2ND FILE:--------------------------
				 String dir = path + tableName +"_" + databaseName + ".xml";
					
				 SAXBuilder builder = new SAXBuilder();
				File xmlFile = new File( dir );
				
				Document doc = (Document) builder.build(xmlFile);
				Element rootNode = doc.getRootElement();
				
					Element row = new Element("row");
					for( int i = 0 ;i < corresValue.length ;i++){
						row.addContent(new Element( corresValue[i] ).setText( value[i] ));
					}//end for i.
					List list = getList(tableName);
					for( int i = 0 ;i < list.size() ;i++){
						Element node = (Element) list.get(i);
						if( !set.contains( node.getText() ) ){
							if( !checkStatus(tableName, node.getText(), "autoincrement" ) ){
								row.addContent(new Element( node.getText() +"" ).setText( "" ));
							}else{
								int x = getCurrentIndex(tableName) + 1;
								row.addContent(new Element(node.getText()).setText( x +"" ) );
								setCurrentIndex( tableName , x );
							}
						}//end if contains.
					}//end for i.
					
					rootNode.addContent(row);
					
					XMLOutputter xmlOutput = new XMLOutputter();
					 
					// display nice nice
					xmlOutput.setFormat(Format.getPrettyFormat());
					xmlOutput.output(doc, new FileWriter(dir) );
					return true;
			  } catch (IOException io) {
				  System.out.println("File Error in insert.");
				return false;
			  } catch (JDOMException e) {
				System.out.println("JDOM Exception in insert.");
				return false;
			  }catch(Exception e){
				  System.out.println("You have to stick to the type in insert.");
				  return false;
			  }
		}//end if
		else{
			System.out.println("Table " +  tableName + " doesnot exist ");
			return false;
		}
		
	}//end method insertIntoTable.

	@Override
	public ArrayList<String>[] selectFromTable( String tableName ,String[] corresValue ,String[][] condition ,String[] operators ) {
		// TODO Auto-generated method stub
		counter = -1;
		if( checkTableExist(tableName) ){
			
			try {
				
				String dir = path + tableName +"_" + databaseName + ".xml";
				SAXBuilder builder = new SAXBuilder();
				File xmlFile = new File( dir );
				
				Document doc = (Document) builder.build(xmlFile);
				Element rootNode = doc.getRootElement();
				// lawo mafesh where condition = null , operators = null;
				if( corresValue[0].equals( "*" ) ){
					List test = getList(tableName);
					corresValue = new String[ test.size() ];
					
					for( int i = 0 ;i < test.size() ;i++){
						Element node = (Element) test.get(i);
						corresValue[i] = node.getText();
					}//end for i
					
				}//end if *.
				if( operators == null ){
					operators = new String[0];
				}
				for( int i = 0 ;i < corresValue.length ;i++ ){
					if( !check( tableName ,corresValue[i] ) ){  System.out.println("Invalid Col : " + corresValue[i] +".");	 return null;}
				}//end for i.
				for( int i = 0 ;i <= operators.length && condition != null ;i++ ){
					if( !check( tableName ,condition[i][0] ) ){  System.out.println("Invalid Col : " + condition[i][0] +".");	 return null;}
					String t = getType( tableName ,condition[i][0] );
					if( t == null ){ System.out.println(condition[i][0] + " is not in the database." );  return null;}
					if( !checkStatus(tableName, condition[i][0] ,  "searchable" ) ){
						System.out.println("The attribute " + corresValue[i] + "is not searchable.");
						return null;
					}
					
					if( t.equals("String") && ( !condition[i][ 1 ].equals("=") && !condition[i][1].equals("<>") ) ){
						System.out.println("Not a valid Condition.");
						return null;
					}else if( t.equals("int") ){
						int x = Integer.parseInt( condition[i][ 2 ] );
					}else if (t.equals("double")){
						double x = Double.parseDouble( condition[i][ 2 ] );
					}//end else if. 
					else if( t.equals("float") ){
						float x = Float.parseFloat( condition[i][ 2 ] );
					}else if( t.equals("long") ){
						long x = Long.parseLong( condition[i][ 2 ] );
					}
				}//end for i.
				
				ArrayList<String>[] wanted;
				wanted = new ArrayList[ corresValue.length ];
				for (int i = 0; i < wanted.length; i++) {
					wanted[i] = new ArrayList<String>();
					wanted[i].add( corresValue[i] );
				}
				
				//CHECK CORRES VALUE:
				
//				if( check == false ){
//					return null;
//				}
				List list = rootNode.getChildren( "row" );
				boolean[] visited = new boolean[ corresValue.length ];
				for( int i = 0 ;i < list.size() ;i++){
					Element node = (Element) list.get(i);
					boolean finalCheck = false;
					if( condition != null ){
						String current = node.getChildText( condition[0][0] );
						finalCheck = false;
						String t = getType( tableName ,condition[0][0] );
						String[] newCondition = new String[3];
						newCondition[ 0 ] = condition[0][ 0 ];
						newCondition[ 1 ] = condition[0][ 1 ];
						newCondition[ 2 ] = condition[0][ 2 ];
						finalCheck = checkCondition( newCondition ,t,current );
						
						for( int j = 0 ;j < operators.length ;j++ ){
							t = getType( tableName , condition[j+1][0] );
							current = node.getChildText( condition[j+1][0]  );
							newCondition[ 0 ] = condition[j+1][0];
							newCondition[ 1 ] = condition[j+1][1];
							newCondition[ 2 ] = condition[j+1][2];
							boolean currentCheck = checkCondition(  newCondition , t,current);
							if( operators[j].equalsIgnoreCase("AND") ){
								finalCheck = finalCheck && currentCheck;
							}//end if.
							else if( operators[j].equalsIgnoreCase("OR") ){
								finalCheck = finalCheck || currentCheck;
							}//end if.
							
						}//end for j.
					}//end if.
					else{
						finalCheck = true;
					}//end else
					
					if( finalCheck ){
						for( int j = 0 ;j < corresValue.length ;j++){
							if( !visited[j] && !checkStatus(tableName, corresValue[j] , "readable" ) ){
								System.out.println("The attribute " + corresValue[j] + "is not readable.");
								visited[j] = true;
								wanted[j].add("");
							}else{
								wanted[j].add( node.getChildText(corresValue[j]) );
							}
						}//end for j.
								
					}//end if
					
				}//end for i.
				
//				for( int i = 0 ;i < wanted.length ;i++ ){
//					if( wanted[i].size() > 1  ){
//						if(wanted[i].get(1) == null){
//							System.out.println("You have to sent valid attributes to Select.");
//							return null;
//						}		
//					}	
//				}//end for i.
				tableNameSelect = tableName;
				counter = wanted[0].size() - 1;
				System.out.println( "Number of Rows affected: " + counter );
				return wanted;
			 } catch (IOException io) {
				 System.out.println("File exception in selectFromTable.");
				return null;
			  } catch (JDOMException e) {
				System.out.println("JDOM Exception in selectFromTable.");
				return null;
			  }catch( Exception e){
				System.out.println( "You have to stick to the type in selectFromTable." ); 
				return null;
			  }
				
			
		}//end if check exist.
		else{
			System.out.println("Table " +  tableName + " doesnot exist.");
			return null;
		}
	}//end method select from table.

	@Override
	public boolean deleteFromTable( String tableName ,String[][] condition ,String[] operators ) {
		// TODO Auto-generated method stub
		// yenf3 delete table???
		counter = -1;
		if( checkTableExist(tableName) ){
			try {
				
				String dir = path + tableName +"_" + databaseName + ".xml";
				SAXBuilder builder = new SAXBuilder();
				File xmlFile = new File( dir );
				
				Document doc = (Document) builder.build(xmlFile);
				Element rootNode = doc.getRootElement();
				
				if( condition[0][0].equals("*") ){
					List list = rootNode.getChildren();
					counter = list.size();
					rootNode.removeContent();
				}else{
					
					if( operators == null ){
						operators = new String[0];
					}
					
					for( int i = 0 ;i <= operators.length ;i++ ){
						if( !check( tableName ,condition[i][0] ) ){  System.out.println("Invalid Col : " + condition[i][0] +".");  return false;}
						String t = getType( tableName ,condition[i][0] );
						if( t == null ){ System.out.println( condition[i][0]+" is not in the database." ); return false;}
						if( !checkStatus(tableName, condition[i][0] , "searchable" ) ){
							System.out.println("The attribute " + condition[i][0] + "is not searchable,You cannot update.");
							return false;
						}
						if( t.equals("String") && ( !condition[i][ 1 ].equals("=") && !condition[i][1].equals("<>") ) ){
							System.out.println("Not a valid Condition.");
							return false;
						}else if( t.equals("int") ){
							int x = Integer.parseInt( condition[i][ 2 ] );
						}else if (t.equals("double")){
							double x = Double.parseDouble( condition[i][ 2 ] );
						}//end else if. 
						else if( t.equals("float") ){
							float x = Float.parseFloat( condition[i][ 2 ] );
						}else if( t.equals("long") ){
							long x = Long.parseLong( condition[i][ 2 ] );
						}
					}//end for i.
					counter = 0;
					List list = rootNode.getChildren();
					for( int i = 0 ;i < list.size() ;i++){
						Element node = (Element) list.get(i);
						String current = node.getChildText( condition[0][0] );
						boolean finalCheck = false;
						String t = getType( tableName ,condition[0][0] );
						String[] newCondition = new String[3];
						newCondition[ 0 ] = condition[0][ 0 ];
						newCondition[ 1 ] = condition[0][ 1 ];
						newCondition[ 2 ] = condition[0][ 2 ];
						finalCheck = checkCondition(  newCondition , t , current);
						for( int j = 0 ;j < operators.length ;j++ ){
							t = getType( tableName , condition[j+1][0] );
							current = node.getChildText( condition[j+1][0] );
							newCondition[ 0 ] = condition[j+1][0];
							newCondition[ 1 ] = condition[j+1][1];
							newCondition[ 2 ] = condition[j+1][2];
							boolean currentCheck = checkCondition(  newCondition , t , current);
							if( operators[j].equalsIgnoreCase("AND") ){
								finalCheck = finalCheck && currentCheck;
							}//end if.
							else if( operators[j].equalsIgnoreCase("OR") ){
								finalCheck = finalCheck || currentCheck;
							}//end if.
						}//end for j.
						if( finalCheck ){
							rootNode.removeContent( node );
							// VERY IMPORTANT:
							counter++;
							i--;
						}
						
					}//end for i.
					
				}//end if.
				
				
				XMLOutputter xmlOutput = new XMLOutputter();
				// display nice nice
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, new FileWriter( dir ));
				System.out.println("Number of Rows affected: " + counter +".");
				return true;
			  } catch (IOException io) {
				  counter = -1;
				  System.out.println("File Error in delete.");
				return false;
			  } catch (JDOMException e) {
				  counter = -1;
				  System.out.println("JDOM Exception  in delete.");
				return false;
			  }
				catch(Exception e){
					counter = -1;
					System.out.println("Error in delete.");
				  return false;
			  }
		}//end if
		else{
			System.out.println("Table " +  tableName + " doesnot exist.");
			return false;
		}
		
		
	}//end method delete from table.

	@Override
	public boolean updateTable( String tableName , String[] corresValue ,String[] value ,String[][] condition ,String[] operators ) {
		// TODO Auto-generated method stub
		
		if( checkTableExist(tableName) ){
			
			try {
				 String dir = path + tableName +"_" + databaseName + ".xml";
				
				 SAXBuilder builder = new SAXBuilder();
				File xmlFile = new File( dir );
				
				Document doc = (Document) builder.build(xmlFile);
				Element rootNode = doc.getRootElement();
				
				if( operators == null ){
					operators = new String[0];
				}
				
				for( int i = 0 ;i <= operators.length ;i++ ){
					if( !check( tableName ,condition[i][0] ) ){  System.out.println("Invalid Col : " + condition[i][0] +".");	 return false;}
					String t = getType( tableName ,condition[i][0] );
					if( t == null ){ System.out.println(condition[i][0] + " is not in the database." ); }
					if( !checkStatus(tableName, condition[i][0], "searchable" ) ){
						System.out.println("The attribute " + corresValue[i] + "is not searchable.");
						return false;
					}
					if( t.equals("String") && ( !condition[i][ 1 ].equals("=") && !condition[i][1].equals("<>") ) ){
						System.out.println("Not a valid Condition.");
						return false;
					}else if( t.equals("int") ){
						int x = Integer.parseInt( condition[i][ 2 ] );
					}else if (t.equals("double")){
						double x = Double.parseDouble( condition[i][ 2 ] );
					}//end else if. 
					else if( t.equals("float") ){
						float x = Float.parseFloat( condition[i][ 2 ] );
					}else if( t.equals("long") ){
						long x = Long.parseLong( condition[i][ 2 ] );
					}
				}//end for j.
				List list = rootNode.getChildren();
//				for( int i = 0 ;i < list.size() ;i++){
				if( list.size() == 0){
					System.out.println("You have to insert first");
					return false;
				}
				
					for( int i = 0 ;i < corresValue.length ;i++ ){
						if( !check( tableName ,corresValue[i] ) ){  System.out.println("Invalid Col : " + corresValue[i] + "."); return false;}
						String t = getType( tableName ,corresValue[i] );
						if( !checkStatus(tableName, corresValue[i], "nullable" ) && value[i].equalsIgnoreCase("null") ){
							System.out.println("The attribute " + corresValue[i] + " is not nullable,You cannot insert.");
							return false;
						}
						if(  !checkStatus(tableName, corresValue[i], "writable" ) ){
							System.out.println("The attribute " + corresValue[i] + " is not Writtable,You cannot insert.");
							return false;
						}
						if( checkStatus(tableName, corresValue[i], "autoincrement" ) ){
							System.out.println("The attribute " + corresValue[i] + " is autoincremented,You cannot insert.");
							return false;
						}
						if( t == null ){
							System.out.println("The attribute " + corresValue[i] + " doesnot exist.");
							return false;
						}else{
							
							if( t.equalsIgnoreCase("int") ){
									int x = Integer.parseInt( value[i] );
							}else if( t.equalsIgnoreCase( "double" ) ){
									double x = Double.parseDouble( value[i] );
							}//end else if.
							else if( t.equalsIgnoreCase("float") ){
								float x = Float.parseFloat( value[i] );
							}
							else if( t.equalsIgnoreCase("long") ){
								long x = Long.parseLong( value[i] );
							}
						}//end else.
						
					}//end for j.
					
//				}//end for i.
				
				counter = 0;
				for( int i = 0 ;i < list.size() ;i++){
					Element node = (Element) list.get(i);
					String current = node.getChildText( condition[0][0] );
					boolean finalCheck = false;
					String t = getType( tableName ,condition[0][0] );
					String[] newCondition = new String[3];
					newCondition[ 0 ] = condition[0][ 0 ];
					newCondition[ 1 ] = condition[0][ 1 ];
					newCondition[ 2 ] = condition[0][ 2 ];
					finalCheck = checkCondition( newCondition ,t,current );
					
					for( int j = 0 ;j < operators.length ;j++ ){
						t = getType( tableName , condition[j+1][0] );
						current = node.getChildText( condition[j+1][0]  );
						newCondition[ 0 ] = condition[j+1][0];
						newCondition[ 1 ] = condition[j+1][1];
						newCondition[ 2 ] = condition[j+1][2];
						boolean currentCheck = checkCondition(  newCondition , t,current);
						if( operators[j].equalsIgnoreCase("AND") ){
							finalCheck = finalCheck && currentCheck;
						}//end if.
						else if( operators[j].equalsIgnoreCase("OR") ){
							finalCheck = finalCheck || currentCheck;
						}//end if.
						
					}//end for j.
					
					if( finalCheck ){
						counter++;
						for( int j = 0 ; j < corresValue.length ;j++){
							node.getChild(corresValue[j]).setText( value[j] );
						}//end for j.
					}//end if.
					
				}//end for i.
				
				XMLOutputter xmlOutput = new XMLOutputter();
				// display nice nice
				xmlOutput.setFormat(Format.getPrettyFormat());
				xmlOutput.output(doc, new FileWriter( dir ));
				System.out.println("Number of Rows affected: " + counter + ".");
				return true;
			  } catch (IOException io) {
				  counter = -1;
				  System.out.println("File Error.");
				return false;
			  } catch (JDOMException e) {
				  counter = -1;
				  System.out.println("JDOM Exception.");
				return false;
			  }catch(Exception e){
				  counter = -1;
				  System.out.println("You have to stick to the type.");
				  return false;
			  }
		}//end if
		else{
			System.out.println("Table " +  tableName + " doesnot exist.");
			counter = -1;
			return false;
		}
	}//end method update table.
	
	public boolean checkTableExist( String tableName ){
		
		String dir = path + tableName +"_" + databaseName+".xml";
		File f = new File(dir);
		if( f.exists() ){
			return true;
		}
		
		return false;
	}//end method checkTableExist.
	
	public boolean updateDatabaseFile(String tableName , ArrayList<String> values, ArrayList<String> types ,ArrayList< HashMap<String , Boolean>> list ){
		//saving the name of the table in db file.
					
					counter = -1;
					try {
					
					if( checkTableExist( tableName ) ){
						System.out.println("Table " +  tableName + " doesnot exist.");
						return false;
					}
					
					// update staff id attribute
					SAXBuilder builder = new SAXBuilder();
					
					File xmlFile = new File( path + databaseName +".xml" );
					
					Document doc = (Document) builder.build(xmlFile);
					Element rootNode = doc.getRootElement();
					
					Element tableN =  new Element("table").setAttribute( "name",tableName );
					rootNode.addContent(tableN );
					
					
					Element row = new Element("rowSpecial").setAttribute("currentNum",0 +"");
					for( int i = 0 ; i < types.size() ;i++){
						Element r = new Element( values.get(i));
						r.setText(values.get(i));
						r.setAttribute("type" ,types.get(i));
						r.setAttribute("autoincrement", list.get(i).get("autoincrement")+"" );
						r.setAttribute("nullable", list.get(i).get("nullable")+"" );
						r.setAttribute("readable", list.get(i).get("readable")+"" );
						r.setAttribute("searchable", list.get(i).get("searchable")+"" );
						r.setAttribute("writable", list.get(i).get("writable")+"" );
//						row.addContent(new Element( values.get(i) ).setText( values.get(i) ) .setAttribute("type" ,types.get(i)));
						row.addContent(r);
					}//end for i.
					tableN.addContent(row);
					
					XMLOutputter xmlOutput = new XMLOutputter();
					xmlOutput.setFormat(Format.getPrettyFormat());
					xmlOutput.output(doc, new FileWriter( path + databaseName +".xml" ));
					return true;
				 } catch (IOException io) {
					 counter = -1;
						System.out.println("File not found!!");
					return false;
				  } catch (JDOMException e) {
					 System.out.println( "JDOM Exception!!");
					 counter = -1;
					return false;
				  }
				
	}//end method update DB File.
	
	//el condition array gaylay metzabata.
	public boolean checkCondition( String[] condition ,String type ,String current){
		
		try{
			//boolean lsaaaaaaaaaaaaaaaaaaa.
			if( current == null || current.equals("") ){
				return false;
			}
			if( type.equalsIgnoreCase("int") ){
				int x = Integer.parseInt( current );
				int y = Integer.parseInt( condition[2] );
				if( condition[1].equals(">") && x > y ){
					return true;
				}
				else if( condition[1].equals("<") && x < y ){
					return true;
				}
				else if( condition[1].equals("=") && x == y ){
					return true;
				}
				else if( condition[1].equals("<>") && x != y ){
					return true;
				}
			}//end if.
			else if( type.equalsIgnoreCase("double") ){
				double x = Double.parseDouble( current );
				double y = Double.parseDouble( condition[2] );
				
				if( condition[1].equals(">") && x > y ){
					return true;
				}
				else if( condition[1].equals("<") && x < y ){
					return true;
				}
				else if( condition[1].equals("=") && x == y ){
					return true;
				}
				else if( condition[1].equals("<>") && x != y ){
					return true;
				}
			}//end else if
			else if( type.equalsIgnoreCase("String") ){
				if( condition[1].equals("=") && current.equals(condition[2]) ){
					return true;
				}
				else if( condition[1].equals("<>") && !current.equals(condition[2]) ){
					return true;
				}
			}//end else if.
			else if( type.equalsIgnoreCase("float") ){
				float x = Float.parseFloat( current );
				float y = Float.parseFloat( condition[2] );
				
				if( condition[1].equals(">") && x > y ){
					return true;
				}
				else if( condition[1].equals("<") && x < y ){
					return true;
				}
				else if( condition[1].equals("=") && x == y ){
					return true;
				}
				else if( condition[1].equals("<>") && x != y ){
					return true;
				}
			}//end else if.
			else if ( type.equalsIgnoreCase("long") ){
				long x = Long.parseLong( current );
				long y = Long.parseLong( condition[2] );
				
				if( condition[1].equals(">") && x > y ){
					return true;
				}
				else if( condition[1].equals("<") && x < y ){
					return true;
				}
				else if( condition[1].equals("=") && x == y ){
					return true;
				}else if( condition[1].equals("<>") && x != y ){
					return true;
				}
			}//end else if.
			return false;
		}catch( Exception e ){
			System.out.println("You have to Stick to the type.");
			return false;
		}
		
	}//end method checkCondition.
	
	public String getType( String tableName ,String value ){
		
		try {
			
			String dir = path + databaseName + ".xml";
			 SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File( dir );
			
			Document doc = (Document) builder.build(xmlFile);
			
			Element rootNode = doc.getRootElement();
			Element tableN = null;
			
			List list =  rootNode.getChildren();
			for( int i = 0 ;i < list.size() ;i++){
				Element node = (Element) list.get(i);
				String typeShape = node.getAttributeValue("name");
				if( typeShape.equals(tableName) ){
					tableN = node.clone();
				}
			}//end for i.
			Element rowSpecial = tableN.getChild("rowSpecial");
			String t = rowSpecial.getChild( value ).getAttributeValue("type");
			return t;
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}catch(Exception e){
			System.out.println("Type Doesnot Exist.");
			return null;
		}
		
	}//end method getType.
	
	public List getList(String tableName){
		try {
			String dir = path + databaseName + ".xml";
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File( dir );
			Document doc = (Document) builder.build(xmlFile);
		
			Element rootNode = doc.getRootElement();
			
			Element tableN = null;
			List list =  rootNode.getChildren();
			for( int i = 0 ;i < list.size() ;i++){
				Element node = (Element) list.get(i);
				String typeShape = node.getAttributeValue("name");
				if( typeShape.equals(tableName) ){
					tableN = node.clone();
				}
			}//end for i.
			
				
			Element rowSpecial = tableN.getChild("rowSpecial");
			
			List list2 = rowSpecial.getChildren();
			
			return list2;
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			System.out.println("List is not accepted.");
			return null;
		}
	}//end method getList.
	
	public void setPathDBMS(String name){
		path = name;
	}//end method
	
	public void setURL( String name ){
		
		String temp = "";
		int index = -1;
		for( int i = name.length() - 1 ; i >= 0 ;i--){
			temp = name.charAt(i) + temp;
			if( name.charAt(i) == '.' && temp.equals(".xml") ){
				index = i;
			}
			
		}//end for i.
		String dbName = "";
		for( int i = index - 1 ;i >= 0; i-- ){
			if( name.charAt(i) == '\\' ){
				index = i;
				break;
			}
			dbName = name.charAt(i) + dbName;
		}//end for i.
		path = "";
		for( int i = index ;i >= 0; i-- ){
			path = name.charAt(i) + path;
		}//end for i.
		
		useDatabase(dbName);
	}//end method.
	
	public void releaseDatabase(){
		path = "";
		databaseName = "";
	}
	
	public String getTableName(){
		return tableNameSelect;
	}//end method.
	
	public int getCounter(){
		return counter;
	}
	
	//getting type for ResultSet.
	public String getType( String tableName ,int colIndex ,ArrayList<String>[] adjList ){
		
		try {
			 String dir = path + databaseName + ".xml";
			 SAXBuilder builder = new SAXBuilder();
		 	 File xmlFile = new File( dir );
			
			Document doc = (Document) builder.build(xmlFile);
			
			Element rootNode = doc.getRootElement();
			Element tableN = null;
			
			List list =  rootNode.getChildren();
			for( int i = 0 ;i < list.size() ;i++){
				Element node = (Element) list.get(i);
				String typeShape = node.getAttributeValue("name");
				if( typeShape.equals(tableName) ){
					tableN = node.clone();
				}
			}//end for i.
			Element rowSpecial = tableN.getChild("rowSpecial");
			String t = rowSpecial.getChild( adjList[colIndex].get(0) ).getAttributeValue("type");
			
			return t;
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}catch(Exception e){
			return null;
		}
		
		
	}//end method.
	
	public boolean checkStatus( String tableName ,String colName ,String valueWanted ){
		String check = getStatus( tableName , colName , valueWanted );
		if( check == null){
			System.out.println("Value of " + valueWanted + " not found.");
			return false;
		}
		else if( check.equals("true") ){
			return true;
		}
		return false;
	}//end method checkStatus.
	
	public String getStatus( String tableName ,String colName ,String valueWanted ){
		try {
			String dir = path + databaseName + ".xml";
			 SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File( dir );
			Document doc = (Document) builder.build(xmlFile);
			Element rootNode = doc.getRootElement();
			Element tableN = null;			
			
			List list =  rootNode.getChildren();
			for( int i = 0 ;i < list.size() ;i++){
				Element node = (Element) list.get(i);
				String typeShape = node.getAttributeValue("name");
				if( typeShape.equals(tableName) ){
					tableN = node.clone();
				}
			}//end for i.
			Element rowSpecial = tableN.getChild("rowSpecial");
			String t = rowSpecial.getChild( colName ).getAttributeValue( valueWanted );
			
			return t;
		} catch (JDOMException e) {
			return null;
		} catch (IOException e) {
			return null;
		}catch(Exception e){
			return null;
		}
		
	}//end method.
	
	public String getPath(){
		return path;
	}//end method.
	
	public int getCurrentIndex( String tableName ){
		try {
			String dir = path + databaseName + ".xml";
			 SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File( dir );
			
			Document doc = (Document) builder.build(xmlFile);
			Element rootNode = doc.getRootElement();
			Element tableN = null;			
			List list =  rootNode.getChildren();
			for( int i = 0 ;i < list.size() ;i++){
				Element node = (Element) list.get(i);
				String typeShape = node.getAttributeValue("name");
				if( typeShape.equals(tableName) ){
					tableN = node.clone();
				}
			}//end for i.
			String t = tableN.getChild("rowSpecial").getAttributeValue("currentNum");
			int x = Integer.parseInt( t );
			
			return x;
		} catch (JDOMException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}catch(Exception e){
			return -1;
		}
		
	}//end method.
	
	public void setCurrentIndex( String tableName ,int currentIndex ){
		
		try {
			String dir = path + databaseName + ".xml";
			 SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File( dir );
			
			Document doc = (Document) builder.build(xmlFile);
			Element rootNode = doc.getRootElement();
			Element tableN = null;			
			List list =  rootNode.getChildren();
			for( int i = 0 ;i < list.size() ;i++){
				Element node = (Element) list.get(i);
				String typeShape = node.getAttributeValue("name");
				if( typeShape.equals(tableName) ){
					node.getChild("rowSpecial").removeAttribute("currentNum");
					node.getChild("rowSpecial").setAttribute("currentNum",currentIndex+"");
					tableN = node.clone();
				}
			}//end for i.
			XMLOutputter xmlOutput = new XMLOutputter();
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter( path + databaseName +".xml" ));
			
		} catch (JDOMException e) {
		} catch (IOException e) {
		}catch(Exception e){
			System.out.println("here in set index!!");
		}
		
		
	}//end method.
	
	public String getDatabaseName(){
		return databaseName;
	}
	
	public boolean check( String tableName , String name ){
		try {
			String dir = path + databaseName + ".xml";
			 SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File( dir );
			Document doc = (Document) builder.build(xmlFile);
			Element rootNode = doc.getRootElement();
			Element tableN = null;			
			List list =  rootNode.getChildren();
			for( int i = 0 ;i < list.size() ;i++){
				Element node = (Element) list.get(i);
				String typeShape = node.getAttributeValue("name");
				if( typeShape.equals(tableName) ){
					tableN = node.clone();
				}
			}//end for i.
			 Element n = tableN.getChild("rowSpecial").getChild( name );
			 n.getText();
			return true;
		}catch (JDOMException e) {	return false;}
		catch (IOException e) {return false;}
		catch(Exception e){return false;}
	}//end method.
	
	public void setDb(String name){
		try {
			String dir = path + name+".xml";
			FileReader fr = new FileReader(dir);
			databaseName = name;
		} catch (FileNotFoundException e) {
			System.out.println("Database Not Found.");
			databaseName = "";
		}
		
	}//end method
	
}//end Class.
