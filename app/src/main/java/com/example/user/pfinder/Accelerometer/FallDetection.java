package com.example.user.pfinder.Accelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.pfinder.Location.MyLocation;
import com.example.user.pfinder.R;


public class FallDetection implements SensorEventListener {

    private static String NUMERTEL = "+48737507188";
    private static double BORDERSQUARE = 0.7;
    private static int BREAKTURNS = 5000000;
    private SensorManager accelManage;
    private Sensor senseAccel;
    private Context context;
    private MyLocation myLocation;
    private float valueX;
    private float valueY;
    private float valueZ;

    private int historyLength;
    private float historyValuesX[];
    private float historyValuesY[];
    private float historyValuesZ[];

    private boolean possibleFall;
    private boolean messageIsSended;
    private int position;

    public FallDetection(Context context){
        historyLength = 3;
        historyValuesX = new float[historyLength];
        historyValuesY = new float[historyLength];
        historyValuesZ = new float[historyLength];
        possibleFall = false;
        messageIsSended = false;
        position = 0;

        this.context = context;
        accelManage = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        senseAccel = accelManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_GAME);
    }


    public void onSensorChanged(SensorEvent sensorEvent) {
        // TODO Auto-generated method stub
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double rootSquare;
            valueX = sensorEvent.values[0];
            valueY = sensorEvent.values[1];
            valueZ = sensorEvent.values[2];

            if(possibleFall){
                if(position < historyLength) {
                    historyValuesX[position] = (int)valueX/1;
                    historyValuesY[position] = (int)valueY/1;
                    historyValuesZ[position] = (int)valueZ/1;
                    position++;
                    doBreak();
                }
                else{
                    if(isValuesOfArrEquals(historyValuesX) && isValuesOfArrEquals(historyValuesY) && isValuesOfArrEquals(historyValuesZ)) {
                        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
                        TextView latitudeTextView = (TextView) rootView.findViewById(R.id.latitudeTextView);
                        TextView longitudeTextView = (TextView) rootView.findViewById(R.id.longitudeTextView);
                        Toast.makeText(context, latitudeTextView.getText() + "/" + longitudeTextView.getText() ,Toast.LENGTH_SHORT).show();
                        String message = "Probably I lost my phone. Let me know next coordinates: " + latitudeTextView.getText() + ", " +longitudeTextView.getText();
                        if(!messageIsSended) {
                            sendSMS(NUMERTEL, message);
                            messageIsSended = true;
                        }
                    }
                    position = 0;
                    possibleFall = false;
                }
            }else{
                rootSquare = Math.sqrt(Math.pow( valueX, 2) + Math.pow(valueY, 2) + Math.pow(valueZ, 2));

                if(rootSquare < BORDERSQUARE) {
                    possibleFall = true;
                }
            }
        }
    }

    private boolean isValuesOfArrEquals(float arr[]){
        boolean result = true;
        for(int i = 0; i < (arr.length - 1); i++){
            if(arr[i] != arr[i+1]){
                result = false;
            }
        }
        return result;
    }

    private void doBreak(){
        for(int i =0; i<BREAKTURNS; i++){

        }
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
