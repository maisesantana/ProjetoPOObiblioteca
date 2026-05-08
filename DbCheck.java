import java.sql.*;
public class DbCheck {
  public static void main(String[] args) throws Exception {
    Class.forName("com.mysql.cj.jdbc.Driver");
    try (Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/atlas","root","")) {
      printTable(c, "person");
      printTable(c, "employee");
      printTable(c, "attendant");
      printTable(c, "librarian");
    }
  }
  private static void printTable(Connection c, String table) throws SQLException {
    System.out.println("--- " + table + " ---");
    try (Statement s = c.createStatement(); ResultSet rs = s.executeQuery("SHOW COLUMNS FROM " + table)) {
      while (rs.next()) {
        System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
      }
    }
  }
}
