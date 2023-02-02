package main.java.org.ufu.ds.election;

import java.io.Serializable;

public class Message implements Serializable {

    private String topic;


    public Message(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "Message{" +
                "topic='" + topic + '\'' +
                '}';
    }
}
