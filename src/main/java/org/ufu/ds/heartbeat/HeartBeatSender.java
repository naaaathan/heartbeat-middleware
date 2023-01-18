package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.Constants;
import main.java.org.ufu.ds.election.HostInfo;
import main.java.org.ufu.ds.schedule.ScheduledRun;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HeartBeatSender extends HeartBeat {

    private SocketChannel socketChannel;

    private final List<HostInfo> hostInfoList;

    public HeartBeatSender(List<HostInfo> hostInfoList) {
        this.timeBetweenHeartbeats = Constants.BEAT_INTERVAL;
        this.hostInfoList = hostInfoList;

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

                    for (HostInfo hostInfo : hostInfoList) {

                        socketChannel = SocketChannel.open();

                        String hostname = hostInfo.getHostName();
                        Integer port = hostInfo.getPort();

                        System.out.printf("Sending heartbeat for hostname " + hostname + " and port = " + port + "\n");

                        socketChannel.connect(new InetSocketAddress(hostname, port));

                        ByteBuffer buffer = ByteBuffer.wrap("Heartbeat".getBytes());
                        socketChannel.write(buffer);

                        socketChannel.close();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

    }

}
