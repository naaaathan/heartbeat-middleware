package main.java.org.ufu.ds;

import main.java.org.ufu.ds.election.CoordinatorMessageSender;
import main.java.org.ufu.ds.election.HostInfo;
import main.java.org.ufu.ds.election.Role;
import main.java.org.ufu.ds.heartbeat.*;
import main.java.org.ufu.ds.socket.MulticastReceiver;
import main.java.org.ufu.ds.util.SocketMessagesHandler;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Role role = null;

    public static List<HostInfo> hostInfoList = new ArrayList<>();

    public static void main(String[] args) {
        if (args[0] == null) {
            System.out.println("Should designate the Role of the server in parameters");
            throw new RuntimeException("Should designate the Role of the server in parameters");
        }

        Role role = Role.valueOf(args[0]);
        Main.role = role;

        if (Role.COORDINATOR.equals(role)) {
            Main.hostInfoList = Helper.loadNodeProperties();
        }

        System.out.println("STARTED SERVER AS ROLE = " + role);

        HeartBeatFactory heartBeatFactory = new HeartBeatFactory();
        HeartBeatReceiver heartBeatReceiver = new HeartBeatReceiver();

        SocketMessagesHandler socketMessagesHandler = new SocketMessagesHandler(heartBeatReceiver);
        socketMessagesHandler.handle();

        if (Role.COORDINATOR.equals(role)) {
            new CoordinatorMessageSender(Main.hostInfoList);
        } else {
            new MulticastReceiver(heartBeatFactory).start();
        }

    }
}