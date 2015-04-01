package com.anil.falldetection.falldetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;


/**
 * Created by Anil on 3/1/15.
 */

public class AccelerometerBroadcastReceiver extends BroadcastReceiver {
    public static final String ACCELEROMETER_ACTION_RESP =
            "com.anil.compdemo.sensordemo.ACCELEROMETER_MESSAGE_PROCESSED";

    public TextView ACCELEROMETERValx;
    public TextView ACCELEROMETERValy;
    public TextView ACCELEROMETERValz;

    public AccelerometerBroadcastReceiver(TextView ACCELEROMETERValx, TextView ACCELEROMETERValy, TextView ACCELEROMETERValz) {
        this.ACCELEROMETERValx = ACCELEROMETERValx;
        this.ACCELEROMETERValy = ACCELEROMETERValy;
        this.ACCELEROMETERValz = ACCELEROMETERValz;

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String x = intent.getStringExtra(AccelerometerSensorService.Accelerometer_OUT_MSG_X);
        String y = intent.getStringExtra(AccelerometerSensorService.Accelerometer_OUT_MSG_Y);
        String z = intent.getStringExtra(AccelerometerSensorService.Accelerometer_OUT_MSG_Z);
        System.out.print("Broadcast x: " + x + " y: " + y + " z: " + z);
        this.ACCELEROMETERValx.setText("X Axis: " + x);
        this.ACCELEROMETERValy.setText("Y Axis: " + y);
        this.ACCELEROMETERValz.setText("Z Axis: " + z);

    }
}