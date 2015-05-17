package com.anil.falldetection.falldetection;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    private AccelerometerBroadcastReceiver accelReceiver;
    private TextView xInput;
    private TextView yInput;
    private TextView zInput;
    private TextView statusInput;
    private ValueAnimator va;
    private SharedPreferences settings;
    private GPSTracking gps;
    private Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        xInput = (TextView) findViewById(R.id.xValue);
        yInput = (TextView) findViewById(R.id.yValue);
        zInput = (TextView) findViewById(R.id.zValue);
        statusInput = (TextView) findViewById(R.id.StatusValue);


        xInput.setVisibility(View.GONE);
        yInput.setVisibility(View.GONE);
        zInput.setVisibility(View.GONE);
        statusInput.setVisibility(View.GONE);

        int start = Color.rgb(0x00, 0x99, 0x00);
        int end = Color.rgb(0x00, 0xff, 0x00);
        va = ObjectAnimator.ofInt(findViewById(R.id.toggleFallDetect), "backgroundColor", start, end);
        va.setDuration(750);
        va.setEvaluator(new ArgbEvaluator());
        va.setRepeatCount(ValueAnimator.INFINITE);
        va.setRepeatMode(ValueAnimator.REVERSE);

        gps = new GPSTracking(this);

        if(gps.canGetLocation()) {

            currentLocation = new Location("");
            currentLocation.setLatitude(gps.getLatitude());
            currentLocation.setLongitude(gps.getLongitude());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,
                    SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAlertActivity (View v) {
        Intent intent = new Intent(this,
                AlertActivity.class);
        startActivity(intent);

    }

    public void startAccelerometerService (View v) {

        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleFallDetect);
        boolean debugMode = settings.getBoolean("debug_pref", false);

        if (debugMode) {
            xInput.setVisibility(View.VISIBLE);
            yInput.setVisibility(View.VISIBLE);
            zInput.setVisibility(View.VISIBLE);
            statusInput.setVisibility(View.VISIBLE);
        } else {
            xInput.setVisibility(View.GONE);
            yInput.setVisibility(View.GONE);
            zInput.setVisibility(View.GONE);
            statusInput.setVisibility(View.GONE);
        }

        String strInputMsg = xInput.getText().toString();
        Intent msgIntent = new Intent(this, AccelerometerSensorService.class);
        msgIntent.putExtra(AccelerometerSensorService.Accelerometer_IN_MSG, strInputMsg);


        if (toggleButton.isChecked()) {
            xInput.setText("Starting Accelerometer Service");
            startService(msgIntent);
            IntentFilter filter = new IntentFilter(AccelerometerBroadcastReceiver.ACCELEROMETER_ACTION_RESP);
            accelReceiver = new AccelerometerBroadcastReceiver(xInput, yInput, zInput, statusInput);
            registerReceiver(accelReceiver, filter);
            va.start();

        } else {
            xInput.setText("Accelerometer Service Stopped");
            yInput.setText("");
            zInput.setText("");
            statusInput.setText("");
            unregisterReceiver(accelReceiver);
            stopService(new Intent(this, AccelerometerSensorService.class));
            va.end();
            toggleButton.setBackgroundColor(Color.LTGRAY);
        }


    }

}
