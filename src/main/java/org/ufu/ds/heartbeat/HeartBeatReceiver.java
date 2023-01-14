package main.java.org.ufu.ds.heartbeat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class HeartBeatReceiver extends HeartBeat {

    private ServerSocketChannel serverSocketChannel;
    private final int port;

    public HeartBeatReceiver(Long heartBeatIntervalInMs, int port) {
        this.timeBetweenHeartbeats = heartBeatIntervalInMs;
        this.port = port;
    }

    @Override
    public void startHeartBeat() {

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));

            while (true) {

                System.out.println("StartHeartBeat");

                SocketChannel socketChannel = serverSocketChannel.accept();

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(buffer);
                buffer.flip();
                byte[] data = new byte[readBytes];
                buffer.get(data);
                String message = new String(data);

                System.out.println("Received: " + message);

                socketChannel.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
