package com.example.federicop.simpletest;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by FedericoP on 5/1/2018.
 */

public class EventSender {

    Socket socket;

    public InetAddress address;
    public int port = 1060;

    public void startConnection() throws IOException
    {
        if(address == null)
            throw new NullPointerException();

        socket = new Socket();
        socket.connect(new InetSocketAddress(address,port),500);
        socket.setTcpNoDelay(true);
    }

    public void stopConnection() throws IOException
    {
        socket.close();
    }

    public void sendKey(int key, boolean press) throws IOException {
        //Early return if connection not established
        if(socket == null)
            return;

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
