package test.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.heartbeat.HeartBeatSender;


public class HeartBeatTest {

    public static void main(String[] args) {
        HeartBeatSender heartBeatSender = new HeartBeatSender(1000L);
        heartBeatSender.startHeartBeat();
    }
}
