package main.java.org.ufu.ds.election;

import main.java.org.ufu.ds.socket.MulticastSender;
import main.java.org.ufu.ds.util.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoordinatorMessageSender {

    public CoordinatorMessageSender(List<HostInfo> hostInfoList) {

        new Thread(() -> sendCoordinatorMessage(hostInfoList)).start();
    }

    private void sendCoordinatorMessage(List<HostInfo> hostInfoList) {

        List<HostInfo> followersNode = hostInfoList.stream().filter(hostInfo -> !Objects.equals(hostInfo.getHostName(), thisHost())).collect(Collectors.toList());

        List<String> nodesHostname = followersNode.stream().map(HostInfo::getHostName).collect(Collectors.toList());

        CoordinatorMessage coordinatorMessage = new CoordinatorMessage(Constants.COORDINATOR, hostInfoList);

        new MulticastSender().multicast(coordinatorMessage, nodesHostname);

    }


    private String thisHost() {

        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("ERROR=" + e);
        }
        return null;
    }
}
