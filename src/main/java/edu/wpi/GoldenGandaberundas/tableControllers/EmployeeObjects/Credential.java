package edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Credential {

  private String salt = new String("9153723056");
  private String hashedPass = null;
  private Integer empID = null;

  /**
   * ONLY TO BE USED BY TABLE CONTROLLER
   *
   * @param hashedPass
   * @param salt
   * @param empID
   */
  public Credential(int empID, String hashedPass, String salt) {
    this.salt = salt;
    this.hashedPass = hashedPass;
    this.empID = empID;
  }

  /**
   * Constructor for UI form
   *
   * @param plainText
   * @param empID
   */
  public Credential(Integer empID, String plainText) {
    this.salt = Credential.getRandomString();
    hashedPass = hashIt(plainText);
    this.empID = empID;
  }

  public Credential getInstance() {
    return this;
  }

  private synchronized String hashIt(String plainText) {
    MessageDigest messageDigest = null;
    try {
      messageDigest = MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    messageDigest.update((plainText + this.salt).getBytes(StandardCharsets.UTF_8));
    BigInteger foo = new BigInteger(messageDigest.digest());
    String hex = foo.toString(16);
    byte[] raw = messageDigest.digest();

    System.out.println(hex);
    return hex;
  }

  public static String getRandomString() {
    String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890abcdefghijklmnopqrstuvwxyz";
    StringBuilder randomString = new StringBuilder();
    Random rnd = new Random();
    while (randomString.length() < 18) { // length of the random string.
      int index = (int) (rnd.nextFloat() * CHARS.length());
      randomString.append(CHARS.charAt(index));
    }
    String randomStr = randomString.toString();
    return randomStr;
  }

  public boolean checkPassword(String plainText) {
    String hashed = hashIt(plainText);
    System.out.println("entered: " + hashed);
    System.out.println("hashedPass: " + this.hashedPass);
    if (this.hashedPass.equals(hashed)) {
      return true;
    }
    return false;
  }

  public boolean equals(Object obj) {
    Credential o = (Credential) obj;
    return (this.empID.equals(o.empID))
        && (this.salt.equals(o.salt))
        && (this.hashedPass.equals(o.hashedPass));
  }

  public String getSalt() {
    return salt;
  }

  public String getHashedPass() {
    return hashedPass;
  }

  public void setHashedPass(String hashedPass) {
    this.hashedPass = hashedPass;
  }

  public Integer getEmpID() {
    return empID;
  }

  public void setEmail(Integer empID) {
    this.empID = empID;
  }

  public String toString() {
    return empID + "," + hashedPass + "," + salt;
  }
}
