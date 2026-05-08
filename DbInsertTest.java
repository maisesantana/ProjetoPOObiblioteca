import java.sql.*;
public class DbInsertTest {
  public static void main(String[] args) throws Exception {
    Class.forName("com.mysql.cj.jdbc.Driver");
    try (Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/atlas","root","")) {
      c.setAutoCommit(false);
      try {
        try (PreparedStatement p = c.prepareStatement("INSERT INTO person (cpf,name,email,gender,birthDate) VALUES (?,?,?,?,?)")) {
          p.setString(1, "99999999999");
          p.setString(2, "Teste");
          p.setString(3, "teste@example.com");
          p.setString(4, "m");
          p.setDate(5, Date.valueOf("1990-01-01"));
          p.executeUpdate();
        }
        try (PreparedStatement p = c.prepareStatement("INSERT INTO employee (cpf,password) VALUES (?,?)")) {
          p.setString(1, "99999999999");
          p.setInt(2, 1234);
          p.executeUpdate();
        }
        try (PreparedStatement p = c.prepareStatement("INSERT INTO attendant (cpf) VALUES (?)")) {
          p.setString(1, "99999999999");
          p.executeUpdate();
        }
        c.commit();
        System.out.println("Success");
      } catch (Exception e) {
        e.printStackTrace();
        c.rollback();
      }
    }
  }
}
