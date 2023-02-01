package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.*;
import main.java.org.ufu.ds.election.HostInfo;
import main.java.org.ufu.ds.election.Role;

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

            HostInfo actualNode = Helper.getThisHostInfo();

            //System.out.println("actualNode=" + actualNode);

            List<HostInfo> nodeToParticipateOfElection = Main.hostInfoList
                    .stream()
                    .filter(hostInfo -> hostInfo.getId() > actualNode.getId()
                            && !hostInfo.getRole().equals(Role.COORDINATOR))
                    .collect(Collectors.toList());


            //System.out.println("Nodes to participate of election:" + nodeToParticipateOfElection);


            if (nodeToParticipateOfElection.isEmpty()) {
                findOldCoordinatorAndRemoveIt();
                System.out.println("No nodes have higher identifier than me turning into COORDINATOR");
                actualNode.setRole(Role.COORDINATOR);
                new CoordinatorMessageSender(Main.hostInfoList);
                heartBeatFactory.designateHeartBeat(Role.COORDINATOR).startHeartBeat();
            } else {

                List<String> nodesAddresses = nodeToParticipateOfElection.stream().map(HostInfo::getHostName).collect(Collectors.toList());
                ElectionMessage electionMessage = new ElectionMessage(Constants.ELECTION);
                new MulticastSender().multicast(electionMessage, nodesAddresses);
            }

        }).start();
    }

    private void findOldCoordinatorAndRemoveIt() {

        boolean removed = Main.hostInfoList.removeIf(hostInfo -> Role.COORDINATOR.equals(hostInfo.getRole()) && hostInfo != Helper.getThisHostInfo());

        //if (removed)
            //System.out.println("OLD COORDINATOR REMOVED");


    }
}
