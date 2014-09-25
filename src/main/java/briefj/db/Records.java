package briefj.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;


public class Records
{
  
  private Connection conn;
  final private String CONN_PATH;
  private String DB_NAME = "index.db";
  final private String DB_TABLE = "run";
  final private LinkedHashMap<String,String> options, output;
  final private String folderLocation;
  
  public Records(LinkedHashMap<String,String> options, LinkedHashMap<String,String> output, File execFolderLocation)
  {
    this(options, output, execFolderLocation, new File(System.getenv().get("CONN_PATH")));
  }
  
  public Records(LinkedHashMap<String,String> options, LinkedHashMap<String,String> output, File execFolderLocation, File dbLocation)
  {
    this.options = options;
    this.output = output;
    this.folderLocation = execFolderLocation.toString();
    this.CONN_PATH = dbLocation.toString();
  }
  
  public void recordFullRun() 
  {
    ensureInitalized();
    createTable();   
    insertInto();
  }
  
  private static Set<String> cleanSet(LinkedHashMap<String,String> set)
  {
    Set<String> clean = new LinkedHashSet<String>(); 
    for(String val : set.keySet())
      clean.add(val.replace(".", ""));
    return clean;
  }
  
  private void alterTable(String newCol)
  {
    StringBuilder str = new StringBuilder(); 
    str.append("ALTER TABLE ");
    str.append(DB_TABLE);
    str.append(" ADD column ");
    str.append(newCol);
    str.append(" string DEFAULT NULL");

    try
    {
      Statement statement = conn.createStatement();
      statement.execute(str.toString());
     
    } catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  
  
  private void insertInto()
  {
    Set<String> variables = getCurrentCols();
    
    int nRecorded = variables.size();
    int n2Input = options.size() + output.size() + 3;
    
    if (nRecorded != n2Input)
    {
      Set<String> addCols = cleanSet(options);
      addCols.addAll(cleanSet(output));
      addCols.removeAll(variables);
      
      for(String newCol : addCols)
        alterTable(newCol);

    }   
 
    
    
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
  
  private static void mapKey2String(LinkedHashMap<String,String> map, StringBuilder colNames)
  {
    for (String value : map.keySet())
    {
      String newCol = ", " +  value.replace(".", "") + " string DEFAULT NULL";
      colNames.append(newCol);
    }
  }
  
  private static void insertStatement(LinkedHashMap<String,String> map, StringBuilder strNames, StringBuilder strValues)
  {
    for(String value: map.keySet())
    {
      strNames.append(value.replace(".", "") + ", ");
      strValues.append("'" + map.get(value) + "', ");
    }
    strNames.deleteCharAt(strNames.lastIndexOf(","));
    strValues.deleteCharAt(strValues.lastIndexOf(","));
  }
  
  private Set<String> getCurrentCols()
  {

    Set<String> variables = new HashSet<String>();

    try
    {
      Statement statement = conn.createStatement();
      ResultSet res = statement.executeQuery("pragma table_info(run)");
      while( res.next())
      {
        variables.add(res.getString("name"));
      }
      res.close();
    } catch (SQLException e)
    {
      e.printStackTrace();
    }
    return variables;
  }

  
  public void ensureInitalized()
  {
    if (conn != null)
      return;
    connect();
  }
  
  
  public void connect()
  {
    try
    {
      Class.forName("org.sqlite.JDBC");
      conn = DriverManager.getConnection("jdbc:sqlite:/" + CONN_PATH + "/" + DB_NAME);
    } catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public void setDB_NAME(String dB_NAME)
  {
    DB_NAME = dB_NAME;
  }
  
}
