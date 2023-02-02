package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.util.Constants;

import java.nio.ByteBuffer;

public class HeartBeatReceiver extends HeartBeat {


    public HeartBeatReceiver() {
    }

    @Override
    public void startHeartBeat() {

        new Thread(() -> {

            try {

                while (true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public ByteBuffer receiveHeartBeat(String address) {
        System.out.println("Received HeartBeat from address = " + address);
        return ByteBuffer.wrap(Constants.OK.getBytes());
    }

}
