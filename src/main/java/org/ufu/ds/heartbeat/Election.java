package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.*;
import main.java.org.ufu.ds.election.CoordinatorMessageSender;
import main.java.org.ufu.ds.election.ElectionMessage;
import main.java.org.ufu.ds.election.HostInfo;
import main.java.org.ufu.ds.election.Role;
import main.java.org.ufu.ds.socket.MulticastSender;
import main.java.org.ufu.ds.util.Constants;

import java.util.List;
import java.util.stream.Collectors;

public class Election {

    private final HeartBeatFactory heartBeatFactory;

    public Election(HeartBeatFactory heartBeatFactory) {
        this.heartBeatFactory = heartBeatFactory;
        startElection();
    }

    private void startElection() {
        new Thread(() -> {

            System.out.println("Starting election code...");

            HostInfo actualNode = Helper.getThisHostInfo();

            List<HostInfo> nodeToParticipateOfElection = Main.hostInfoList
                    .stream()
                    .filter(hostInfo -> hostInfo.getId() > actualNode.getId())
                    .collect(Collectors.toList());


            System.out.println("nodesToParticipate="+nodeToParticipateOfElection);


            if (nodeToParticipateOfElection.isEmpty()) {
                turnIntoCoordinator(actualNode);
            } else {

                List<String> nodesAddresses = nodeToParticipateOfElection.stream().map(HostInfo::getHostName).collect(Collectors.toList());
                ElectionMessage electionMessage = new ElectionMessage(Constants.ELECTION);
                boolean receivedAnyReponse = new MulticastSender().multicastWithResponseWait(electionMessage, nodesAddresses);

                if (!receivedAnyReponse) {
                    turnIntoCoordinator(actualNode);
                }

            }

        }).start();
    }

    private void turnIntoCoordinator(HostInfo actualNode) {
        findOldCoordinatorAndRemoveIt();
        System.out.println("No nodes have higher identifier than me or have sent a response...Turning into COORDINATOR");
        actualNode.setRole(Role.COORDINATOR);
        new CoordinatorMessageSender(Main.hostInfoList);
        heartBeatFactory.designateHeartBeat(Role.COORDINATOR).startHeartBeat();
    }

    private void findOldCoordinatorAndRemoveIt() {

        Main.hostInfoList.removeIf(hostInfo -> Role.COORDINATOR.equals(hostInfo.getRole()) && hostInfo != Helper.getThisHostInfo());

    }
}
