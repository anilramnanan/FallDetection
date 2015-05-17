package com.anil.falldetection.falldetection;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class AlertActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final int REQUEST_CODE = 1234;
    private TextToSpeech textToSpeech;
    private Intent voiceIntent;
    private SpeechCountDownTimer countDownTimer;
    private long timeElapsed;
    private final long startTime = 20000;
    private final long interval = 1000;
    private SharedPreferences settings;
    private GPSTracking gps;
    private Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        textToSpeech = new TextToSpeech(this, this);
        countDownTimer = new SpeechCountDownTimer(startTime, interval);
        countDownTimer.start();

    }



    public void speakButtonClicked(View v) {
        startVoiceRecognitionActivity();
    }

    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Have you fallen ? (Yes/No)");
        startActivityForResult(voiceIntent, REQUEST_CODE);
    }

    private void stopVoiceRecognition() {

    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            countDownTimer.cancel();
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this.getBaseContext(), matches.get(0).toString(),
                    Toast.LENGTH_SHORT).show();
            if (matches.get(0).toLowerCase().contentEquals("no")) {
                convertTextToSpeech("Ok, cancelling the alert");

                final Handler handler = new Handler();
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                finish();
                            }
                        });
                    }
                }, 3000);


            }
            if (matches.get(0).toLowerCase().contentEquals("yes")) {
                convertTextToSpeech("Sending an alert!");
                sendSMS("I have fallen. Please send help");
                final Handler handler = new Handler();
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                finish();
                            }
                        });
                    }
                }, 3000);

            }
            if (!matches.get(0).toLowerCase().contentEquals("yes") && !matches.get(0).toLowerCase().contentEquals("no")) {
                convertTextToSpeech("I did not understand you. If you cannot respond I will send an alert");
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void convertTextToSpeech(String Value) {

        textToSpeech.speak(Value, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void stopAlert(View v) {
        countDownTimer.cancel();
        finish();

    }

    public void startAlert(View v) {
        countDownTimer.cancel();
        convertTextToSpeech("Calling Emergency Services");
        sendSMS("I have fallen. Please send help");



    }

    private void sendSMS(String message)
    {
        String contact = settings.getString("emergency_contact","none");
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(contact, null, message, null, null);
        Toast.makeText(AlertActivity.this.getBaseContext(), "SMS Sent to "+ contact, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {

                convertTextToSpeech("Have you fallen ? Would you like to alert someone ?");
                final Handler handler = new Handler();
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                startVoiceRecognitionActivity();
                            }
                        });
                    }
                }, 4000);




            }
        } else {
            Log.e("error", "Initilization Failed!");
        }
    }


    @Override
    public void onDestroy() {
        textToSpeech.shutdown();
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    protected void onStop() {

        countDownTimer.cancel();
        super.onStop();
    }


    public class SpeechCountDownTimer extends CountDownTimer {

        public SpeechCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            finishActivity(REQUEST_CODE);
            Toast.makeText(AlertActivity.this.getBaseContext(), "Timer Complete",
                    Toast.LENGTH_SHORT).show();
            convertTextToSpeech("You have not responded. Sending an alert!");
            sendSMS("I have fallen. Please send help");

            final Handler handler = new Handler();
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            finish();
                        }
                    });
                }
            }, 4000);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            timeElapsed = startTime - millisUntilFinished;


        }
    }

}


