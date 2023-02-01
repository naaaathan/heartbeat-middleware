package main.java.org.ufu.ds;

import main.java.org.ufu.ds.election.HostInfo;

import java.io.Serializable;
import java.util.List;

public class CoordinatorMessage extends Message implements Serializable {

    private List<HostInfo> hostInfoList;

    public CoordinatorMessage(String topic, List<HostInfo> hostInfoList) {
        super(topic);
        this.hostInfoList = hostInfoList;
    }

    public List<HostInfo> getHostInfoList() {
        return hostInfoList;
    }

    public void setHostInfoList(List<HostInfo> hostInfoList) {
        this.hostInfoList = hostInfoList;
    }

    @Override
    public String toString() {
        return "CoordinatorMessage{" +
                "topic=" + getTopic() + "," +
                "hostInfoList=" + hostInfoList +
                '}';
    }
}
