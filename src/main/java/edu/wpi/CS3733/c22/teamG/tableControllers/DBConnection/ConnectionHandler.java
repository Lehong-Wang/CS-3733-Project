package edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection;

import edu.wpi.CS3733.c22.teamG.TableController;
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
        var oldConnection = this.connectionType;
        this.connectionType = connectionType;
        if (connectionType == ConnectionType.embedded) {
          SQLiteConfig config = new SQLiteConfig();
          config.enforceForeignKeys(true);
          connection = DriverManager.getConnection(dbPath, config.toProperties());
          this.transferAllData(oldConnection, connectionType);
        } else if (connectionType == ConnectionType.clientServer) {
          this.connection = DriverManager.getConnection(clientServerPath, "admin", "admin");
          this.transferAllData(oldConnection, connectionType);
        } else if (connectionType == ConnectionType.cloud) {
          this.transferAllData(oldConnection, connectionType);
        } else {
          System.err.println("Connection type error in ConnectionHandler");
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
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
    if (update == ConnectionType.cloud) {
      newTables.get(0).loadFromArrayList(oldTables.get(0).getObjList());
      newTables.get(1).loadFromArrayList(oldTables.get(4).getObjList());
      newTables.get(2).loadFromArrayList(oldTables.get(12).getObjList());
      newTables.get(3).loadFromArrayList(oldTables.get(15).getObjList());
      if (old != ConnectionType.embedded) {
        for (int i = 0; i < oldTables.size(); i++) {
          var objList = oldTables.get(i).getObjList();
          getConnectionSet(ConnectionType.embedded).get(i).loadFromArrayList(objList);
        }
      }
    } else if (old == ConnectionType.cloud) {
      newTables.get(0).loadFromArrayList(oldTables.get(0).getObjList());
      newTables.get(4).loadFromArrayList(oldTables.get(1).getObjList());
      newTables.get(12).loadFromArrayList(oldTables.get(2).getObjList());
      newTables.get(15).loadFromArrayList(oldTables.get(3).getObjList());
      if (update != ConnectionType.embedded) {
        for (int i = 0; i < oldTables.size(); i++) {
          var objList = oldTables.get(i).getObjList();
          newTables
              .get(i)
              .loadFromArrayList(getConnectionSet(ConnectionType.embedded).get(i).getObjList());
        }
      }
    } else {
      for (int i = 0; i < oldTables.size(); i++) {
        var objList = oldTables.get(i).getObjList();
        newTables.get(i).loadFromArrayList(objList);
      }
    }
  }
}
