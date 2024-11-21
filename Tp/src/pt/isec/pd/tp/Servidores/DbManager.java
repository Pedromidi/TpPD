package pt.isec.pd.tp.Servidores;

import javax.swing.*;
import java.beans.DefaultPersistenceDelegate;
import java.io.File;
import java.sql.*;

public class DbManager {
    String dbPath;
    String dbName;
    String dbAdress;
    Connection connection;
    int dbVersion;
    String lastQuery;
    boolean updated;

    public DbManager(String dbAdress, String dbName){
        this.dbAdress = dbAdress;
        this.dbName = dbName;
        this.lastQuery = "none";
        this.updated = false;
        //this.dbPath = "jdbc:sqlite:" + dbAdress + File.separator + dbName;
        this.dbPath = "jdbc:sqlite:" + dbAdress + File.separator + dbName;
    }

    public String connect (){
        try {
            connection = DriverManager.getConnection(dbPath);
            connection.setAutoCommit(true);
            return "Connection to SQLite has been established.";

        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    public int getDbVersion() {
        String query = "SELECT numero FROM versao";
        try (PreparedStatement s = connection.prepareStatement(query);
             ResultSet rs = s.executeQuery()) {
            if (rs.next()) {
                dbVersion = rs.getInt("numero");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 666; //Codigo de erro, numero do diabo e tal, parece-me apropriado
        }
        return dbVersion;
    }

    public void incDbVersion(){
        String query = "UPDATE versao SET numero = numero + 1";
        try (PreparedStatement s = connection.prepareStatement(query)) {
            int rowsAffected = s.executeUpdate();
            if (rowsAffected > 0) {
                dbVersion = getDbVersion();
                updated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLastQuery(){
        return lastQuery;
    }

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    //Alterações--------------------------------------------------------------------------------------------------------------------

    public boolean adicionaRegisto(String email, String nome, String telefone, String password){
        String query = "INSERT INTO utilizador (email, nome, telefone, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, nome);
            stmt.setString(3, telefone);
            stmt.setString(4, password);
            stmt.executeUpdate();
            setLastQuery(query);
            return true; // Sucesso
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Falha
        }
    }

    public boolean alteraNome(String email, String novoNome) {
        String query = "UPDATE Utilizadores SET nome = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novoNome);
            stmt.setString(2, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alteraTelefone(String email, String novoTelefone) {
        String query = "UPDATE Utilizadores SET telefone = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novoTelefone);
            stmt.setString(2, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alteraEmail(String email, String novoEmail) {
        String query = "UPDATE Utilizadores SET email = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novoEmail);
            stmt.setString(2, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alteraPassword(String email, String novaPassword) {
        String query = "UPDATE Utilizadores SET password = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novaPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



//Verificacões------------------------------------------------------------------------------------------------------------------
    /**
     * Verifica se o email existe
     * com "SELECT EXISTS (select 1 FROM utilizador WHERE email = ?)"
     * A query devolve 1 se existir e 0 se nao existir
     * @return true, existe;  false, nao existe
     */
    public Boolean verificaEmail(String email){

        String query = "SELECT EXISTS (select 1 FROM utilizador WHERE email = ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,email); //troca o primeiro '?' pelo email

            ResultSet rs = stmt.executeQuery();
            return rs.getBoolean(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica se o email existe
     * com "SELECT EXISTS (select 1 FROM utilizador WHERE email = ? AND password = ?)"
     * A query devolve 1 se existir e 0 se nao existir
     * @return true, existe;  false, nao existe
     */
    public Boolean verificaPassword(String email, String password){

        String query = "SELECT EXISTS (select 1 FROM utilizador WHERE email = ? AND password = ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,email); //troca o primeiro '?' pelo email
            stmt.setString(2,password);

            ResultSet rs = stmt.executeQuery();
            return rs.getBoolean(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica se o telefone existe
     * @return true, existe;  false, não existe
     */
    public Boolean verificaTelefone(String telefone){
        String query = "SELECT 1 FROM utilizador WHERE telefone = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, telefone);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retorna true se o telefone for encontrado
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se o utilizador está no grupo
     * @return true, está no grupo;  false, não está no grupo;
     *
     */
    public Boolean verificaGrupo(String grupo, String email){
        //TODO ir a tabela de elementos_grupo e procurar
        return true;
    }

    /**
     * Verifica se o id (despesa ou pagamento) existe
     * @return true, existe
     * @return false, nao existe
     */
    public Boolean verificaId(String id, String entidade){

        switch (entidade){
            case "despesa":
                //TODO ir a tabela de despesas e procurar o id
            case "pagamento":
                //TODO ir a tabela de pagamentos e procurar o id
        }
        return true;
    }




}
