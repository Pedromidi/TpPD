package pt.isec.pd.tp.Servidores;

import java.io.*;
import java.net.*;

public class ServidorBackup {
    private static final String MULTICAST_ADDRESS = "230.44.44.44";
    private static final int PORT = 4444;
    public static int tcpPort;
    public static int dbVersion;
    public static int lastDbVersion;
    public static String lastQuery;
    public static boolean updated;
    public static File backupFile;


    public static void stethoscope(MulticastSocket socket) throws IOException {
        byte[] buffer = new byte[1024];
        socket.setSoTimeout(30000); //30 segundos à espera de heartbeat, sem heartbeat dá flatline, seems legit

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        String receivedMessage = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Recebi heartbeat: " + receivedMessage);

        String[] parts = receivedMessage.split("; ");
        String tcpport = parts[0].split(": ")[1];
        tcpPort = Integer.parseInt(tcpport);
        String dbversion = parts[1].split(": ")[1];
        dbVersion = Integer.parseInt(dbversion);
        lastQuery = parts[2].split(": ")[1];
        updated = Boolean.parseBoolean(parts[3].split(": ")[1]);

        /*System.out.println("TCPPort: " + tcpPort);
        System.out.println("DbVersion: " + dbVersion);
        System.out.println("LastQuery: " + lastQuery);
        System.out.println("Updated: " + updated);*/
    }

    public static boolean recebeBD(MulticastSocket socket, String backupDirPath) throws IOException{
//================================= Conexão TCP ao servidor principal =================================//
        try (Socket mainServerSocket = new Socket("localhost", tcpPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(mainServerSocket.getInputStream()));
             InputStream in = mainServerSocket.getInputStream()) {

            String fileName = reader.readLine();
            long fileSize = Long.parseLong(reader.readLine());
            System.out.println("Recebendo a base de dados: " + fileName + " (" + fileSize + " bytes)");

            backupFile = new File(backupDirPath + File.separator + fileName);

            try (FileOutputStream fout = new FileOutputStream(backupFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalRead = 0;

                while ((bytesRead = in.read(buffer)) != -1) {
                    fout.write(buffer, 0, bytesRead);
                    totalRead += bytesRead;
                    if (totalRead >= fileSize) {
                        break;
                    }
                }
                System.out.println("Ficheiro recebido e salvo em: " + backupFile.getAbsolutePath());
                lastDbVersion = dbVersion;
            }

        } catch (IOException e) {
            System.out.println("Erro na comunicação com o servidor principal: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean setupInicial(MulticastSocket socket, String backupDirPath) throws IOException {
        stethoscope(socket);
        if(recebeBD(socket, backupDirPath)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
//================================= Confirmar diretoria backup =================================//
        if (args.length != 1) {
            System.out.println("Sintaxe: java ServidorBackup backupDirectoryPath");
            return;
        }

        String backupDirPath = args[0];
        File backupDir = new File(backupDirPath);

        if (!backupDir.exists()) {
            System.out.println("Diretoria de backup: " + args[0] + " inválida - não existe");
            return;
        }

        if (!backupDir.isDirectory()) {
            System.out.println("Diretoria de backup: " + args[0] + " inválida - não é uma diretoria");
            return;
        }

        String[] files = backupDir.list();
        if (files != null && files.length > 0) {
            System.out.println("Diretoria de backup: " + args[0] + " inválida - não está vazia");
            return;
        }

        System.out.println("Diretoria de backup: " + args[0] + " válida");

//================================= Heartbeat stuff =================================//

        try (MulticastSocket socket = new MulticastSocket(PORT)) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            System.out.println("Heartbeat esperado de " + MULTICAST_ADDRESS + ":" + PORT);

            if(!setupInicial(socket, backupDirPath)){
                System.out.println("Erro ao construir cópia da base de dados");
                return;
            }

            while (true) {
                stethoscope(socket);
                if(!updated){
                    if(dbVersion!=lastDbVersion) {
                        System.out.println("Versão da base de dados em conflito");
                        return;
                    }
                }
                else{
                    if(dbVersion!=lastDbVersion+1) {
                        System.out.println("Versão da base de dados em conflito");
                        return;
                    }
                    else{
                        if (backupFile.exists()) {
                            if (backupFile.delete()) {
                                if(!recebeBD(socket, backupDirPath)){
                                    System.out.println("Falha na cópia da nova versão da base de dados");
                                }
                            } else {
                                System.out.println("Falha na cópia da nova versão da base de dados");
                            }
                        } else {
                            System.out.println("Base de dados não existe"); //nunca deve chegar a este else
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

