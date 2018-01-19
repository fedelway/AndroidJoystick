package com.example.federicop.simpletest;

import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by FedericoP on 19/1/2018.
 */

public class CustomOnTouchListenerFactory {

    EventSender senderReference;

    CustomOnTouchListenerFactory(EventSender sender){
        this.senderReference = sender;
    }

    public View.OnTouchListener getListener(final int key)
    {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    new SendKeyStroke().execute(key,1);
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                    new SendKeyStroke().execute(key,0);
                else if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL)
                    new SendKeyStroke().execute(key,0);

                return true;
            }
        };
    }

    public class SendKeyStroke extends AsyncTask<Integer,Void,String>
    {
        @Override
        protected String doInBackground(Integer... params){

            int key = params[0];
            boolean press = (params[1] == 0) ? false : true;

            try{
                senderReference.sendKey(key,press);
            }catch(Exception e){
                return null;
            }

            return null;
        }
    }
}
