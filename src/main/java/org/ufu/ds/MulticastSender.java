package main.java.org.ufu.ds;

import main.java.org.ufu.ds.heartbeat.Helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

public class MulticastSender {

    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;


    public MulticastSender() {
        try {
            socket = new DatagramSocket();
            group = InetAddress.getByName("230.0.0.0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void multicast(Message multicastMessage, List<String> nodeAddresses) {
        try {

            buf = Helper.convertToBytes(multicastMessage);

            for (String address : nodeAddresses) {
                InetAddress inetAddress = InetAddress.getByName(address);
                if (inetAddress.isReachable(1000)) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, inetAddress, 4446);
                    System.out.println("Sending " + multicastMessage + " multicast message to address=" + inetAddress.getHostAddress());
                    socket.send(packet);
                }
            }
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
