package com.example.federicop.simpletest;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by FedericoP on 24/1/2018.
 */

public class IpAddressResolver {

    public static InetAddress getInetAddress(String address) throws UnknownHostException{
        String[] numbers = address.split("\\.");

        if( numbers.length != 4 )
            throw new UnknownHostException("Incorrect Format");

        byte[] bytesAddr = new byte[4];

        try{
            for(int i=0; i<4; i++){
                int byteValue = Integer.parseInt(numbers[i]);

                if(byteValue > 255)
                    throw new NumberFormatException();

                bytesAddr[i] = (byte)byteValue;
            }
        }catch(NumberFormatException e){
            throw new UnknownHostException("Incorrect Format");
        }

        return InetAddress.getByAddress(bytesAddr);
    }
}
