package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.election.HostInfo;
import main.java.org.ufu.ds.election.Role;

import java.util.List;

public class HeartBeatFactory {


    public static HeartBeat designateHeartBeat(Role role, List<HostInfo> hostInfoList) {

        switch (role) {

            case LEADER:
                return new HeartBeatSender(hostInfoList);
            case FOLLOWER:
                return new HeartBeatReceiver(hostInfoList);

        }

        return null;
    }


}
