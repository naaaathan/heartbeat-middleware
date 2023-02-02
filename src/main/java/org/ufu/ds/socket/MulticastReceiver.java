package main.java.org.ufu.ds.socket;

import main.java.org.ufu.ds.Main;
import main.java.org.ufu.ds.election.CoordinatorMessage;
import main.java.org.ufu.ds.election.ElectionResponse;
import main.java.org.ufu.ds.election.Message;
import main.java.org.ufu.ds.heartbeat.Election;
import main.java.org.ufu.ds.heartbeat.HeartBeatFactory;
import main.java.org.ufu.ds.heartbeat.Helper;
import main.java.org.ufu.ds.util.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[4096];

    private final HeartBeatFactory heartBeatFactory;

    public MulticastReceiver(HeartBeatFactory heartBeatFactory) {
        this.heartBeatFactory = heartBeatFactory;
    }


    @Override
    public void run() {
        try {

            socket = new MulticastSocket(4446);

            try {

                while (true) {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    Message objectReceived = (Message) Helper.convertFromBytes(packet.getData());
                    System.out.println("received=" + objectReceived);
                    String topic = objectReceived.getTopic();

                    switch (topic) {
                        case Constants.COORDINATOR:
                            handleCoordinatorMessage((CoordinatorMessage) objectReceived);
                            break;
                        case Constants.ELECTION:
                            handleElectionMessage(packet);
                            break;
                        case Constants.OK:
                            handleOkMessage(packet);
                    }

                }
            } finally {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error in MulticastReceiver run =" + e);
        }
    }

    private void handleCoordinatorMessage(CoordinatorMessage objectReceived) {
        Main.hostInfoList = objectReceived.getHostInfoList();
        heartBeatFactory.designateHeartBeat(Helper.getThisHostInfo().getRole()).startHeartBeat();
    }

    private void handleElectionMessage(DatagramPacket packet) throws IOException {
        socket.send(ElectionResponse.okResponse(packet.getAddress(), packet.getPort(), heartBeatFactory));
        new Election(heartBeatFactory);
    }

    private void handleOkMessage(DatagramPacket packet) {
        System.out.println("Received OK from node = " + packet.getAddress());
    }
}
