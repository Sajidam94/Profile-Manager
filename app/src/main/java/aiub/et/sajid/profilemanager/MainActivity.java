package aiub.et.sajid.profilemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
    public static TextView tv;
    public ToggleButton tbutton;
    public static boolean checked;

    Intent proServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv=(TextView)findViewById(R.id.tv);

        tbutton=(ToggleButton)findViewById(R.id.tBtn);
        proServiceIntent = new Intent(this, ProfileService.class);




    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();


        /*tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(((ToggleButton)view).isChecked());
                checked=((ToggleButton)view).isChecked();
                if(checked){
                    startService(proServiceIntent);
                }else {
                    stopService(proServiceIntent);
                }

            }
        });
        */

        if(checked){
            tbutton.setChecked(true);
            tv.setText("Service Running\n");
        }else{
            tbutton.setChecked(false);
            tv.setText("Service Stopped");
        }

        tbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    startService(proServiceIntent);
                } else {
                    // The toggle is disabled
                    stopService(proServiceIntent);
                    tv.setText("Service Stopped");
                }
            }
        });

    }



    /*public void profileHome(){
        if (isFaceUp){mAudioManager.setRingerMode(2);}
    }

    public void profilePocket(){
        if (isShaking){
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            mAudioManager.adjustStreamVolume();
        }
    }

    public void profileSilent(){
        if (isFaceDown){mAudioManager.setRingerMode(2);}
    }*/

}
