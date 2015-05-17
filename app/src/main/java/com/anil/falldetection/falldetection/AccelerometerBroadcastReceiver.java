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
    public TextView ACCELEROMETERStatus;

    public AccelerometerBroadcastReceiver(TextView ACCELEROMETERValx, TextView ACCELEROMETERValy, TextView ACCELEROMETERValz, TextView ACCELEROMETERStatus) {
        this.ACCELEROMETERValx = ACCELEROMETERValx;
        this.ACCELEROMETERValy = ACCELEROMETERValy;
        this.ACCELEROMETERValz = ACCELEROMETERValz;
        this.ACCELEROMETERStatus = ACCELEROMETERStatus;

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String x = intent.getStringExtra(AccelerometerSensorService.Accelerometer_OUT_MSG_X);
        String y = intent.getStringExtra(AccelerometerSensorService.Accelerometer_OUT_MSG_Y);
        String z = intent.getStringExtra(AccelerometerSensorService.Accelerometer_OUT_MSG_Z);


        float x1 = Float.parseFloat(x);
        float y1 = Float.parseFloat(y);
        float z1 = Float.parseFloat(z);

        System.out.print("Broadcast x: " + x + " y: " + y + " z: " + z);
        this.ACCELEROMETERValx.setText("X: " + x);
        this.ACCELEROMETERValy.setText("Y: " + y);
        this.ACCELEROMETERValz.setText("Z: " + z);
        float accel2 = (float) Math.sqrt((x1 * x1) + (y1 * y1)  + (z1 * z1));
        if (accel2 < 1) {
            this.ACCELEROMETERStatus.setText("Phone Fell: " + Math.sqrt((x1 * x1) + (y1 * y1) + (z1 * z1)));
            Intent myIntent=new Intent(context, AlertActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);

        }


    }
}