package main.java.org.ufu.ds.election;

import java.io.Serializable;

public class HostInfo implements Serializable {

    private int id;

    private String hostName;

    private Integer port;

    private Role role;


    public HostInfo(int id, String hostName, int port, Role role) {
        this.id = id;
        this.hostName = hostName;
        this.port = port;
        this.role = role;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "HostInfo{" +
                "id=" + id +
                ", hostName='" + hostName + '\'' +
                ", port=" + port +
                ", role=" + role +
                '}';
    }
}
