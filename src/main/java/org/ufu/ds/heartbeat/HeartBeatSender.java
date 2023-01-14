package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.schedule.ScheduledRun;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class HeartBeatSender extends HeartBeat {

    private SocketChannel socketChannel;

    private final Map<String, Integer> nodeHostAndPortMap;

    public HeartBeatSender(Long timeBetweenHeartbeats) {
        this.timeBetweenHeartbeats = timeBetweenHeartbeats;
        this.nodeHostAndPortMap = loadNodeProperties();
    }


    public void startHeartBeat() {
        Timer timer = new Timer();
        TimerTask timerTask = createTimeTask();
        ScheduledRun scheduledRun = new ScheduledRun(timer, timerTask, timeBetweenHeartbeats);
        scheduledRun.schedule();
    }

    public TimerTask createTimeTask() {

        return new TimerTask() {
            @Override
            public void run() {
                try {

                    for (Map.Entry<String, Integer> entry : nodeHostAndPortMap.entrySet()) {

                        socketChannel = SocketChannel.open();

                        String hostname = entry.getKey();
                        Integer port = entry.getValue();

                        socketChannel.connect(new InetSocketAddress(hostname, port));

                        ByteBuffer buffer = ByteBuffer.wrap("Heartbeat".getBytes());
                        socketChannel.write(buffer);

                        socketChannel.close();

                        System.out.printf("Sending heartbeat for hostname " + hostname + " and port = " + port + "\n");


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

    }


    public Map<String, Integer> loadNodeProperties() {

        Properties properties = new Properties();

        try {
            properties.load(Files.newInputStream(Paths.get("main/java/org/ufu/ds/nodes.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Integer> mapHostToPorts = new HashMap<>();

        for (int hostIndex = 1; hostIndex <= properties.size() / 2; hostIndex++) {
            String keyStringHost = "node" + hostIndex + ".host";
            String keyStringPort = "node" + hostIndex + ".port";

            System.out.println("put="+properties.getProperty(keyStringHost));

            mapHostToPorts.put(properties.getProperty(keyStringHost), Integer.valueOf(properties.getProperty(keyStringPort)));

        }

        return mapHostToPorts;

    }

}
