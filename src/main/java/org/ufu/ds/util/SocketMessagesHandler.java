package main.java.org.ufu.ds.util;

import main.java.org.ufu.ds.heartbeat.HeartBeatReceiver;
import main.java.org.ufu.ds.util.Constants;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketMessagesHandler {

    private final HeartBeatReceiver heartBeatReceiver;


    public SocketMessagesHandler(HeartBeatReceiver heartBeatReceiver) {
        this.heartBeatReceiver = heartBeatReceiver;
    }

    public void handle() {

        new Thread(() -> {

            try {
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.bind(new InetSocketAddress(80));
                while (true) {

                    SocketChannel socketChannel = serverSocketChannel.accept();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int readBytes = 0;

                    readBytes = socketChannel.read(buffer);

                    buffer.flip();
                    byte[] data = new byte[readBytes];
                    buffer.get(data);
                    String message = new String(data);

                    switch (message) {
                        case Constants.HEARTBEAT:
                            ByteBuffer responseBuffer = heartBeatReceiver.receiveHeartBeat(socketChannel.socket().getInetAddress().getHostAddress());
                            socketChannel.write(responseBuffer);
                            break;
                    }
                    socketChannel.close();
                }
            } catch (Exception e) {
                System.out.println("SocketMessagesHandler error=" + e);
                e.printStackTrace();
            }
        }).start();
    }

}
