package edu.wpi.GoldenGandaberundas.tableControllers.DBConnection;

import edu.wpi.GoldenGandaberundas.TableController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.sqlite.SQLiteConfig;

public class ConnectionHandler {
  private final String dbPath = new String("jdbc:sqlite:hospitalData.db");
  private static ConnectionHandler instance = null;
  private final String clientServerPath =
      new String("jdbc:sqlserver://130.215.250.187:58910;databaseName=gandaberunda");
  private ConnectionType connectionType = ConnectionType.embedded;

  private ArrayList<TableController> embeddedTables = new ArrayList<TableController>();
  private ArrayList<TableController> clientServerTables = new ArrayList<TableController>();
  private ArrayList<TableController> cloudTables = new ArrayList<TableController>();
  /** connection object used to access the database */
  protected static Connection connection;

  /** creates a ConnectionHandler object that is connected to embedded database by default */
  private ConnectionHandler() {
    try {
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      Class.forName("org.sqlite.JDBC"); // check if we have the drive
      connection =
          DriverManager.getConnection(dbPath, config.toProperties()); // create a connection object
      PreparedStatement s = connection.prepareStatement("PRAGMA foreign_keys = ON;");
      s.execute();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * generates or returns singular ConnectionHandler object
   *
   * @return ConnectionHandler object
   */
  public static ConnectionHandler getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {

          instance = new ConnectionHandler();
        }
      }
    }
    return instance;
  }

  public ConnectionType getCurrentConnectionType() {
    return connectionType;
  }

  /**
   * adds table to the appropriate set based on current connection
   *
   * @param table
   */
  public void addTable(TableController table, ConnectionType connectionType) {
    switch (connectionType) {
      case embedded:
        embeddedTables.add(table);
        break;
      case clientServer:
        clientServerTables.add(table);
        break;
      case cloud:
        cloudTables.add(table);
        break;
    }
  }

  /**
   * changes the connection of the database
   *
   * @param connectionType
   */
  public void setConnection(ConnectionType connectionType) {
    if (this.connectionType != connectionType) {

      try {
        if (connectionType == ConnectionType.embedded) {
          SQLiteConfig config = new SQLiteConfig();
          config.enforceForeignKeys(true);
          connection = DriverManager.getConnection(dbPath, config.toProperties());
          this.transferAllData(this.connectionType, connectionType);
        } else if (connectionType == ConnectionType.clientServer) {
          this.connection = DriverManager.getConnection(clientServerPath, "admin", "admin");
          this.transferAllData(this.connectionType, connectionType);
        } else if (connectionType == ConnectionType.cloud) {
          this.transferAllData(this.connectionType, connectionType);
        } else {
          System.err.println("Connection type error in ConnectionHandler");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      this.connectionType = connectionType;
    }
  }

  /** @return current connection type */
  public Connection getConnection() {
    return connection;
  }

  /**
   * gets a table set depending on the given connection
   *
   * @param connectionType
   * @return
   */
  private ArrayList<TableController> getConnectionSet(ConnectionType connectionType) {
    switch (connectionType) {
      case embedded:
        return embeddedTables;
      case clientServer:
        return clientServerTables;
      case cloud:
        return cloudTables;
    }
    return null;
  }

  /**
   * transfers all data from current table set to newly connected tableset
   *
   * @param old
   * @param update
   */
  private void transferAllData(ConnectionType old, ConnectionType update) {
    ArrayList<TableController> oldTables = getConnectionSet(old);
    ArrayList<TableController> newTables = getConnectionSet(update);
    System.out.println(this.getConnection());
    for (int i = 0; i < oldTables.size(); i++) {
      var objList = oldTables.get(i).getObjList();
      newTables.get(i).loadFromArrayList(objList);
    }
  }
}
