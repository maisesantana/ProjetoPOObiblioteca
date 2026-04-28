package br.com.atlas.dao;

import br.com.atlas.service.Librarian;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibrarianDAO extends EmployeeDAO {

    public LibrarianDAO(Connection connection) {
        super(connection);
    }

    public void insert(Librarian lib) throws SQLException {
        super.insert(lib);

        String sql = "INSERT INTO Librarian (Cpf) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, lib.getCpf());
            stmt.executeUpdate();
        }
    }

    public boolean isLibrarian(String cpf) throws SQLException {
        String sql = "SELECT 1 FROM Librarian WHERE Cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Librarian> findAllLibrarians() throws SQLException {
        List<Librarian> list = new ArrayList<>();
        String sql = """
            SELECT p.*, e.Password
            FROM Person p
            INNER JOIN Employee e ON p.Cpf = e.Cpf
            INNER JOIN Librarian l ON p.Cpf = l.Cpf
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Librarian(
                    rs.getString("Cpf"),
                    rs.getString("Name"),
                    rs.getString("Email"),
                    rs.getString("Gender"),
                    rs.getDate("BirthDate").toLocalDate(),
                    rs.getInt("Password")
                ));
            }
        }
        return list;
    }
}