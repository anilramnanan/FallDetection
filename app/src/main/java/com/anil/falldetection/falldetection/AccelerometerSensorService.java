package com.anil.falldetection.falldetection;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;


public class AccelerometerSensorService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelSensor;
    public static final String Accelerometer_OUT_MSG_X = "A_X";
    public static final String Accelerometer_OUT_MSG_Y = "A_Y";
    public static final String Accelerometer_OUT_MSG_Z = "A_Z";
    public static final String Accelerometer_OUT_MSG_STATUS = "A_S";

    public static final String Accelerometer_IN_MSG = "in";

    @Override
    public void onCreate() {

        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelSensor,SensorManager.SENSOR_DELAY_NORMAL);



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        String sensorResultX = String.valueOf(x);
        String sensorResultY = String.valueOf(y);
        String sensorResultZ = String.valueOf(z);
        Intent broadcastIntent;
        broadcastIntent = new Intent();
        broadcastIntent.setAction(AccelerometerBroadcastReceiver.ACCELEROMETER_ACTION_RESP);
        broadcastIntent.putExtra(Accelerometer_OUT_MSG_X, sensorResultX);
        broadcastIntent.putExtra(Accelerometer_OUT_MSG_Y, sensorResultY);
        broadcastIntent.putExtra(Accelerometer_OUT_MSG_Z, sensorResultZ);
        broadcastIntent.putExtra(Accelerometer_OUT_MSG_STATUS, "");

        sendBroadcast(broadcastIntent);
        System.out.println("Accelerometer sensor changing");
        System.out.println("writing to db " + sensorResultX + " " + sensorResultY + " " + sensorResultZ);

    }

    public void onDestroy() {
       sensorManager.unregisterListener(this,accelSensor);
    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("sensor changing onAccuracyChanged");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
