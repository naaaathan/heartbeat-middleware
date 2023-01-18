package main.java.org.ufu.ds.election;

public class HostInfo {

    private int id;

    private String hostName;

    private Integer port;

    private Long processPid;

    private Role role;


    public HostInfo(int id, String hostName, int port, Long processPid) {
        this.id = id;
        this.hostName = hostName;
        this.port = port;
        this.processPid = processPid;
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

    public Long getProcessPid() {
        return processPid;
    }

    public void setProcessPid(Long processPid) {
        this.processPid = processPid;
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
                ", processPid=" + processPid +
                ", role=" + role +
                '}';
    }
}
