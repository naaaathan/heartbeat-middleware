package main.java.org.ufu.ds;

import java.io.Serializable;

public class Message implements Serializable {

    private String topic;


    public Message(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "Message{" +
                "topic='" + topic + '\'' +
                '}';
    }
}
