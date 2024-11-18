package pt.isec.pd.tp.Servidores;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Heartbeat implements Runnable {
    private int TCPPort;
    private int DbVersion;

    public Heartbeat( int port, int dbVersion) {
        this.TCPPort = port;
        this.DbVersion = dbVersion;
    }

    private void sendHeartbeat() {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress group = InetAddress.getByName("230.44.44.44");
            String heartbeatMessage = "porto TCP: " + TCPPort + ", versão base dados: " + DbVersion + ", ultima query: ";

            byte[] buffer = heartbeatMessage.getBytes();
            DatagramPacket pkt = new DatagramPacket(buffer, buffer.length, group, 4444);
            socket.send(pkt);

            System.out.println("Enviei heartbeat: " + heartbeatMessage);
        } catch (IOException e) {
            System.err.println("Erro a enviar heartbeat: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                sendHeartbeat();
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
