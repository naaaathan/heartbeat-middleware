package main.java.org.ufu.ds;

import main.java.org.ufu.ds.heartbeat.Election;
import main.java.org.ufu.ds.heartbeat.HeartBeatFactory;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class ElectionResponse {

    public ElectionResponse() {
    }

    public static DatagramPacket okResponse(InetAddress address, int port, HeartBeatFactory heartBeatFactory) {

        byte[] response = Constants.OK.getBytes();
        new Election(heartBeatFactory);

        return new DatagramPacket(response, response.length, address, port);

    }
}
