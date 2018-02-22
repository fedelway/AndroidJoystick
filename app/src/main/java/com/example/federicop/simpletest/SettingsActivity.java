package com.example.federicop.simpletest;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = null;

    private static void initializeListener(){
        sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {
                String stringValue = value.toString();

                if (preference instanceof ListPreference) {
                    // For list preferences, look up the correct display value in
                    // the preference's 'entries' list.
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);

                    // Set the summary to reflect the new value.
                    preference.setSummary(
                            index >= 0
                                    ? listPreference.getEntries()[index]
                                    : null);

                } else if (preference instanceof RingtonePreference) {
                    // For ringtone preferences, look up the correct display value
                    // using RingtoneManager.
                    if (TextUtils.isEmpty(stringValue)) {
                        // Empty values correspond to 'silent' (no ringtone).
                        preference.setSummary(R.string.pref_ringtone_silent);

                    } else {
                        Ringtone ringtone = RingtoneManager.getRingtone(
                                preference.getContext(), Uri.parse(stringValue));

                        if (ringtone == null) {
                            // Clear the summary if there was a lookup error.
                            preference.setSummary(null);
                        } else {
                            // Set the summary to reflect the new ringtone display
                            // name.
                            String name = ringtone.getTitle(preference.getContext());
                            preference.setSummary(name);
                        }
                    }

                } else {
                    // For all other preferences, set the summary to the value's
                    // simple string representation.
                    executePreferenceChange(preference,stringValue);
                }
                return true;
            }
        };
    }

    private static void executePreferenceChange(Preference pref, String value){

        try {
            if (pref.getKey().equals("ip_preference")) {
                changeIP(value, pref);
            } else if (pref.getKey().equals("port_preference")) {
                changePort(value);
            }

            //Al final del try. Si algo falla, no se actualiza
            pref.setSummary(value);
        }catch(Exception e) {
            Toast.makeText(pref.getContext(),
                    e.getMessage().isEmpty()? e.toString() : e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private static void changeIP(String ip,Preference pref)throws UnknownHostException{
        MainActivity.getSender().address = IpAddressResolver.getInetAddress(ip);

        /*AddressCreator addressCreator = new AddressCreator();
        addressCreator.previousValue = pref.getSummary() == null ? "" : pref.getSummary().toString();
        addressCreator.preference = pref;
        addressCreator.execute(ip);*/
    }

    /*private static void afterChangeIp(int error, String previousValue,Preference preference){

        if(error != AddressCreator.noError){
            preference.setSummary(previousValue);

            if(error == AddressCreator.unknownHostError) {
                Toast.makeText(preference.getContext(), "UNKNOWN HOST", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    private static void changePort(String port){
        MainActivity.getSender().port = Integer.parseInt(port);
    }

    /*private static class AddressCreator extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {

            try{
                //MainActivity.getSender().address = InetAddress.getByName(params[0]);
                MainActivity.getSender().address = IpAddressResolver.getInetAddress(params[0]);
            }catch(UnknownHostException e) {
                error = unknownHostError;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void res){
            afterChangeIp(error,previousValue,preference);
        }

        public static final int noError = 0;
        public static final int unknownHostError = 1;

        public int error = noError;
        public String previousValue;
        public Preference preference;
    }*/

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {

        if(sBindPreferenceSummaryToValueListener == null)
            initializeListener();

        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        //getFragmentManager().beginTransaction().replace(android.R.id.content,
        //        new GeneralPreferenceFragment()).commit();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    /*@Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }*/

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            //setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference("ip_preference"));
            bindPreferenceSummaryToValue(findPreference("port_preference"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
