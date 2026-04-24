package br.com.atlas.dao;

import br.com.atlas.model.Client;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// representa a conexão ativa com o banco de dados
// é recebido pelo construtor e guardado no atributo connection — isso significa que quem
// criar o ClientDAO é responsável por passar a conexão
// O final garante que depois de atribuída no construtor, a conexão não pode ser trocada por outra

public class ClientDAO {

    private final Connection connection; 

    public ClientDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE — insere em Person e depois em Client
    public void insert(Client client) throws SQLException { //Avisa que esse método pode lançar erro de banco. Quem chamar o insert é responsável por tratar esse erro
        String sqlPerson = "INSERT INTO person (cpf, name, email, gender, birthDate) VALUES (?,?,?,?,?)";
        String sqlClient = "INSERT INTO client (cpf, address) VALUES (?,?)";

        // client.cpf é uma chave estrangeira que referencia person.cpf
        // Se tentar gravar em client antes, o banco rejeita

        // Garante que os dois PreparedStatement são fechados automaticamente ao final
        try (PreparedStatement stmtPerson = connection.prepareStatement(sqlPerson); // representa os "?"
            PreparedStatement stmtClient = connection.prepareStatement(sqlClient)) {

            stmtPerson.setString(1, client.getCpf());
            stmtPerson.setString(2, client.getName());
            stmtPerson.setString(3, client.getEmail());
            stmtPerson.setString(4, client.getGender());
            stmtPerson.setDate(5, Date.valueOf(client.getBirthDate()));
            stmtPerson.executeUpdate();

            stmtClient.setString(1, client.getCpf());
            stmtClient.setString(2, client.getAddress());
            stmtClient.executeUpdate(); 
            // executeUpdate() executa a query no banco. Usado para INSERT, UPDATE e DELETE
        }
    }

    // READ ALL — JOIN entre person e client
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, " +
            "c.address, c.startSuspensionDate, c.endSuspensionDate " +
            "FROM client c " +
            "JOIN person p ON c.cpf = p.cpf";
            // unindo as duas tabelas por cpf / p e c sao apelidos

        List<Client> clients = new ArrayList<>(); // Cria a lista vazia que vai receber os clientes encontrados. Se não encontrar nenhum, retorna a lista vazia 

        try (PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) { // É o objeto que guarda o resultado da query

            while (rs.next()) {
                clients.add(mapResultSet(rs));
            }

            // Enquanto houver linhas no resultado, chama o mapResultSet(rs) que transforma a linha atual num objeto Client e adiciona na lista.
        }

        return clients;
    }

    // READ BY CPF
    public Client findByCpf(String cpf) throws SQLException {
        String sql = "SELECT p.cpf, p.name, p.email, p.gender, p.birthDate, " +
            "c.address, c.startSuspensionDate, c.endSuspensionDate " +
            "FROM client c " +
            "JOIN person p ON c.cpf = p.cpf " +
            "WHERE c.cpf = ?"; // Filtra o resultado para trazer apenas o cliente com aquele CPF específico

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }

                // aq so esperamos uma unica linha, por isso o if em vez de while
            }
        }

        return null;
    }

    // UPDATE — atualiza person e client separadamente
    public void update(Client client) throws SQLException {
        String sqlPerson = "UPDATE person SET name = ?, email = ?, gender = ?, birthDate = ? WHERE cpf = ?";
        String sqlClient = "UPDATE client SET address = ?, startSuspensionDate = ?, endSuspensionDate = ? WHERE cpf = ?";

        try (PreparedStatement stmtPerson = connection.prepareStatement(sqlPerson);
            PreparedStatement stmtClient = connection.prepareStatement(sqlClient)) {

            stmtPerson.setString(1, client.getName());
            stmtPerson.setString(2, client.getEmail());
            stmtPerson.setString(3, client.getGender());
            stmtPerson.setDate(4, Date.valueOf(client.getBirthDate()));
            stmtPerson.setString(5, client.getCpf());
            stmtPerson.executeUpdate();

            stmtClient.setString(1, client.getAddress());
            // datas de suspensão podem ser null
            stmtClient.setDate(2, client.getStartSuspensionDate() != null
                    ? Date.valueOf(client.getStartSuspensionDate()) : null);
            stmtClient.setDate(3, client.getEndSuspensionDate() != null
                    ? Date.valueOf(client.getEndSuspensionDate()) : null);
            stmtClient.setString(4, client.getCpf());
            stmtClient.executeUpdate();
        }
    }


    // DELETE — deleta client primeiro, depois person (sem CASCADE no banco)
    public void delete(String cpf) throws SQLException {
        String sqlClient = "DELETE FROM client WHERE cpf = ?";
        String sqlPerson = "DELETE FROM person WHERE cpf = ?";

        try (PreparedStatement stmtClient = connection.prepareStatement(sqlClient);
            PreparedStatement stmtPerson = connection.prepareStatement(sqlPerson)) {

            stmtClient.setString(1, cpf);
            stmtClient.executeUpdate();

            stmtPerson.setString(1, cpf);
            stmtPerson.executeUpdate();
        }
    }

    // MÉTODO AUXILIAR
    private Client mapResultSet(ResultSet rs) throws SQLException {
        String cpf        = rs.getString("cpf");
        String name       = rs.getString("name");
        String email      = rs.getString("email");
        String gender     = rs.getString("gender");
        LocalDate birthDate = rs.getDate("birthDate").toLocalDate();
        String address    = rs.getString("address");

        Client client = new Client(cpf, name, email, gender, birthDate, address);

        Date start = rs.getDate("startSuspensionDate");
        Date end   = rs.getDate("endSuspensionDate");
        client.setStartSuspensionDate(start != null ? start.toLocalDate() : null);
        client.setEndSuspensionDate(end != null ? end.toLocalDate() : null);

        return client;

        // pegar uma linha do ResultSet e transformar num objeto Client.
        // Tanto o findAll quanto o findByCpf chamam ele pra não repetir esse código.
    }
}