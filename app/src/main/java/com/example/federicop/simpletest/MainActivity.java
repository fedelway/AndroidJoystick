package com.example.federicop.simpletest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static EventSender sender = new EventSender();

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);

        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.startoption:
                start();
                return true;
            case R.id.stopoption:
                stop();
                return true;
            case R.id.configoption:
                startConfig();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startConfig() {
        final String EXTRA_SHOW_FRAGMENT = ":android:show_fragment";
        final String EXTRA_NO_HEADERS = ":android:no_headers";

        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(EXTRA_SHOW_FRAGMENT,SettingsActivity.GeneralPreferenceFragment.class.getName());
        //intent.putExtra(EXTRA_NO_HEADERS,true);
        startActivity(intent);
    }

    private void start() {
        EventSenderStarter starter = new EventSenderStarter();
        starter.execute();
    }

    private void afterStart(int error){

        if(error == EventSenderStarter.noError){
            MenuItem startOption = menu.findItem(R.id.startoption);
            startOption.setEnabled(false);startOption.setVisible(false);
            MenuItem stopOption = menu.findItem(R.id.stopoption);
            stopOption.setEnabled(true);stopOption.setVisible(true);
        }else{
            String errorText = "ERROR";
            if(error == EventSenderStarter.connError)
                errorText = "CONNECTION ERROR";
            if(error == EventSenderStarter.addrError)
                errorText = "ADDRESS NOT SET";

            showToast(errorText,Toast.LENGTH_SHORT);
        }
    }

    private void stop()
    {
        EventSenderStopper stopper = new EventSenderStopper();
        stopper.execute();
    }

    private void afterStop(int error){

        if(error == EventSenderStopper.noError){
            MenuItem startOption = menu.findItem(R.id.startoption);
            startOption.setEnabled(true);startOption.setVisible(true);
            MenuItem stopOption = menu.findItem(R.id.stopoption);
            stopOption.setEnabled(false);startOption.setVisible(false);
        }else{
            String errorText = "ERROR";
            if(error == EventSenderStopper.connError){
                errorText = "CONNECTION ERROR";
            }

            showToast(errorText,Toast.LENGTH_SHORT);
        }
    }

    private class EventSenderStarter extends AsyncTask<Void,Void,Void>{
        @Override
        public Void doInBackground(Void... params) {
            try {
                sender.startConnection();
            } catch (IOException e) {
                error = connError;
            } catch (NullPointerException e){
                error = addrError;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            afterStart(this.error);
        }

        public static final int noError = 0;
        public static final int connError = 1;
        public static final int addrError = 2;

        public int error = noError;
    }

    private class EventSenderStopper extends AsyncTask<Void,Void,Void>{
        @Override
        public Void doInBackground(Void... params) {
            try {
                sender.stopConnection();
            } catch (IOException e) {
                error = connError;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void res) {
            afterStop(this.error);
        }

        public static final int noError = 0;
        public static final int connError = 1;

        public int error = noError;
    }

    private void showToast(final String text, final int duration){
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,text,duration).show();
            }
        });
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

    public static EventSender getSender(){
        return sender;
    }
}