package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.election.HostInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Helper {

    public static List<HostInfo> loadNodeProperties() {

        Properties properties = new Properties();
        List<HostInfo> hostInfoList = new ArrayList<>();

        try {
            properties.load(new FileInputStream("main/java/org/ufu/ds/nodes.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int hostIndex = 1; hostIndex <= properties.size() / 2; hostIndex++) {
            String keyStringHost = "node" + hostIndex + ".host";
            String keyStringPort = "node" + hostIndex + ".port";

            String hostname = properties.getProperty(keyStringHost);
            int port = Integer.parseInt(properties.getProperty(keyStringPort));

            hostInfoList.add(new HostInfo(hostIndex, hostname, port, null));

        }
        return hostInfoList;
    }

    public static HostInfo getThisHostInfo(List<HostInfo> hostInfos) {


        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String hostName = inetAddress.getHostAddress();

        return hostInfos.stream().filter(host -> host.getHostName().equals(hostName)).findFirst().orElse(null);

    }

}
