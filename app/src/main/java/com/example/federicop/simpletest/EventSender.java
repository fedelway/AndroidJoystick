package com.example.federicop.simpletest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by FedericoP on 5/1/2018.
 */

public class EventSender {

    Socket socket;

    public EventSender(InetAddress address, int port) throws IOException
    {
        socket = new Socket(address,port);
        socket.setTcpNoDelay(true);
    }

    public void sendKey(int key, boolean press) throws IOException {
        final int packetSize = 3 + 2 + 2 + 1;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.reset();

        stream.write("MSG004".getBytes(Charset.forName("UTF-8")));

        String keyStr = new Integer(key).toString();
        String data = "00".substring(keyStr.length()) + keyStr;
        if(press)
            data += "S";
        else data += "N";

        stream.write(data.getBytes(Charset.forName("UTF-8")));

        socket.getOutputStream().write(stream.toByteArray());
        socket.getOutputStream().flush();
    }
}
