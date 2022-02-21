package edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects;

import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class EmployeeTbl extends TableController<Employee, Integer> {

  private static EmployeeTbl instance = null;

  private EmployeeTbl() throws SQLException {
    super(
        "Employees",
        Arrays.asList(
            new String[] {"empID", "fName", "lName", "role", "email", "phone number", "address"}));
    String[] cols = {"empID", "fName", "lName", "role", "email", "phone number", "address"};
    createTable();

    objList = new ArrayList<Employee>();
    objList = readTable();
  }

  public static EmployeeTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new EmployeeTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  // reads the DB table and returns the information as a ArrayList<Employee>
  @Override
  public ArrayList<Employee> readTable() {
    ArrayList tableInfo = new ArrayList<Employee>();
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Employee(
                r.getInt(1),
                r.getString(2),
                r.getString(3),
                r.getString(4),
                r.getString(5),
                r.getString(6),
                r.getString(7)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(Employee obj) {
    if (!this.getEmbedded()) {
      return addEntryOnline(obj);
    }
    Employee emp = (Employee) obj;
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement(
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?, ?, ?, ?);");

      s.setInt(1, emp.getEmpID());
      s.setString(2, emp.getFName());
      s.setString(3, emp.getLName());
      s.setString(4, emp.getRole());
      s.setString(5, emp.getEmail());
      s.setString(6, emp.getPhoneNum());
      s.setString(7, emp.getAddress());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean addEntryOnline(Employee obj) {
    try {
      PreparedStatement s =
          connection.prepareStatement(
              " IF NOT EXISTS (SELECT 1 FROM "
                  + tbName
                  + " WHERE "
                  + colNames.get(0)
                  + " = ?)"
                  + "BEGIN"
                  + "    INSERT INTO "
                  + tbName
                  + " VALUES (?, ?, ?, ?, ?, ?, ?)"
                  + "end");
      s.setInt(1, obj.getEmpID());
      s.setString(2, obj.getFName());
      s.setString(3, obj.getLName());
      s.setString(4, obj.getRole());
      s.setString(5, obj.getEmail());
      s.setString(6, obj.getPhoneNum());
      s.setString(7, obj.getAddress());
      int r = s.executeUpdate();
      if (r != 0) {
        return true;
      }
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<Employee> readBackup(String fileName) {
    ArrayList<Employee> empList = new ArrayList<Employee>();
    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("empID,fName,lName,perms,role,email,phone number,address"))) {
        System.err.println("employee backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        // check if a line is blank
        if (!currentLine.isEmpty()) {
          String[] element = currentLine.split(","); // separates each element based on a comma
          Employee emp =
              new Employee(
                  Integer.parseInt(element[0]),
                  element[1],
                  element[2],
                  element[3],
                  element[4],
                  element[5],
                  element[6]);
          empList.add(emp); // adds the location to the list
        } else {
          System.out.println("EMPTY");
        }
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return empList;
  }

  @Override
  public void createTable() {
    if (!this.getEmbedded()) {
      createOnlineTable();
      return;
    }
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM sqlite_master WHERE tbl_name = ? LIMIT 1;");
      s.setString(1, tbName);
      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }

    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
      e.printStackTrace();
      return;
    }

    System.out.println("SQLite driver registered!");

    Statement s = null;
    try {
      s = connection.createStatement();
      s.execute("PRAGMA foreign_keys = ON");
      s.execute(
          "CREATE TABLE IF NOT EXISTS  Employees(empID INTEGER NOT NULL ,fName TEXT, lName TEXT, role TEXT,email TEXT NOT NULL UNIQUE,phoneNumber TEXT,address TEXT, PRIMARY KEY ('empID'));");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void createOnlineTable() {
    try {
      PreparedStatement s1 =
          connection.prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }
      Statement s = connection.createStatement();
      s.execute(
          "CREATE TABLE  Employees(empID INTEGER NOT NULL ,fName TEXT, lName TEXT, role TEXT,email varchar(60) NOT NULL UNIQUE,phoneNumber TEXT,address TEXT, PRIMARY KEY (empID));");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Employee getEntry(Integer pkID) {

    Employee emp = new Employee();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        emp.setEmpID(r.getInt(1));
        emp.setFName(r.getString(2));
        emp.setLName(r.getString(3));
        emp.setRole(r.getString(4));
        emp.setEmail(r.getString(5));
        emp.setPhoneNum(r.getString(6));
        emp.setAddress(r.getString(7));
        return emp;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return emp;
  }
}
