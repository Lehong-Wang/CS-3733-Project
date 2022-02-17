package edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects;

public class Employee {
  private int empID;
  private String FName;
  private String LName;
  private String role;
  private String email;
  private String phoneNum;
  private String address;

  public Employee() {}

  public Employee(
      int id,
      String FName,
      String LName,
      String role,
      String email,
      String phoneNum,
      String address) {
    this.empID = id;
    this.FName = FName;
    this.LName = LName;
    this.role = role;
    this.email = email;
    this.phoneNum = phoneNum;
    this.address = address;
  }

  public boolean equals(Object obj) {
    Employee o = (Employee) obj;
    return (this.empID == o.empID)
        && (this.FName.equals(o.FName))
        && (this.LName.equals(o.LName))
        && (this.role.equals(o.role))
        && (this.email.equals(o.email))
        && (this.phoneNum.equals(o.phoneNum))
        && (this.address.equals(o.address));
  }

  public String getFName() {
    return FName;
  }

  public void setFName(String FName) {
    this.FName = FName;
  }

  public String getLName() {
    return LName;
  }

  public void setLName(String LName) {
    this.LName = LName;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNum() {
    return phoneNum;
  }

  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public int getEmpID() {
    return empID;
  }

  public void setEmpID(int empID) {
    this.empID = empID;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(empID);
    stringBuilder.append(",");
    stringBuilder.append(FName);
    stringBuilder.append(",");
    stringBuilder.append(LName);
    stringBuilder.append(",");
    stringBuilder.append(role);
    stringBuilder.append(",");
    stringBuilder.append(email);
    stringBuilder.append(",");
    stringBuilder.append(phoneNum);
    stringBuilder.append(",");
    stringBuilder.append(address);

    return stringBuilder.toString();
  }
}
