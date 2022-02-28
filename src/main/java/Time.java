import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class Time {

  public static long getTime() {
    return System.currentTimeMillis();
  }

  public static String formatDate() {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    return (dateFormat.format(getTime()));
  }
}
