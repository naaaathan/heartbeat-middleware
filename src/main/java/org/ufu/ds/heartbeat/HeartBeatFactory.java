package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.Main;
import main.java.org.ufu.ds.election.Role;

public class HeartBeatFactory {

    private HeartBeatSender heartBeatSender;

    private HeartBeatReceiver heartBeatReceiver;

    private NotHeartBeater notHeartBeater;

    public HeartBeatFactory() {
    }

    public HeartBeat designateHeartBeat(Role role) {

        switch (role) {

            case FOLLOWER_BEAT_SENDER:
                return getHeartBeatSender();
            case COORDINATOR:
                return getHeartBeatReceiver();
            default:
                return getNotHeartBeater();

        }
    }

    public HeartBeatSender getHeartBeatSender() {

        if (heartBeatSender != null && heartBeatSender.isTimerCanceled()) {
            heartBeatSender = new HeartBeatSender(Main.hostInfoList, this);
        }
        if (heartBeatSender == null) {
            heartBeatSender = new HeartBeatSender(Main.hostInfoList, this);
        }
        return heartBeatSender;
    }

    public void setHeartBeatSender(HeartBeatSender heartBeatSender) {
        this.heartBeatSender = heartBeatSender;
    }

    public HeartBeatReceiver getHeartBeatReceiver() {

        heartBeatReceiver = new HeartBeatReceiver();
        return heartBeatReceiver;
    }

    public void setHeartBeatReceiver(HeartBeatReceiver heartBeatReceiver) {
        this.heartBeatReceiver = heartBeatReceiver;
    }

    public NotHeartBeater getNotHeartBeater() {

        notHeartBeater = new NotHeartBeater();

        return notHeartBeater;
    }

    public void setNotHeartBeater(NotHeartBeater notHeartBeater) {
        this.notHeartBeater = notHeartBeater;
    }
}
