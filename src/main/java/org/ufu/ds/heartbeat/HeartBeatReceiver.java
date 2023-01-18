package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.Constants;
import main.java.org.ufu.ds.election.HostInfo;
import main.java.org.ufu.ds.election.Role;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HeartBeatReceiver extends HeartBeat {

    private final List<HostInfo> hostInfo;

    private Long lastHeartBeatReceived = System.currentTimeMillis();

    AtomicBoolean recevingHeartBeat = new AtomicBoolean(true);

    public HeartBeatReceiver(List<HostInfo> hostInfoList) {
        this.timeBetweenHeartbeats = Constants.BEAT_INTERVAL;
        this.hostInfo = hostInfoList;
    }

    @Override
    public void startHeartBeat() {

        new Thread(() -> {

            try {

                while (recevingHeartBeat.get()) {

                    long timeNow = System.currentTimeMillis();
                    long timeSpent = timeNow - lastHeartBeatReceived;


                    if (timeSpent > timeBetweenHeartbeats + 100L) {
                        System.out.println("time spent=" + timeSpent);
                        promoteElection();
                        recevingHeartBeat.set(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    public void receiveHeartBeat() {
        System.out.println("Received HeartBeat");
        this.lastHeartBeatReceived = System.currentTimeMillis();
    }


    private void promoteElection() throws UnknownHostException {

        System.out.println("promoteElection begin");

        HostInfo hostSendingElection = Helper.getThisHostInfo(hostInfo);

        if (hostSendingElection == null) {
            throw new RuntimeException("could not find host in known hosts");
        }
        //Promote Election here
    }

    public void stop() {

        recevingHeartBeat.set(false);
        Helper.getThisHostInfo(hostInfo).setRole(Role.SEARCHING_LEADER);
    }
}
