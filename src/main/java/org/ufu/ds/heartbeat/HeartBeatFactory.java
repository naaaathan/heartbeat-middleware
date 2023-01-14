package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.election.Role;

public class HeartBeatFactory {


    public static HeartBeat designateHeartBeat(Role role, Long heartBeatIntervalInMs) {

        switch (role) {

            case LEADER:
                return new HeartBeatSender(heartBeatIntervalInMs);
            case FOLLOWER:
                return new HeartBeatReceiver(heartBeatIntervalInMs, 80);

        }

        return null;
    }


}
