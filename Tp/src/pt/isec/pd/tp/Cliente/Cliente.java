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


    public static final String TIME_REQUEST = "TIME";
    public static final int TIMEOUT = 10;

    public Boolean enviaComando(String comando){
        //TODO enviar string ao servidor, servidor verifica e devolve responta
        return true;
    }



    public static void main(String[] args) {
        InetAddress serverAddr;
        int serverPort;
        Calendar response;

        if (args.length != 2) {
            System.out.println("Sintaxe: java Client serverAddress serverPort");
            return;
        }

        try {
            serverAddr = InetAddress.getByName(args[0]);
            serverPort = Integer.parseInt(args[1]);

            try (Socket socket = new Socket(serverAddr, serverPort)) {
                socket.setSoTimeout(TIMEOUT * 1000);

                // a)
                // [PT] Serializar o objecto do tipo String TIME_REQUEST para o OutputStream disponibilizado pelo socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject("Time");

                // b)
                // [PT] Deserializa o objecto do tipo Calendar recebido no InputStream disponibilizado pelo socket
                ObjectInputStream in =  new ObjectInputStream(socket.getInputStream());
                response = (Calendar) in.readObject();

                System.out.println("Resposta: " + response.getTime());

                Scanner input = new Scanner(System.in);
                int opcao;
                do {
                    System.out.println("Escolha uma opção:");
                    System.out.println("1. Autenticação");
                    System.out.print("2. Registo\n->");

                    opcao = input.nextInt();

                    if (opcao != 1 && opcao != 2) {
                        System.out.println("Opcao invalida. Por favor escolha 1 ou 2");
                    }
                    input.nextLine();

                } while (opcao != 1 && opcao != 2);

                System.out.println("Nome: ");
                String nome = input.nextLine();
                System.out.println("Password: ");
                String pass = input.nextLine();
                System.out.println(nome + ", " + pass);

                if(opcao == 1) {
                    //TODO - enviar ao server como login
                }
                else{
                    //TODO - enviar ao server como register
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Destino desconhecido:\n\t" + e);
        } catch (NumberFormatException e) {
            System.out.println("O porto do servidor deve ser um inteiro positivo.");
        } catch (SocketTimeoutException e) {
            System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
        } catch(ClassNotFoundException e){
            System.out.println("O objecto recebido não é do tipo esperado:\n\t"+e);
        }
    }
}
