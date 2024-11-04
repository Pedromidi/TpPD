package pt.isec.pd.tp.Servidores;

import pt.isec.pd.tp.Despesa;
import pt.isec.pd.tp.Grupo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Calendar;

public class Servidor {
    public static final String TIME_REQUEST = "TIME";
    String nomeFicheiro = "test";

    //100% theory, test it at your own peril
    public void exportarDespesasParaCSV(Grupo grupo, String nomeFicheiro){
        try(FileWriter writer = new FileWriter(nomeFicheiro)){
            writer.append("Data, Responsavel, Valor, Pago por, A dividir com");
            for (Despesa despesa : grupo.getDespesas()){
                writer.append(despesa.getData() + ",");
                writer.append(despesa.getPagador()+ ",");
                writer.append(despesa.getValor()+ ",");
                writer.append(despesa.getPagador() + ",");
                writer.append(despesa.getDivididaCom()+",");
            } System.out.println("Despesas exportadas com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        int listeningPort;
        String receivedMsg;

        if (args.length != 1) {
            System.out.println("Sintaxe: java Server listeningPort");
            return;
        }


        listeningPort = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {

            System.out.println("TCP Message Server iniciado...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    // a)
                    // [PT] Deserializar o objecto do tipo String recebido no InputStream disponibilizado pelo socket

                    ObjectInputStream in =  new ObjectInputStream(clientSocket.getInputStream());
                    receivedMsg = (String) in.readObject();

                    System.out.println("Recebido \"" + receivedMsg + "\" de " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                    /*if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST)) {
                        continue;
                    }*/

                    // b)
                    // [PT] Serializar o objecto do tipo Calendar para o OutputStream disponibilizado pelo socket
                    // [EN] Serialize the object of type Calendar to the OutputStream available on the socket
                    ObjectOutputStream out =  new ObjectOutputStream(clientSocket.getOutputStream());
                    String confirm =  "Resposta do servidor";

                    out.writeObject(confirm);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do serverSocket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
        } catch(ClassNotFoundException e){
            System.out.println("O objecto recebido não é do tipo esperado:\n\t"+e);
        }
    }

    //main original
    /*public static void main(String[] args) throws UnknownHostException {
        int listeningPort;
        String receivedMsg;

        if (args.length != 1) {
            System.out.println("Sintaxe: java Server listeningPort");
            return;
        }


        listeningPort = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {

            System.out.println("UDP Time Server iniciado...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    // a)
                    // [PT] Deserializar o objecto do tipo String recebido no InputStream disponibilizado pelo socket

                    ObjectInputStream in =  new ObjectInputStream(clientSocket.getInputStream());
                    receivedMsg = (String) in.readObject();

                    System.out.println("Recebido \"" + receivedMsg + "\" de " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                    if (!receivedMsg.equalsIgnoreCase(TIME_REQUEST)) {
                        continue;
                    }

                    // b)
                    // [PT] Serializar o objecto do tipo Calendar para o OutputStream disponibilizado pelo socket
                    // [EN] Serialize the object of type Calendar to the OutputStream available on the socket
                    ObjectOutputStream out =  new ObjectOutputStream(clientSocket.getOutputStream());
                    Calendar calendar =  Calendar.getInstance();

                    out.writeObject(calendar);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do serverSocket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
        } catch(ClassNotFoundException e){
            System.out.println("O objecto recebido não é do tipo esperado:\n\t"+e);
        }
    }*/
}
