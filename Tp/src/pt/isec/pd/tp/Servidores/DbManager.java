package pt.isec.pd.tp.Servidores;

import javax.swing.*;
import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {
    String dbPath;
    String dbName;
    String dbAdress;

    public DbManager(String dbAdress, String dbName){
        this.dbAdress = dbAdress;
        this.dbName = dbName;
        //this.dbPath = "jdbc:sqlite:" + dbAdress + File.separator + dbName;
        this.dbPath = "jdbc:sqlite:" + dbAdress + "/" + dbName;

    }

    public void connect (){

        try (var conn = DriverManager.getConnection(dbPath)) {

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

    }

//Alterações--------------------------------------------------------------------------------------------------------------------


//Verificacões------------------------------------------------------------------------------------------------------------------
    /**
     * Verifica se o email existe
     * @return true, existe;  false, nao existe
     */
    public Boolean verificaEmail(String email){
        //TODO ir a tabela de utilizadores e procurar o email
        return true;
    }

    /**
     * Verifica se o telefone existe
     * @return true, existe;  false, não existe
     */
    public Boolean verificaTelefone(String telefone){
        //TODO ir a tabela de utilizadores e procurar o telefone
        return true;
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
