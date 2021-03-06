package edu.umkc.fridell.database;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class DbController {
  private static DbController ourInstance;
  private static DbConnection connection;

  private DbController() {
    connection = new DbConnection();
  }

  public static synchronized DbController getInstance() {
    if (ourInstance == null) {
      ourInstance = new DbController();
    }
    return ourInstance;
  }

  @Deprecated
  public Connection connection() {
    return connection.connection;
  }

  public void close() {
    try {
      connection.connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Statement createStatement() throws SQLException {
    return connection.connection.createStatement();
  }

  public List<List<Object>> query(String[] columnNames, String from) throws SQLException {
    Statement statement = createStatement();
    String queryCommand = "SELECT " + String.join(", ", columnNames) +
        " FROM " + from;

    ResultSet rs = statement.executeQuery(queryCommand);
    List<List<Object>> result = new ArrayList<>();
    while (rs.next()) {
      List<Object> row = new ArrayList<>();
      for (String col : columnNames) {
        row.add(rs.getObject(col));
      }
      result.add(row);
    }
    statement.close();
    return result;
  }

  public void delete(String from, String where) throws SQLException {
    Statement statement = createStatement();
    String command = "DELETE FROM " + from + " WHERE " + where;
    statement.execute(command);
    statement.close();
  }

  public void setAutoCommit(boolean b) throws SQLException {
    connection.setAutoCommit(b);
  }

  public PreparedStatement prepareStatement(String statement) throws SQLException {
    return connection.prepareStatement(statement);
  }

  public void commit() throws SQLException {
    connection.commit();
  }

  public void rollback() throws SQLException {
    connection.rollback();
  }

  public int count(String table) {
    try {
      Statement stmt = createStatement();
      ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as cnt FROM " + table);
      if (rs.next()) {
        return rs.getInt("cnt");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  private static class DbConnection {
    private String password;
    private String hostname;
    private String username;
    private String serverType;
    private String connectionInfo;
    private Connection connection;


    DbConnection() {
      FileReader config;

      try {
        config = new FileReader("db.json");
        if (config.ready()) {
          parseConfigFile(config);
          setConnectionInfo(hostname, serverType);
        }

      } catch (FileNotFoundException e) {
        manualConfiguration();
        setConnectionInfo(hostname, serverType);
      } catch (IOException e) {
        e.printStackTrace();
      }

      try {
        connection = DriverManager.getConnection(connectionInfo, username, password);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    private void manualConfiguration() {
      System.out.println("Configuration file not found.");
      Scanner in = new Scanner(System.in);
      System.out.println("Enter connection hostname: ");
      hostname = in.next();
      System.out.println("Enter connection type: ");
      serverType = in.next();
      System.out.println("Enter connection username: ");
      username = in.next();
      System.out.println("Enter connection password: ");
      password = in.next();
    }

    private void parseConfigFile(FileReader config) {
      JsonObject dbInfo = new JsonParser().parse(config).getAsJsonObject();
      username = dbInfo.get("connection").getAsJsonObject().get("username").getAsString();
      password = dbInfo.get("connection").getAsJsonObject().get("password").getAsString();
      hostname = dbInfo.get("connection").getAsJsonObject().get("hostname").getAsString();
      serverType = dbInfo.get("connection").getAsJsonObject().get("server_type").getAsString();
    }

    private void setConnectionInfo(String hostname, String serverType) {
      StringBuilder sb = new StringBuilder();
      sb.append("jdbc:");
      sb.append(serverType);
      sb.append("://");
      sb.append(hostname);
      connectionInfo = sb.toString();
    }

    private void setAutoCommit(boolean val) throws SQLException {
      connection.setAutoCommit(val);
    }

    private void commit() throws SQLException {
      connection.commit();
    }

    private void rollback() throws SQLException {
      connection.rollback();
    }

    private PreparedStatement prepareStatement(String statement) throws SQLException {
      return connection.prepareStatement(statement);
    }
  }

}
