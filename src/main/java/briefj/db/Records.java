package briefj.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import briefj.opt.OrderedStringMap;
import briefj.run.Results;

public class Records
{
  
  private Connection conn;
  final private String CONN_PATH;
  final private String DB_NAME = "index.db";
  final private String DB_TABLE = "run";
  final private OrderedStringMap options;
  final private OrderedStringMap output;
  final private String folderLocation;
  
  public Records(OrderedStringMap options, OrderedStringMap output, String folderLocation)
  {
    this.options = options;
    this.output = output;
    this.folderLocation = folderLocation;
    this.CONN_PATH = System.getenv().get("CONN_PATH");
  }
  
  public void recordFullRun() 
  {
    try
    {
      ensureInitalized();
    } catch (ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    createTable();   
    insertInto();
  }
  
  
  private void insertInto()
  {
    StringBuilder colNames = new StringBuilder();
    StringBuilder values = new StringBuilder();
    colNames.append(" (");
    values.append(" (");
    insertStatement(options, colNames, values);
    colNames.append(", ");
    values.append(", ");
    insertStatement(output, colNames, values);
    
    colNames.append(", folderLocation");
    values.append(", '" + folderLocation + "'");
    
    colNames.append(")");
    values.append(")");
    Statement statement;
    try
    {
      statement = conn.createStatement();
      String insert = "INSERT INTO " + DB_TABLE + colNames + 
          " VALUES" + values;
      statement.execute(insert);
    } catch (SQLException e)
    {
      e.printStackTrace();
    }
       
  }
  
  
  
  private void createTable()
  {
    StringBuilder colNames = new StringBuilder();   
    String createTable = "CREATE TABLE IF NOT EXISTS " + DB_TABLE +  
    " (id INTEGER PRIMARY KEY AUTOINCREMENT ";
    mapKey2String(options, colNames);
    mapKey2String(output, colNames);
    colNames.append(", folderLocation string, Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
    try
    {
      Statement statement = conn.createStatement();
      statement.execute(createTable + colNames.toString());
    } catch (SQLException e)
    {
      e.printStackTrace();
    }
    
  }
  
  private static void mapKey2String(OrderedStringMap map, StringBuilder colNames)
  {
    for (String value : map.keys())
    {
      String newCol = ", " +  value.replace(".", "") + " string DEFAULT NULL";
      colNames.append(newCol);
    }
  }
  
  private static void insertStatement(OrderedStringMap map, StringBuilder strNames, StringBuilder strValues)
  {
    for(String value: map.keys())
    {
      strNames.append(value.replace(".", "") + ", ");
      strValues.append("'" + map.get(value) + "', ");
    }
    strNames.deleteCharAt(strNames.lastIndexOf(","));
    strValues.deleteCharAt(strValues.lastIndexOf(","));
  }
  
  public void ensureInitalized() throws ClassNotFoundException
  {
    if (conn != null)
      return;
    connect();
  }
  
  
  public void connect() throws ClassNotFoundException
  {
    Class.forName("org.sqlite.JDBC");
    try
    {
      System.out.println(CONN_PATH + "/" + DB_NAME);
      conn = DriverManager.getConnection("jdbc:sqlite:/" + CONN_PATH + "/" + DB_NAME);
    } catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
}
