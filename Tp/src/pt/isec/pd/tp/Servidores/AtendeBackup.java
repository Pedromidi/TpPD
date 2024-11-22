package pt.isec.pd.tp.Servidores;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

class AceitaBackup implements Runnable {
    ServerSocket serverSocket;
    //int listeningPort;
    File databaseFile;

    public AceitaBackup(ServerSocket serverSocket, File databaseFile) {
        this.serverSocket = serverSocket;
        this.databaseFile = databaseFile;
    }

    @Override
    public void run() {
        try {
            //serverSocket = new ServerSocket(listeningPort);
            System.out.println("TCP Server Thread para enviar database iniciada");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Runnable clientThread = new AtendeBackup(clientSocket, databaseFile);
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
}

public class AtendeBackup implements Runnable {
    Socket clientSocket;
    File databaseFile;

    public AtendeBackup(Socket clientSocket, File databaseFile) {
        this.clientSocket = clientSocket;
        this.databaseFile = databaseFile;
    }

    @Override
    public void run() {
        try (OutputStream out = clientSocket.getOutputStream();
             FileInputStream fis = new FileInputStream(databaseFile)) {

            System.out.println("Iniciando envio da base de dados para " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

            // Enviar o nome e o tamanho do ficheiro primeiro
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(databaseFile.getName());
            writer.println(databaseFile.length());

            // Enviar o conteúdo do ficheiro
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            System.out.println("Envio da base de dados para " + clientSocket.getInetAddress().getHostAddress() + ": " + clientSocket.getPort());

        } catch (IOException e) {
            System.out.println("Erro ao enviar a base de dados para " + clientSocket.getInetAddress().getHostAddress() + ": " + clientSocket.getPort() + ": " + e);
        }
    }
}

