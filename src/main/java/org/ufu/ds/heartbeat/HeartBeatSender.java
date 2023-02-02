package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.util.Constants;
import main.java.org.ufu.ds.election.HostInfo;
import main.java.org.ufu.ds.election.Role;
import main.java.org.ufu.ds.schedule.ScheduledRun;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;

public class HeartBeatSender extends HeartBeat {

    private SocketChannel socketChannel;

    private final List<HostInfo> hostInfoList;

    private final ScheduledRun scheduledRun;

    private final HeartBeatFactory heartBeatFactory;

    private boolean isTimerCanceled = false;

    public HeartBeatSender(List<HostInfo> hostInfoList, HeartBeatFactory heartBeatFactory) {
        this.timeBetweenHeartbeats = Constants.BEAT_INTERVAL;
        this.hostInfoList = hostInfoList;
        this.heartBeatFactory = heartBeatFactory;
        Timer timer = new Timer();
        TimerTask timerTask = createTimeTask();
        scheduledRun = new ScheduledRun(timer, timerTask, timeBetweenHeartbeats);


    }


    public void startHeartBeat() {

        try {
            scheduledRun.schedule();
        } catch (IllegalStateException e) {
            //ignoring because there is nothing to do
        }
    }

    public TimerTask createTimeTask() {

        final HostInfo[] coordinator = {null};

        return new TimerTask() {
            @Override
            public void run() {


                try {

                    Optional<HostInfo> optionalCoordinator = hostInfoList.stream().filter(hostInfo -> hostInfo.getRole().equals(Role.COORDINATOR)).findFirst();

                    if (optionalCoordinator.isPresent()) {

                        coordinator[0] = optionalCoordinator.get();

                        socketChannel = SocketChannel.open();

                        Selector selector = Selector.open();

                        String hostname = coordinator[0].getHostName();
                        Integer port = coordinator[0].getPort();

                        System.out.printf("Sending heartbeat for coordinator with hostname " + hostname + " and port = " + port + "\n");

                        validateIfHostIsReachable(hostname);
                        socketChannel.connect(new InetSocketAddress(hostname, port));

                        ByteBuffer buffer = ByteBuffer.wrap(Constants.HEARTBEAT.getBytes());
                        socketChannel.write(buffer);

                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);

                        long timeout = 3000L; // 3 seconds
                        if (selector.select(timeout) > 0) {
                            receiveResponseUnderTimeoutTime(selector);
                        } else {
                            notReceivedResponseStartAndElection("TIMEOUT - Didn't received response starting election");
                        }
                        socketChannel.close();
                        scheduledRun.removeFromTrackList(this);
                    }

                } catch (IOException e) {
                    notReceivedResponseStartAndElection("ERROR - Didn't received response starting election");
                }
            }
        };

    }

    private static void validateIfHostIsReachable(String hostname) throws IOException {
        if (!InetAddress.getByName(hostname).isReachable(3000)) {
            throw new NoRouteToHostException("No route to host");
        }
    }

    private void notReceivedResponseStartAndElection(String x) {
        scheduledRun.cancel();
        setTimerCanceled(true);
        System.out.println(x);
        new Election(heartBeatFactory);
    }

    private void receiveResponseUnderTimeoutTime(Selector selector) throws IOException {
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        for (SelectionKey key : selectedKeys) {
            if (key.isReadable()) {
                ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
                socketChannel.read(responseBuffer);
                String response = new String(responseBuffer.array()).trim();
                System.out.println("Received response from COORDINATOR =" + response);
            }
        }
        selectedKeys.clear();
    }

    public boolean isTimerCanceled() {
        return isTimerCanceled;
    }

    public void setTimerCanceled(boolean timeCanceled) {
        isTimerCanceled = timeCanceled;
    }

}
