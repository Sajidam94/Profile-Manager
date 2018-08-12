package aiub.et.sajid.profilemanager;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import static aiub.et.sajid.profilemanager.MainActivity.tv;

public class ProfileService extends Service implements SensorEventListener {

    boolean isFaceUp=false;
    boolean isFaceDown=false;
    boolean isShaking=false;
    boolean isZero=false;
    String tvText;

    private SensorManager mSensorManager;
    public Sensor mSensorP, mSensorA;

    private AudioManager mAudioManager;

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mSensorManager.registerListener(this, mSensorP, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorA, SensorManager.SENSOR_DELAY_NORMAL);
        mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ProfileService.this.getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.getMessage();
        }
        return Service.START_CONTINUATION_MASK;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.checked=false;
        mSensorManager.unregisterListener(this);
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(ProfileService.this.getApplicationContext(),"Service Stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorP = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorA = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        MainActivity.checked=true;


    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            tv.setText("Proximity: "+event.values[0]);
            if(event.values[0]==0){
                mAudioManager.setRingerMode(0);
                isZero=true;
                tvText= (String) tv.getText();
                System.out.println("Silent");
            }else{
                isFaceUp=true;
                isZero=false;
                mAudioManager.setRingerMode(2);
                System.out.println("Normal Mode Activated");
            }
        }

        else if(isZero){
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                System.out.println("X: "+event.values[0]+" Y: "+event.values[1]+" Z: "+event.values[2]);
                tv.setText(tvText+"\nAccelerometer:\nX: "+event.values[0]+"Y: "+event.values[1]+"Z: "+event.values[0]);

                //Face Up
                if( event.values[1] >=0 && event.values[1]<=1){
                    isFaceUp=true;
                    isShaking=false;
                    isFaceDown=false;
                    mAudioManager.setRingerMode(2);
                    System.out.println("Normal Mode");
                }

                //Face Down
                else if( event.values[1]>=-1 && event.values[1] <0){
                    isFaceDown=true;
                    isFaceUp=false;
                    isShaking=false;
                    mAudioManager.setRingerMode(0);
                    System.out.println("silent Mode");
                }
                //Shaking
                else{
                    isShaking=true;
                    isFaceUp=false;
                    isFaceDown=false;
                    mAudioManager.setRingerMode(1);
                    System.out.println("Vibrate Mode");
                }
            }
        }

          /*if (isFaceUp && !isFaceDown && !isShaking){mAudioManager.setRingerMode(2);}
          else if(isFaceDown && !isFaceUp &&!isShaking){mAudioManager.setRingerMode(0);}
          else if(isShaking && !isFaceUp &&!isFaceDown){mAudioManager.setRingerMode(1);}
          else{
                tv.setText("Error");
          }*/

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
