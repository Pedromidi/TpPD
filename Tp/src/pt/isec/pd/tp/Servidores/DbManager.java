package pt.isec.pd.tp.Servidores;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

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

    public boolean criaGrupo(String criadorEmail, String nomeGrupo) {
        String insertGrupo = "INSERT INTO grupo (nome) VALUES (?)";
        String insertMembro = "INSERT INTO elementos_grupo (nome_grupo, email) VALUES (?, ?)";

        try {
            // Inserir
            try (PreparedStatement stmtGrupo = connection.prepareStatement(insertGrupo)) {
                stmtGrupo.setString(1, nomeGrupo);
                stmtGrupo.executeUpdate();
                setLastQuery(insertGrupo);
            }
            // Associar
            try (PreparedStatement stmtMembro = connection.prepareStatement(insertMembro)) {
                stmtMembro.setString(1, nomeGrupo);
                stmtMembro.setString(2, criadorEmail);
                stmtMembro.executeUpdate();
                setLastQuery(insertMembro);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int adicionaDespesa(String email, String grupo, String valor, String data, String email_pagador,String descricao){

        String getnovoID = "SELECT MAX(id) FROM despesa";
        String query = "INSERT INTO despesa (id, data, valor, descricao, nome_grupo, email_criador, email_pagador) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try{
            PreparedStatement st = connection.prepareStatement(getnovoID);
            ResultSet rs = st.executeQuery();

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, rs.getInt(1)+1);
            stmt.setString(2, data);      stmt.setString(3, valor);
            stmt.setString(4, descricao); stmt.setString(5, grupo);
            stmt.setString(6, email);     stmt.setString(7, email_pagador);

            stmt.executeUpdate();
            incDbVersion();
            setLastQuery(query);

            return rs.getInt(1)+1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Falha
        }
    }

    public boolean adicionaDespesaPartilhada(int id,String email_partilha){
        String query = "INSERT INTO despesa_partilhada (id_despesa, email) VALUES (?, ?)";

        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.setString(2, email_partilha);

            stmt.executeUpdate();
            incDbVersion();
            setLastQuery(query);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean adicionaPagamento(String quemPagou, String quemRecebeu, String data, String valor, String grupo) {
        String query = "INSERT INTO pagamento (email_pagador, email_recetor, data, valor,nome_grupo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, quemPagou);
            stmt.setString(2, quemRecebeu);
            stmt.setString(3, data);
            stmt.setString(4, valor);
            stmt.setString(5, grupo);
            stmt.executeUpdate();

            setLastQuery(query);
            incDbVersion();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alteraCampoPerfil(String email, String campo,String novaPassword) { // email, nome, telefone, password
        String query = "UPDATE utilizador SET "+ campo +" = ? WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, novaPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
            setLastQuery(query);
            incDbVersion();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Nao funfa :(
     * @param novoNome
     * @param oldNome
     * @return
     */
    public boolean alteraNomeGrupo(String novoNome,String oldNome) { //nao funfa
        String query = "UPDATE grupo SET nome = ? WHERE nome = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, novoNome);
            stmt.setString(2, oldNome);
            stmt.executeLargeUpdate();
            setLastQuery(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alteraCampoDespesa(String id, String campo, String data){ //data,descricao,valor,email_pagador
        String query = "UPDATE despesa SET "+ campo +" = ? WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, data);
            stmt.setString(2, id);
            stmt.executeUpdate();

            incDbVersion();
            setLastQuery(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    /**
     * Elimina uma despesa da Base de Dados
     * comeca por eliminar as suas dependencias (despesa_partilhada)
     * e depois elimina da tabela despesa
     * @param id
     * @return
     */
    public boolean eliminarDespesa(String id){
        String dependencias = "DELETE FROM despesa_partilhada WHERE id_despesa = ? ";
        String despesa = "DELETE FROM despesa WHERE id = ?";
        try{
            PreparedStatement stmt1 = connection.prepareStatement(dependencias);
            stmt1.setString(1, id);
            stmt1.executeUpdate();
            setLastQuery(dependencias);

            PreparedStatement stmt2 = connection.prepareStatement(despesa);
            stmt2.setString(1, id);
            stmt2.executeUpdate();
            setLastQuery(despesa);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina um pagamento da Base de Dados
     * @param id
     * @return
     */
    public boolean eliminarPagamento(String id){
        String query1 = "DELETE FROM pagamento WHERE id = ? ";

        try (PreparedStatement stmt1 = connection.prepareStatement(query1)) {
            stmt1.setString(1, id);
            stmt1.executeUpdate();
            setLastQuery(query1);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarGrupodaDB(String grupo){
        String eliminarPagamentos = "DELETE FROM pagamento WHERE nome_grupo = ?";
        String eliminarElementos = "DElETE FROM elementos_grupo WHERE nome_grupo = ?";
        String eliminarGrupo = "DELETE FROM grupo WHERE nome = ?";


        try{
            PreparedStatement pagamentos = connection.prepareStatement(eliminarPagamentos);
            pagamentos.setString(1, grupo);
            pagamentos.executeUpdate();
            setLastQuery(eliminarPagamentos);
            incDbVersion();

            PreparedStatement elementos = connection.prepareStatement(eliminarElementos);
            elementos.setString(1, grupo);
            elementos.executeUpdate();
            setLastQuery(eliminarElementos);
            incDbVersion();

            PreparedStatement ogrupo = connection.prepareStatement(eliminarGrupo);
            ogrupo.setString(1, grupo);
            ogrupo.executeUpdate();
            setLastQuery(eliminarGrupo);
            incDbVersion();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean retiraEmailGrupo(String email, String grupo){
        String query  = "DELETE FROM elementos_grupo WHERE email = ? AND nome_grupo = ?";

        try (PreparedStatement stmt1 = connection.prepareStatement(query)) {
            stmt1.setString(1, email);
            stmt1.setString(2, grupo);
            stmt1.executeUpdate();

            setLastQuery(query);
            incDbVersion();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//Getters-----------------------------------------------------------------------------------------------------------------------

    public ArrayList<String> listaDespesas(String grupo){

        String query = "SELECT * FROM despesa WHERE nome_grupo = ?";
        String query_partilhados = "SELECT email FROM despesa_partilhada WHERE id_despesa = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            PreparedStatement stmt_partilhados = connection.prepareStatement(query_partilhados);
            stmt.setString(1,grupo);

            ArrayList<String> list = new ArrayList<>();
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                StringBuilder partilhados = new StringBuilder();
                stmt_partilhados.setInt(1,rs.getInt("id"));

                ResultSet rs_partilhados = stmt_partilhados.executeQuery();
                while(rs_partilhados.next()){
                    partilhados.append(rs_partilhados.getString(1)+" ");
                }

                list.add("\n - " + rs.getInt(1) + ", Data: "+ rs.getString(2) + ", Valor: "+rs.getFloat(3)+
                         "\n    Quem Inseriu: "+rs.getString(6)+ ", Quem Pagou: "+rs.getString(7)+
                         "\n    Partilhado com: " + partilhados +
                         "\n    Descricao: " + rs.getString(4));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String listaPagamentos(String grupo){

        String query = "SELECT * FROM pagamento WHERE nome_grupo = ?";

        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,grupo);

            StringBuilder list = new StringBuilder();
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                list.append("\n - ").append(rs.getInt(1)).append(", Data: ").append(rs.getString(2)).append(", Valor: ").append(rs.getFloat(3))
                                    .append("\n    Quem Recebeu: ").append(rs.getString(5)).append(", Quem Pagou: ").append(rs.getString(6));
            }
            return list.toString();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String listaGrupos(String email){
        String query = "SELECT nome_grupo FROM elementos_grupo WHERE email = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,email);

            ResultSet rs = stmt.executeQuery();
            StringBuilder result = new StringBuilder();
            while(rs.next()){
                result.append("\n - " + rs.getString(1));
            }
            return result.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public float somaDespesas(String grupo){
        String query = "SELECT SUM(valor) FROM despesa WHERE nome_grupo = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,grupo);

            ResultSet rs = stmt.executeQuery();
            rs.getFloat(1);
            return rs.getFloat(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
     * Verifica se a pass corresponde ao utilizador existe
     * com "SELECT EXISTS (select 1 FROM utilizador WHERE email = ? AND password = ?)"
     * A query devolve 1 se existir e 0 se nao existir
     * @return true, existe;  false, nao existe
     */
    public Boolean verificaPassword(String email, String password){

        String query = "SELECT EXISTS (select 1 FROM utilizador WHERE email = ? AND password = ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,email);
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
    public Boolean verificaPertenceGrupo(String email, String grupo){
        String query = "SELECT EXISTS (select 1 FROM elementos_grupo WHERE email = ? AND nome_grupo = ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,email); //troca o primeiro '?' pelo email
            stmt.setString(2,grupo);

            ResultSet rs = stmt.executeQuery();
            return rs.getBoolean(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica se o id (despesa ou pagamento) existe
     * @return true, existe
     * @return false, nao existe
     */
    public Boolean verificaId(String id, String entidade) {

        String query = "SELECT EXISTS (select 1 FROM " + entidade + " WHERE id = ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1,id);

            ResultSet rs = stmt.executeQuery();
            return rs.getBoolean(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica se o grupo existe na BD
     * @return true, existe
     * @return false, nao existe
     */
    public boolean verificaGrupo(String nomeGrupo) {
        String query = "SELECT EXISTS (select 1 FROM grupo WHERE nome = ?)"; //nomo do hrupo é uma chave unica
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nomeGrupo);
            ResultSet rs = stmt.executeQuery();

            return rs.getBoolean(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica se a pessoa x tem alguma depesa associada no grupo
     * @return true, existe
     * @return false, nao existe
     */
    public boolean verificaDespesaPessoaGrupo(String email, String grupo){
        String query = "SELECT EXISTS (select 1 FROM despesa WHERE nome_grupo = ? AND email_pagador = ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, grupo);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();

            return rs.getBoolean(1);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
