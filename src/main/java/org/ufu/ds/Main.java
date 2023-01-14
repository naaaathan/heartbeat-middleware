package main.java.org.ufu.ds;

import main.java.org.ufu.ds.election.Role;
import main.java.org.ufu.ds.heartbeat.HeartBeat;
import main.java.org.ufu.ds.heartbeat.HeartBeatFactory;

public class Main {
    public static void main(String[] args) {
        if (args[0] == null) {
            System.out.println("Should designate the Role of the server in parameters");
            throw new RuntimeException("Should designate the Role of the server in parameters");
        }


        Role role = Role.valueOf(args[0]);

        System.out.println("STARTED SERVER AS ROLE = " + role);

        Long intervalBetweenHeartBeats = 1500L;

        HeartBeat heartBeat = HeartBeatFactory.designateHeartBeat(role, intervalBetweenHeartBeats);

        heartBeat.startHeartBeat();


    }
}