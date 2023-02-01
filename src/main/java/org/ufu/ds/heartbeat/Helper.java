package main.java.org.ufu.ds.heartbeat;

import main.java.org.ufu.ds.Main;
import main.java.org.ufu.ds.election.HostInfo;
import main.java.org.ufu.ds.election.Role;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Helper {

    public static List<HostInfo> loadNodeProperties() {

        Properties properties = new Properties();
        List<HostInfo> hostInfoList = new ArrayList<>();

        try {
            properties.load(new FileInputStream("main/java/org/ufu/ds/nodes.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Integer> generated = new HashSet<>();

        for (int hostIndex = 1; hostIndex <= properties.size() / 3; hostIndex++) {
            String keyStringHost = "node" + hostIndex + ".host";
            String keyStringPort = "node" + hostIndex + ".port";
            String keyRole = "node" + hostIndex + ".role";

            String hostname = properties.getProperty(keyStringHost);
            int port = Integer.parseInt(properties.getProperty(keyStringPort));
            Role role = Role.valueOf(properties.getProperty(keyRole));

            int sortedProccessId = 0;

            String localhost = null;
            try {
                localhost = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            if (hostname.equals(localhost)) {
                role = Role.COORDINATOR;
            }

            if (Main.role.equals(Role.COORDINATOR)) {
                sortedProccessId = getSortedProccessId(properties, generated);
            }

            HostInfo hostInfo = new HostInfo(sortedProccessId, hostname, port, role);
            hostInfoList.add(hostInfo);

        }
        return hostInfoList;
    }

    private static int getSortedProccessId(Properties properties, Set<Integer> generated) {
        int randomInt = ThreadLocalRandom.current().nextInt(1, properties.size() + 1);

        while (generated.contains(randomInt)) {
            randomInt = ThreadLocalRandom.current().nextInt(1, properties.size() + 1);
        }
        generated.add(randomInt);

        return randomInt;
    }

    public static HostInfo getThisHostInfo() {


        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        String hostName = inetAddress.getHostAddress();

        Optional<HostInfo> optionalHostInfo = Main.hostInfoList.stream().filter(host -> host.getHostName().equals(hostName)).findFirst();

        if (optionalHostInfo.isPresent()) {
            return optionalHostInfo.get();
        }

        System.out.println("HOSTNAME=" + hostName);
        Main.hostInfoList.forEach(System.out::println);

        throw new RuntimeException("ERROR IN GET THIS HOST INFO !");

    }

    public static byte[] convertToBytes(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static Object convertFromBytes(byte[] bytes) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object obj = objectInputStream.readObject();
            objectInputStream.close();
            return obj;
        } catch (Exception e) {
            System.out.println("Error in convertFromBytes, e=" + e);
        }
        return null;
    }

}
