package main.java.org.ufu.ds;

import main.java.org.ufu.ds.election.HostInfo;
import main.java.org.ufu.ds.election.Role;
import main.java.org.ufu.ds.heartbeat.HeartBeat;
import main.java.org.ufu.ds.heartbeat.HeartBeatFactory;
import main.java.org.ufu.ds.heartbeat.HeartBeatReceiver;
import main.java.org.ufu.ds.heartbeat.Helper;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args[0] == null) {
            System.out.println("Should designate the Role of the server in parameters");
            throw new RuntimeException("Should designate the Role of the server in parameters");
        }

        Role role = Role.valueOf(args[0]);
        List<HostInfo> hostInfos = Helper.loadNodeProperties();

        System.out.println("STARTED SERVER AS ROLE = " + role);

        if (role.equals(Role.FOLLOWER)) {
            HeartBeatReceiver heartBeat = (HeartBeatReceiver) HeartBeatFactory.designateHeartBeat(role, hostInfos);
            SocketMessagesHandler socketMessagesHandler = new SocketMessagesHandler(heartBeat);
            socketMessagesHandler.handle();
            heartBeat.startHeartBeat();
        } else {
            HeartBeat heartBeat = HeartBeatFactory.designateHeartBeat(role, hostInfos);
            heartBeat.startHeartBeat();
        }

    }


}