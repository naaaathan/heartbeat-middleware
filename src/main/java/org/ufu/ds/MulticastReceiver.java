package main.java.org.ufu.ds;

import main.java.org.ufu.ds.heartbeat.Election;
import main.java.org.ufu.ds.heartbeat.HeartBeatFactory;
import main.java.org.ufu.ds.heartbeat.Helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[4096];

    protected InetAddress group = null;

    private final HeartBeatFactory heartBeatFactory;


    public MulticastReceiver(HeartBeatFactory heartBeatFactory) {
        this.heartBeatFactory = heartBeatFactory;
    }


    @Override
    public void run() {
        try {

            socket = new MulticastSocket(4446);
            InetAddress group = InetAddress.getByName("230.0.0.0");
            socket.joinGroup(group);

            try {

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    Message objectReceived = (Message) Helper.convertFromBytes(packet.getData());
                    System.out.println("received=" + objectReceived);
                    String topic = objectReceived.getTopic();

                    switch (topic) {
                        case Constants.COORDINATOR:
                            CoordinatorMessage cm = (CoordinatorMessage) objectReceived;
                            Main.hostInfoList = cm.getHostInfoList();
                            heartBeatFactory.designateHeartBeat(Helper.getThisHostInfo().getRole()).startHeartBeat();
                            break;
                        case Constants.ELECTION:

                            socket.send(ElectionResponse.okResponse(packet.getAddress(), packet.getPort(),heartBeatFactory));
                            new Election(heartBeatFactory);
                            break;
                        case Constants.OK:
                            System.out.println("Received OK from node = " + packet.getAddress());
                    }

                }
            } finally {
                socket.leaveGroup(group);
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("ERROR=" + e);
        }
    }
}
