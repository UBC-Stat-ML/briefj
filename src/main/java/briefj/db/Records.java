package briefj.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


public class Records
{
  public static final String FOLDER_LOCATION_COLUMN = "folder_location";
  public static final String TIME_STAMP_COLUMN_NAME = "time_stamp";
  public static final String ID_COLUMN_NAME = "id";
  public static final String databaseTableName = "run";
  
  private final Connection conn;
  private final File dbFile;
  
  public static Records recordsFromEnvironmentVariable()
  {
    File dbFile = new File(System.getenv().get("CONN_PATH"), "index.db");
    return new Records(dbFile);
  }
  
  public Records(File dbFile)
  {
    this.conn = connect(dbFile);
    this.dbFile = dbFile;
  }
  
  public void close()
  {
    try
    {
      conn.close();
    } catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void recordFullRun(LinkedHashMap<String, String> options, LinkedHashMap<String, String> output, File execDir) 
  {
    LinkedHashMap<String, String> keyValuePairs = Maps.newLinkedHashMap();
    if (options != null)
      keyValuePairs.putAll(options);
    if (output != null)
      keyValuePairs.putAll(output);
    if (execDir != null)
      keyValuePairs.put(FOLDER_LOCATION_COLUMN, execDir.toString());
    record(keyValuePairs);
  }
  
  public void recordFullRun(LinkedHashMap<String, String> options,
      File resultDirectory)
  {
    recordFullRun(options, null, resultDirectory);
  }
  
  public void record(LinkedHashMap<String, String> keyValuePairs) 
  {
    ensureTableCreated();   
    insertInto(keyValuePairs);
  }
  
  public static LinkedHashMap<String,String> cleanColumnNames(Map<String,String> datum)
  {
    LinkedHashMap<String,String> clean = new LinkedHashMap<String,String>(); 
    for(String columnName : datum.keySet())
      clean.put(cleanColumnName(columnName), datum.get(columnName));
    
    if (datum.keySet().size() != clean.keySet().size())
      throw new RuntimeException("Cleaning sql table names created a clash. \nBefore: " + datum + "\nAfter: " + clean);
    
    return clean;
  }
  
  public Set<String> recordedExecFolders()
  {
    try
    {
      Set<String> result = Sets.newHashSet();
      Statement stmt = conn.createStatement();
      String sql = "SELECT " + FOLDER_LOCATION_COLUMN + " FROM " + databaseTableName;
      ResultSet rs = stmt.executeQuery(sql);
      
      while(rs.next())
      {
        String last = rs.getString(FOLDER_LOCATION_COLUMN);
        result.add(last);
      }
      rs.close();
      return result;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * 
   * @param string
   * @return A string suitable for unquoted sql table column name.
   */
  private static String cleanColumnName(String string)
  {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < string.length(); i++)
    {
      char current = string.charAt(i);
      if ((current >= 'a' && current <= 'z'))
        result.append(current);
      else if ((current >= 'A' && current <= 'Z'))
      {
        if (i > 0 && (string.charAt(i-1) >= 'a' && string.charAt(i-1) <= 'z'))
          result.append("_");
        result.append(Character.toLowerCase(current));
      }
      else
        result.append("_");
    }
    if (SqlKeywords.keywords.contains(result.toString()))
      return "_" + result;
    else
      return result.toString();
  }
  
  private void alterTable(String newCol)
  {
    _variables = null;
    StringBuilder str = new StringBuilder(); 
    str.append("ALTER TABLE ");
    str.append(databaseTableName);
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
  
  private void insertInto(Map<String,String> _keyValuePairs)
  {
    LinkedHashMap<String,String> keyValuePairs = cleanColumnNames(_keyValuePairs);
    Set<String> variables = getCurrentCols();
    
    int nRecorded = variables.size();
    int n2Input = keyValuePairs.size() + 2;
    
    if (nRecorded != n2Input)
    {
      Set<String> addCols = Sets.newHashSet(keyValuePairs.keySet());
      addCols.removeAll(variables);
      
      for(String newCol : addCols)
        alterTable(newCol);

    }   
    
    StringBuilder colNames = new StringBuilder();
    StringBuilder values = new StringBuilder();
    colNames.append(" (");
    values.append(" (");
    insertStatement(keyValuePairs, colNames, values);
    colNames.append(")");
    values.append(")");
    String insert = null;
    try
    {
      Statement statement = conn.createStatement();
      insert = "INSERT INTO " + databaseTableName + colNames + 
          " VALUES" + values;
      statement.execute(insert);
      statement.close();
    } catch (SQLException e)
    {
      System.out.println("Bad query: " + insert);
      throw new RuntimeException(e);
    }
       
  }
  
  private boolean _tableCreationChecked = false;
  private void ensureTableCreated()
  {
    if (_tableCreationChecked)
      return;
    StringBuilder colNames = new StringBuilder();   
    String createTable = "CREATE TABLE IF NOT EXISTS " + databaseTableName +  
    " (" + ID_COLUMN_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TIME_STAMP_COLUMN_NAME + " DATETIME DEFAULT CURRENT_TIMESTAMP)";
    try
    {
      Statement statement = conn.createStatement();
      statement.execute(createTable + colNames.toString());
      statement.close();
      _tableCreationChecked = true;
    } catch (SQLException e)
    {
      e.printStackTrace();
    }
    
  }
  
  private static void insertStatement(LinkedHashMap<String,String> map, StringBuilder strNames, StringBuilder strValues)
  {
    if (map.isEmpty())
      return;
    for(String key : map.keySet())
    {
      strNames.append(key + ", "); // already cleaned
      strValues.append("'" + (map.get(key) != null ? map.get(key).replace("'", "''") : null) + "', ");
    }
    strNames.deleteCharAt(strNames.lastIndexOf(","));
    strValues.deleteCharAt(strValues.lastIndexOf(","));
  }
  
  public ResultSet select(String entries, String constraint)
  {
    String query = "";
    try
    {
      Statement statement = conn.createStatement();
      query = "SELECT " + entries + 
          " FROM " + databaseTableName +
          (StringUtils.isEmpty(constraint) ? "" : " WHERE " + constraint);
      return statement.executeQuery(query);
    } 
    catch (SQLException e)
    {
      throw new RuntimeException("Problematic query: " + query + "\nDetails:\n" + e);
    }
  }
  
  private Set<String> _variables = null;
  public Set<String> getCurrentCols()
  {
    if (_variables != null)
      return _variables;
    
    _variables = new HashSet<String>();

    try
    {
      Statement statement = conn.createStatement();
      ResultSet res = statement.executeQuery("pragma table_info(run)");
      while( res.next())
        _variables.add(res.getString("name"));
      res.close();
    } catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
    return _variables;
  }
  
  private Connection connect(File databaseFile)
  {
    try
    {
      Class.forName("org.sqlite.JDBC");
      return DriverManager.getConnection("jdbc:sqlite:/" + databaseFile.getAbsolutePath());
    } catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public File getDbFile()
  {
    return dbFile;
  }

  
}
