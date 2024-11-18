package pt.isec.pd.tp.Servidores;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ServidorBackup {
    private static final String MULTICAST_ADDRESS = "230.44.44.44";
    private static final int PORT = 4444;

    public static void main(String[] args) {
        try (MulticastSocket socket = new MulticastSocket(PORT)) {
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            System.out.println("Listening for heartbeats on " + MULTICAST_ADDRESS + ":" + PORT);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String receivedMessage = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received: " + receivedMessage);
            }
        } catch (IOException e) {
            System.err.println("Error in listener: " + e.getMessage());
        }
    }
}

