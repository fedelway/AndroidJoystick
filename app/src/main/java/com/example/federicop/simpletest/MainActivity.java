package com.example.federicop.simpletest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    EventSender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        EventSenderCreate a = new EventSenderCreate();
        a.execute("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeButtons();
    }

    private class EventSenderCreate extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params){

            try{
                sender = new EventSender(InetAddress.getByName("10.0.4.169"),1060);
            }catch (Exception e){
                return null;
            }

            return null;
        }
    }

    private void initializeButtons(){

        CustomOnTouchListenerFactory factory = new CustomOnTouchListenerFactory(this.sender);
        Button btn;

        btn = findViewById(R.id.btnUp);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_UP));
        btn = findViewById(R.id.btnDown);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_DOWN));
        btn = findViewById(R.id.btnLeft);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_LEFT));
        btn = findViewById(R.id.btnRight);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_RIGHT));
        btn = findViewById(R.id.btnX);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_X));
        btn = findViewById(R.id.btnSquare);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_SQUARE));
        btn = findViewById(R.id.btnTriangle);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_TRIANGLE));
        btn = findViewById(R.id.btnCircle);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_CIRCLE));
        btn = findViewById(R.id.btnSTART);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_START));
        btn = findViewById(R.id.btnSELECT);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_SELECT));
        btn = findViewById(R.id.btnR1);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_R1));
        btn = findViewById(R.id.btnR2);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_R2));
        btn = findViewById(R.id.btnL1);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_L1));
        btn = findViewById(R.id.btnL2);
        btn.setOnTouchListener(factory.getListener(BtnCodes.BTN_L2));

        return;
    }
}
