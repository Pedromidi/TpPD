package pt.isec.pd.tp.Cliente;

import pt.isec.pd.tp.MSG;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

//TODO - Tirar quando não for necessário
import java.util.Calendar;
import java.util.Objects;
import java.util.Scanner;

public class Cliente {

    private String nome;
    private String email;
    private String telefone;
    private String password;

    public Cliente (String nome, String email, String telefone, String password) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.password = password;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Utilizador{" + "nome='" + nome + '\'' +
                ", email='" + email + '\'' + ", telefone='";
    }

    public Boolean enviaComando(String comando){
        //TODO enviar string ao servidor, servidor verifica e devolve resposta
        return true;
    }
}
