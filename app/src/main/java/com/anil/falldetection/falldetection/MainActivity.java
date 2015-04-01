package com.anil.falldetection.falldetection;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

    private AccelerometerBroadcastReceiver accelReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAccelerometerService (View v) {

        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleFallDetect);
        TextView xInput = (TextView) findViewById(R.id.xValue);
        TextView yInput = (TextView) findViewById(R.id.yValue);
        TextView zInput = (TextView) findViewById(R.id.zValue);

        String strInputMsg = xInput.getText().toString();
        xInput.setText("Starting Accelerometer Service");
        Intent msgIntent = new Intent(this, AccelerometerSensorService.class);
        msgIntent.putExtra(AccelerometerSensorService.Accelerometer_IN_MSG, strInputMsg);

        if (toggleButton.isChecked()) {

            startService(msgIntent);

            IntentFilter filter = new IntentFilter(AccelerometerBroadcastReceiver.ACCELEROMETER_ACTION_RESP);
            accelReceiver = new AccelerometerBroadcastReceiver(xInput, yInput, zInput);
            registerReceiver(accelReceiver, filter);

        } else {
            xInput.setText("Accelerometer Service Stopped");
            yInput.setText("");
            zInput.setText("");
            unregisterReceiver(accelReceiver);
            stopService(new Intent(this, AccelerometerSensorService.class));
        }


    }

}
