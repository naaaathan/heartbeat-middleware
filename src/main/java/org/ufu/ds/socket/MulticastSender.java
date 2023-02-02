package main.java.org.ufu.ds.socket;

import main.java.org.ufu.ds.election.Message;
import main.java.org.ufu.ds.heartbeat.Helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class MulticastSender {

    private DatagramSocket socket;


    public MulticastSender() {
        try {
            socket = new DatagramSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void multicast(Message multicastMessage, List<String> nodeAddresses) {
        try {

            byte[] buf = Helper.convertToBytes(multicastMessage);

            for (String address : nodeAddresses) {
                InetAddress inetAddress = InetAddress.getByName(address);
                if (checkIfItsReachable(inetAddress)) {
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

    public boolean multicastWithResponseWait(Message multicastMessage, List<String> nodeAddresses) {

        Map<InetAddress, Boolean> responseMap = new ConcurrentHashMap<>();
        AtomicBoolean receivedResponse = new AtomicBoolean(false);

        try {
            byte[] buf = Helper.convertToBytes(multicastMessage);
            byte[] responseBuf = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuf, responseBuf.length);

            for (String address : nodeAddresses) {
                InetAddress inetAddress = InetAddress.getByName(address);
                responseMap.put(inetAddress, false);
            }

            List<Thread> threads = new ArrayList<>();
            for (Map.Entry<InetAddress, Boolean> entry : responseMap.entrySet()) {
                InetAddress inetAddress = entry.getKey();
                Thread t = new Thread(() -> {
                    try {
                        if (checkIfItsReachable(inetAddress)) {
                            DatagramPacket packet = new DatagramPacket(buf, buf.length, inetAddress, 4446);
                            System.out.println("Sending " + multicastMessage + " multicast message to address=" + inetAddress.getHostAddress());
                            socket.send(packet);

                            socket.setSoTimeout(1000);  // Set a timeout of 1 second
                            socket.receive(responsePacket);  // Wait for a response
                            InetAddress receivedAddress = responsePacket.getAddress();
                            System.out.println("Received response from " + receivedAddress);
                            responseMap.put(receivedAddress, true);
                            receivedResponse.set(true);
                        }
                    } catch (SocketTimeoutException e) {
                        System.out.println("didn't receive response...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                threads.add(t);
                t.start();
            }

            for (Thread t : threads) {
                t.join();
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return receivedResponse.get();


    }

    private boolean checkIfItsReachable(InetAddress inetAddress) throws IOException {
        return inetAddress.isReachable(1000);
    }
}
