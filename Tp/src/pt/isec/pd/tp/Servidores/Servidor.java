package pt.isec.pd.tp.Servidores;

import pt.isec.pd.tp.Despesa;
import pt.isec.pd.tp.Grupo;

import java.io.*;
import java.net.*;

import static java.lang.System.exit;

public class Servidor {
    String nomeFicheiro = "test";

    public static void main(String[] args) {
        ServerSocket serverSocket;
        int listeningPort;

        if (args.length != 3) {
            System.out.println("Sintaxe: java Servidor listeningPort bdAdress dbName");
            return;
        }

        try {
            DbManager manager =  new DbManager(args[1], args[2]);
            String result = manager.connect();
            System.out.println(result);

            if(!result.contains("estabelecida!")){
                System.out.println("Não foi possivel estabelecer ligação a uma Base de Dados\n-----ABORTAR----- ");
                exit(1);
            }

            //Popular as variáveis com os valores dos args
            listeningPort = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(listeningPort);

            ServerSocket serverSocketBackup = new ServerSocket(0);
            int port = serverSocketBackup.getLocalPort(); //Port TCP para conexões do servidor backup dado pelo socket automatico
            System.out.println("Porto automatico criado: " + port);

            File dbFile = new File(args[1] + File.separator + args[2]);
            AceitaBackup aceitaBackup = new AceitaBackup(serverSocketBackup, dbFile);
            Thread abThread = new Thread(aceitaBackup);
            abThread.start();

            Heartbeat heartbeat = new Heartbeat(port, manager);
            Thread hbThread = new Thread(heartbeat);
            hbThread.start();

            System.out.println("TCP Message Server iniciado...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Criar e iniciar a thread que vai processar/atender cada cliente
                Runnable clientThread = new AtendeCliente(clientSocket,manager);
                Thread t = new Thread(clientThread);
                t.start();
            }
        } catch (NumberFormatException e) {
            System.out.println("O porto de escuta deve ser um inteiro positivo.");
        } catch (SocketException e) {
            System.out.println("Ocorreu um erro ao nivel do serverSocket TCP:\n\t" + e);
        } catch (IOException e) {
            System.out.println("Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
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
